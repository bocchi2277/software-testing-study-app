package com.example.ui

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.CardsData
import com.example.data.SyllabusData
import com.example.db.QaRepository
import com.example.model.AppState
import com.example.model.CardState
import com.example.model.ChapterBreakdown
import com.example.model.ExamResult
import com.example.model.IstqbFlashcard
import com.example.model.NoteItem
import com.example.model.Profile
import com.example.model.QuizAttempt
import com.example.model.StudySession
import com.example.model.TaskItem
import com.example.model.UiState
import com.example.util.DateUtils
import com.example.util.StatsUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.math.roundToInt

enum class TimerMode { FOCUS, BREAK }

data class TimerUiState(
    val mode: TimerMode = TimerMode.FOCUS,
    val leftSeconds: Int = 25 * 60,
    val totalSeconds: Int = 25 * 60,
    val running: Boolean = false,
    val topicId: String = "",
    val completedToday: Int = 0,
    val lastRewardMessage: String? = null
) {
    val elapsedSeconds: Int
        get() = (totalSeconds - leftSeconds).coerceAtLeast(0)

    val elapsedFocusMinutes: Int
        get() = if (mode == TimerMode.FOCUS) elapsedSeconds / 60 else 0

    val progressFraction: Float
        get() = if (totalSeconds > 0) (elapsedSeconds.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f) else 0f
}

class QaViewModel(
    private val repository: QaRepository?,
    val currentUserId: String?,
    private val onCycleNotify: () -> Unit = {},
    initialThemeMode: String = "auto",
    private val onPersistLocalTheme: (String) -> Unit = {}
) : ViewModel() {

    private companion object {
        const val TAG = "QaViewModel"
        const val STOP_TIMEOUT_MILLIS = 5000L
        val VALID_THEMES = setOf("auto", "light", "dark")
        fun sanitizeTheme(mode: String): String = if (mode in VALID_THEMES) mode else "auto"
    }

    val isGuest: Boolean
        get() = repository == null || currentUserId.isNullOrBlank()

    private val backupJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    private val _cachedThemeMode = MutableStateFlow(sanitizeTheme(initialThemeMode))
    val cachedThemeMode: StateFlow<String> = _cachedThemeMode.asStateFlow()

    private val _guestState = MutableStateFlow(
        AppState(
            profile = Profile(
                startDate = DateUtils.todayStr(),
                theme = sanitizeTheme(initialThemeMode)
            ),
            flashcards = CardsData.toDefaultFlashcards()
        )
    )

    val uiState: StateFlow<UiState<AppState>> = if (repository != null && !currentUserId.isNullOrBlank()) {
        repository.observeAppState(currentUserId)
            .map<AppState, UiState<AppState>> { UiState.Success(it) }
            .catch { error ->
                val firestoreError = generateSequence(error) { it.cause }
                    .filterIsInstance<FirebaseFirestoreException>()
                    .firstOrNull()
                if (firestoreError == null && error is CancellationException) throw error
                val rootError = firestoreError ?: error
                Log.w(TAG, "Error observing user progress", rootError)
                emit(UiState.Error(rootError.localizedMessage ?: "تعذّرت مزامنة البيانات السحابية."))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
                initialValue = UiState.Loading
            )
    } else {
        _guestState
            .map<AppState, UiState<AppState>> { UiState.Success(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = UiState.Success(_guestState.value)
            )
    }

    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    private val _mutatingChapterIds = MutableStateFlow<Set<Int>>(emptySet())
    val mutatingChapterIds: StateFlow<Set<Int>> = _mutatingChapterIds.asStateFlow()

    private val _checkingInStreak = MutableStateFlow(false)
    val checkingInStreak: StateFlow<Boolean> = _checkingInStreak.asStateFlow()

    private val _syncingFlashcards = MutableStateFlow(false)
    val syncingFlashcards: StateFlow<Boolean> = _syncingFlashcards.asStateFlow()

    private var timerJob: Job? = null
    private var endAtMs: Long = 0L

    private val currentAppState: AppState
        get() = (uiState.value as? UiState.Success)?.data ?: _guestState.value

    init {
        val repo = repository
        val uid = currentUserId
        if (repo != null && !uid.isNullOrBlank()) {
            viewModelScope.launch {
                repo.ensureUserProfile(uid).onFailure { e ->
                    _syncError.value = e.localizedMessage ?: "تعذّر تهيئة الملف الشخصي."
                }
                repo.ensureFlashcardsSeeded(uid).onFailure { e ->
                    _syncError.value = e.localizedMessage ?: "تعذّرت مزامنة بطاقات المصطلحات."
                }
            }
        }
        viewModelScope.launch {
            uiState.collect { st ->
                if (st is UiState.Success) {
                    val profile = st.data.profile
                    val safeTheme = sanitizeTheme(profile.theme)
                    if (_cachedThemeMode.value != safeTheme) {
                        _cachedThemeMode.value = safeTheme
                        onPersistLocalTheme(safeTheme)
                    }
                    _timerState.update { cur ->
                        if (!cur.running && cur.leftSeconds == cur.totalSeconds) {
                            val targetMin = if (cur.mode == TimerMode.FOCUS) profile.focusMin else profile.breakMin
                            val sec = targetMin * 60
                            cur.copy(leftSeconds = sec, totalSeconds = sec)
                        } else {
                            cur
                        }
                    }
                }
            }
        }
    }

    fun clearSyncError() {
        _syncError.value = null
    }

    fun retryProfileInit() {
        val repo = repository ?: return
        val uid = currentUserId ?: return
        viewModelScope.launch {
            repo.ensureUserProfile(uid).onFailure { e ->
                _syncError.value = e.localizedMessage ?: "تعذّر الاتصال بالسحابة."
            }
        }
    }

    private fun handleMutationResult(result: Result<*>, fallbackMsg: String) {
        result.onFailure { e ->
            _syncError.value = e.localizedMessage ?: fallbackMsg
        }
    }

    private fun Profile.withRecordedStudy(studyDay: String = DateUtils.todayStr()): Profile {
        val safeDay = studyDay.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr()
        val prevLast = lastStudyDate?.takeIf { DateUtils.isValidDay(it) }
        if (prevLast == safeDay && currentStreak >= 1) return this
        val nextStreak = if (prevLast != null) {
            val diff = DateUtils.daysBetween(prevLast, safeDay)
            when {
                diff < 0 -> return this
                diff == 0 -> maxOf(1, currentStreak)
                diff == 1 -> (currentStreak + 1).coerceAtMost(36500)
                else -> 1
            }
        } else {
            1
        }
        return copy(
            currentStreak = nextStreak,
            bestStreak = maxOf(bestStreak, nextStreak).coerceAtMost(36500),
            lastStudyDate = safeDay
        )
    }

    fun setTimerTopic(topicId: String) {
        _timerState.update { it.copy(topicId = topicId) }
    }

    fun dismissRewardMessage() {
        _timerState.update { it.copy(lastRewardMessage = null) }
    }

    fun applyPomodoroPreset(focusMin: Int, breakMin: Int) {
        val safeFocus = focusMin.coerceIn(5, 120)
        val safeBreak = breakMin.coerceIn(1, 60)
        updateProfile { it.copy(focusMin = safeFocus, breakMin = safeBreak) }
        _timerState.update { cur ->
            if (cur.running) {
                cur
            } else {
                val sec = (if (cur.mode == TimerMode.FOCUS) safeFocus else safeBreak) * 60
                cur.copy(leftSeconds = sec, totalSeconds = sec)
            }
        }
    }

    fun startFocusOnTopic(topicId: String) {
        val focusSec = currentAppState.profile.focusMin * 60
        timerJob?.cancel()
        _timerState.update {
            it.copy(
                mode = TimerMode.FOCUS,
                topicId = topicId,
                leftSeconds = focusSec,
                totalSeconds = focusSec,
                running = false
            )
        }
        startTimer()
    }

    fun startTimer() {
        if (_timerState.value.running) return
        val leftSec = _timerState.value.leftSeconds.coerceAtLeast(1)
        endAtMs = System.currentTimeMillis() + leftSec * 1000L
        _timerState.update { it.copy(running = true, lastRewardMessage = null) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(250)
                val remaining = ((endAtMs - System.currentTimeMillis()) / 1000.0).roundToInt().coerceAtLeast(0)
                _timerState.update { it.copy(leftSeconds = remaining) }
                if (remaining <= 0) {
                    completeTimerCycle()
                    break
                }
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        endAtMs = 0L
        _timerState.update { it.copy(running = false) }
    }

    fun resetTimer() {
        timerJob?.cancel()
        endAtMs = 0L
        val profile = currentAppState.profile
        _timerState.update { cur ->
            val sec = (if (cur.mode == TimerMode.FOCUS) profile.focusMin else profile.breakMin) * 60
            cur.copy(running = false, leftSeconds = sec, totalSeconds = sec)
        }
    }

    fun skipTimer() {
        timerJob?.cancel()
        endAtMs = 0L
        val profile = currentAppState.profile
        _timerState.update { cur ->
            val nextMode = if (cur.mode == TimerMode.FOCUS) TimerMode.BREAK else TimerMode.FOCUS
            val sec = (if (nextMode == TimerMode.FOCUS) profile.focusMin else profile.breakMin) * 60
            cur.copy(mode = nextMode, running = false, leftSeconds = sec, totalSeconds = sec)
        }
    }

    fun finishAndLogActiveFocus() {
        val cur = _timerState.value
        if (cur.mode != TimerMode.FOCUS) return
        val elapsedMin = cur.elapsedFocusMinutes
        if (elapsedMin < 1) return

        timerJob?.cancel()
        endAtMs = 0L
        val profile = currentAppState.profile
        val breakSec = profile.breakMin * 60
        addSession(
            minutes = elapsedMin,
            kind = "focus",
            topic = cur.topicId.ifBlank { null }
        )
        _timerState.update {
            it.copy(
                running = false,
                mode = TimerMode.BREAK,
                leftSeconds = breakSec,
                totalSeconds = breakSec,
                completedToday = it.completedToday + 1
            )
        }
    }

    private fun completeTimerCycle() {
        endAtMs = 0L
        onCycleNotify()
        val cur = _timerState.value
        val profile = currentAppState.profile
        if (cur.mode == TimerMode.FOCUS) {
            val completedMinutes = (cur.totalSeconds / 60).coerceIn(1, 600)
            val breakSec = profile.breakMin * 60
            addSession(
                minutes = completedMinutes,
                kind = "focus",
                topic = cur.topicId.ifBlank { null }
            )
            _timerState.update {
                it.copy(
                    running = false,
                    mode = TimerMode.BREAK,
                    leftSeconds = breakSec,
                    totalSeconds = breakSec,
                    completedToday = it.completedToday + 1
                )
            }
        } else {
            val focusSec = profile.focusMin * 60
            _timerState.update {
                it.copy(
                    running = false,
                    mode = TimerMode.FOCUS,
                    leftSeconds = focusSec,
                    totalSeconds = focusSec
                )
            }
        }
    }

    fun setThemeMode(mode: String) {
        val safeMode = sanitizeTheme(mode)
        _cachedThemeMode.value = safeMode
        onPersistLocalTheme(safeMode)
        updateProfile { it.copy(theme = safeMode) }
    }

    fun toggleLightDarkTheme(isCurrentlyDark: Boolean) {
        val next = if (isCurrentlyDark) "light" else "dark"
        setThemeMode(next)
    }

    fun updateProfile(transform: (Profile) -> Profile) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur ->
                val nextProfile = transform(cur.profile)
                val safeTheme = sanitizeTheme(nextProfile.theme)
                if (_cachedThemeMode.value != safeTheme) {
                    _cachedThemeMode.value = safeTheme
                    onPersistLocalTheme(safeTheme)
                }
                cur.copy(profile = nextProfile.copy(theme = safeTheme))
            }
            return
        }
        val optimisticProfile = transform(currentAppState.profile)
        val optimisticTheme = sanitizeTheme(optimisticProfile.theme)
        if (_cachedThemeMode.value != optimisticTheme) {
            _cachedThemeMode.value = optimisticTheme
            onPersistLocalTheme(optimisticTheme)
        }
        viewModelScope.launch {
            val res = repo.updateProfile(uid, currentAppState.profile, transform)
            handleMutationResult(res, "تعذّر تحديث الإعدادات.")
        }
    }

    fun setProgress(key: String, value: String?) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val cleanKey = key.trim().take(120)
            if (cleanKey.isEmpty()) return
            _guestState.update { cur ->
                val nextProgress = cur.progress.toMutableMap()
                if (value.isNullOrEmpty()) {
                    nextProgress.remove(cleanKey)
                } else {
                    nextProgress[cleanKey] = value.take(200)
                }
                cur.copy(
                    progress = nextProgress,
                    profile = if (!value.isNullOrEmpty()) cur.profile.withRecordedStudy() else cur.profile
                )
            }
            return
        }
        viewModelScope.launch {
            val res = repo.setProgress(uid, key, value)
            handleMutationResult(res, "تعذّر حفظ التقدم.")
        }
    }

    fun setChapterCompleted(chapterId: Int, completed: Boolean) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val chapter = SyllabusData.findChapter(chapterId) ?: return
            _guestState.update { cur ->
                val nextProgress = cur.progress.toMutableMap()
                chapter.topics.forEach { t ->
                    val k = "t:${t.id}"
                    if (completed) nextProgress[k] = "done" else nextProgress.remove(k)
                }
                cur.copy(
                    progress = nextProgress,
                    profile = if (completed) cur.profile.withRecordedStudy() else cur.profile
                )
            }
            return
        }
        if (chapterId in _mutatingChapterIds.value) return
        viewModelScope.launch {
            _mutatingChapterIds.update { it + chapterId }
            try {
                val res = repo.setChapterCompleted(uid, chapterId, completed)
                handleMutationResult(res, "تعذّر تحديث حالة الفصل.")
            } finally {
                _mutatingChapterIds.update { it - chapterId }
            }
        }
    }

    fun checkInDailyStudy() {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur -> cur.copy(profile = cur.profile.withRecordedStudy()) }
            return
        }
        if (_checkingInStreak.value) return
        viewModelScope.launch {
            _checkingInStreak.value = true
            try {
                val res = repo.checkInDailyStudy(uid)
                handleMutationResult(res, "تعذّر تسجيل نشاط اليوم.")
            } finally {
                _checkingInStreak.value = false
            }
        }
    }

    fun addSession(minutes: Int, kind: String, topic: String? = null) {
        val safeMinutes = minutes.coerceIn(1, 600)
        val safeKind = kind.trim().ifEmpty { "focus" }.take(30)
        val safeTopic = topic?.trim()?.takeIf { it.isNotEmpty() }?.take(30)
        val earnedPoints = StatsUtils.pointsForSession(safeMinutes, safeKind)
        val rewardBanner = "تم تسجيل $safeMinutes دقيقة (+$earnedPoints نقطة تقدم 🍅)"

        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val today = DateUtils.todayStr()
            val item = StudySession(
                id = UUID.randomUUID().toString(),
                day = today,
                minutes = safeMinutes,
                kind = safeKind,
                topic = safeTopic
            )
            _guestState.update { cur ->
                val updatedProfile = cur.profile
                    .withRecordedStudy(today)
                    .copy(
                        totalFocusMinutes = (cur.profile.totalFocusMinutes + safeMinutes).coerceAtMost(1_000_000),
                        focusPoints = (cur.profile.focusPoints + earnedPoints).coerceAtMost(10_000_000)
                    )
                cur.copy(
                    sessions = listOf(item) + cur.sessions,
                    profile = updatedProfile
                )
            }
            _timerState.update { it.copy(lastRewardMessage = rewardBanner) }
            return
        }
        viewModelScope.launch {
            val res = repo.addSession(uid, safeMinutes, safeKind, safeTopic)
            if (res.isSuccess) {
                _timerState.update { it.copy(lastRewardMessage = rewardBanner) }
            }
            handleMutationResult(res, "تعذّر تسجيل الجلسة.")
        }
    }

    fun deleteSession(id: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur -> cur.copy(sessions = cur.sessions.filterNot { it.id == id }) }
            return
        }
        viewModelScope.launch {
            val res = repo.deleteSession(uid, id)
            handleMutationResult(res, "تعذّر حذف الجلسة.")
        }
    }

    fun reviewCard(cardId: String, known: Boolean) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val today = DateUtils.todayStr()
            _guestState.update { cur ->
                val updatedCard = com.example.util.LeitnerScheduler.nextCardState(
                    prev = cur.cards[cardId],
                    known = known,
                    today = today
                )
                cur.copy(
                    cards = cur.cards + (cardId to updatedCard),
                    profile = cur.profile.withRecordedStudy(today)
                )
            }
            return
        }
        viewModelScope.launch {
            val res = repo.reviewCard(uid, cardId, known)
            handleMutationResult(res, "تعذّر حفظ مراجعة البطاقة.")
        }
    }

    fun resetCards() {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { it.copy(cards = emptyMap()) }
            return
        }
        viewModelScope.launch {
            val res = repo.resetCards(uid)
            handleMutationResult(res, "تعذّر تصفير البطاقات.")
        }
    }

    fun refreshFlashcardsFromFirestore() {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            if (_guestState.value.flashcards.isEmpty()) {
                _guestState.update { it.copy(flashcards = CardsData.toDefaultFlashcards()) }
            }
            return
        }
        if (_syncingFlashcards.value) return
        viewModelScope.launch {
            _syncingFlashcards.value = true
            try {
                val res = repo.refreshFlashcards(uid)
                handleMutationResult(res, "تعذّر جلب بطاقات المصطلحات من Firestore.")
            } finally {
                _syncingFlashcards.value = false
            }
        }
    }

    fun saveCustomFlashcard(
        term: String,
        question: String,
        answer: String,
        chapter: Int,
        hint: String?
    ) {
        val cleanTerm = term.trim().take(120)
        val cleanQuestion = question.trim().ifEmpty { "ما المقصود بمصطلح $cleanTerm؟" }.take(400)
        val cleanAnswer = answer.trim().take(600)
        if (cleanTerm.isEmpty() || cleanAnswer.isEmpty()) return
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val item = IstqbFlashcard(
                id = "custom-${UUID.randomUUID().toString().take(8)}",
                term = cleanTerm,
                question = cleanQuestion,
                answer = cleanAnswer,
                chapter = chapter.coerceIn(1, 6),
                hint = hint?.trim()?.takeIf { it.isNotEmpty() }?.take(200),
                source = "custom"
            )
            _guestState.update { cur ->
                val base = if (cur.flashcards.isEmpty()) CardsData.toDefaultFlashcards() else cur.flashcards
                cur.copy(flashcards = listOf(item) + base)
            }
            return
        }
        viewModelScope.launch {
            val res = repo.saveFlashcard(uid, cleanTerm, cleanQuestion, cleanAnswer, chapter, hint)
            handleMutationResult(res, "تعذّر حفظ البطاقة الجديدة.")
        }
    }

    fun deleteCustomFlashcard(cardId: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur ->
                cur.copy(
                    flashcards = cur.flashcards.filterNot { it.id == cardId },
                    cards = cur.cards - cardId
                )
            }
            return
        }
        viewModelScope.launch {
            val res = repo.deleteFlashcard(uid, cardId)
            handleMutationResult(res, "تعذّر حذف البطاقة.")
        }
    }

    fun recordAttempt(questionId: String, correct: Boolean, mode: String) {
        recordAttempts(listOf(questionId to correct), mode)
    }

    fun recordAttempts(items: List<Pair<String, Boolean>>, mode: String) {
        if (items.isEmpty()) return
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val now = DateUtils.nowIso()
            val safeMode = mode.trim().ifEmpty { "practice" }.take(20)
            val newAttempts = items.mapNotNull { (qId, ok) ->
                val clean = qId.trim().take(30)
                if (clean.isEmpty()) null else QuizAttempt(clean, ok, safeMode, now)
            }
            _guestState.update { cur ->
                cur.copy(
                    attempts = newAttempts + cur.attempts,
                    profile = cur.profile.withRecordedStudy()
                )
            }
            return
        }
        viewModelScope.launch {
            val res = repo.recordAttempts(uid, items, mode)
            handleMutationResult(res, "تعذّر حفظ سجل الإجابات.")
        }
    }

    fun clearAttempts() {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { it.copy(attempts = emptyList()) }
            return
        }
        viewModelScope.launch {
            val res = repo.clearAttempts(uid)
            handleMutationResult(res, "تعذّر مسح سجل الأسئلة.")
        }
    }

    fun saveExam(score: Int, total: Int, seconds: Int, breakdown: Map<String, ChapterBreakdown>) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val exam = ExamResult(
                id = UUID.randomUUID().toString(),
                score = score.coerceIn(0, 100),
                total = total.coerceIn(1, 100),
                seconds = seconds.coerceIn(0, 36000),
                breakdown = breakdown,
                createdAt = DateUtils.nowIso()
            )
            _guestState.update { cur ->
                cur.copy(
                    exams = listOf(exam) + cur.exams,
                    profile = cur.profile.withRecordedStudy()
                )
            }
            return
        }
        viewModelScope.launch {
            val res = repo.saveExam(uid, score, total, seconds, breakdown)
            handleMutationResult(res, "تعذّر حفظ نتيجة الامتحان.")
        }
    }

    suspend fun saveNote(id: String?, title: String, body: String, topic: String?): String? {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val noteId = id?.takeIf { it.isNotBlank() } ?: UUID.randomUUID().toString()
            val item = NoteItem(
                id = noteId,
                title = title.trim().ifEmpty { "بدون عنوان" }.take(200),
                body = body.take(20000),
                topic = topic?.trim()?.takeIf { it.isNotEmpty() }?.take(30),
                updatedAt = DateUtils.nowIso()
            )
            _guestState.update { cur ->
                val rest = cur.notes.filterNot { it.id == noteId }
                cur.copy(notes = listOf(item) + rest)
            }
            return noteId
        }
        val res = repo.saveNote(uid, id, title, body, topic)
        handleMutationResult(res, "تعذّر حفظ الملاحظة.")
        return res.getOrNull()
    }

    fun deleteNote(id: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur -> cur.copy(notes = cur.notes.filterNot { it.id == id }) }
            return
        }
        viewModelScope.launch {
            val res = repo.deleteNote(uid, id)
            handleMutationResult(res, "تعذّر حذف الملاحظة.")
        }
    }

    fun addTask(title: String, day: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            val clean = title.trim().take(200)
            if (clean.isEmpty()) return
            val safeDay = day.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr()
            val task = TaskItem(id = UUID.randomUUID().toString(), title = clean, day = safeDay, done = false)
            _guestState.update { cur -> cur.copy(tasks = listOf(task) + cur.tasks) }
            return
        }
        viewModelScope.launch {
            val res = repo.addTask(uid, title, day)
            handleMutationResult(res, "تعذّرت إضافة المهمة.")
        }
    }

    fun toggleTask(id: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur ->
                cur.copy(tasks = cur.tasks.map { if (it.id == id) it.copy(done = !it.done) else it })
            }
            return
        }
        val existingDone = currentAppState.tasks.firstOrNull { it.id == id }?.done ?: false
        viewModelScope.launch {
            val res = repo.toggleTask(uid, id, existingDone)
            handleMutationResult(res, "تعذّر تحديث حالة المهمة.")
        }
    }

    fun deleteTask(id: String) {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.update { cur -> cur.copy(tasks = cur.tasks.filterNot { it.id == id }) }
            return
        }
        viewModelScope.launch {
            val res = repo.deleteTask(uid, id)
            handleMutationResult(res, "تعذّر حذف المهمة.")
        }
    }

    fun resetAll() {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            _guestState.value = AppState(profile = Profile(startDate = DateUtils.todayStr()))
            return
        }
        viewModelScope.launch {
            val res = repo.resetAll(uid)
            handleMutationResult(res, "تعذّرت إعادة تعيين البيانات.")
        }
    }

    fun exportBackupJson(): String = backupJson.encodeToString(currentAppState)

    suspend fun importBackupJson(rawJson: String): Boolean {
        val repo = repository
        val uid = currentUserId
        if (repo == null || uid.isNullOrBlank()) {
            return runCatching {
                val imported = backupJson.decodeFromString<AppState>(rawJson)
                _guestState.value = imported
                true
            }.getOrElse {
                _syncError.value = "تعذّر قراءة ملف النسخة الاحتياطية."
                false
            }
        }
        val res = repo.importJson(uid, rawJson)
        handleMutationResult(res, "تعذّر استيراد النسخة الاحتياطية.")
        return res.isSuccess
    }

    override fun onCleared() {
        timerJob?.cancel()
        super.onCleared()
    }
}

class QaViewModelFactory(
    private val application: Application,
    private val currentUserId: String?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QaViewModel::class.java)) {
            val prefs = application.getSharedPreferences("qa_path_prefs", Context.MODE_PRIVATE)
            val savedTheme = prefs.getString("theme_mode", "auto") ?: "auto"
            val persistTheme: (String) -> Unit = { mode ->
                prefs.edit().putString("theme_mode", mode).apply()
            }
            val repo = if (!currentUserId.isNullOrBlank()) {
                val databaseId = application.getString(R.string.firestore_database_id)
                val db = FirebaseFirestore.getInstance(databaseId)
                QaRepository(db)
            } else {
                null
            }
            val notify: () -> Unit = {
                runCatching {
                    val tg = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 75)
                    tg.startTone(ToneGenerator.TONE_PROP_ACK, 400)
                }
                runCatching {
                    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        val vm = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                        vm?.defaultVibrator
                    } else {
                        @Suppress("DEPRECATION")
                        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                    }
                    vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                }
            }
            @Suppress("UNCHECKED_CAST")
            return QaViewModel(
                repository = repo,
                currentUserId = currentUserId,
                onCycleNotify = notify,
                initialThemeMode = savedTheme,
                onPersistLocalTheme = persistTheme
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
