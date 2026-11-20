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
import com.example.db.QaRepository
import com.example.model.AppState
import com.example.model.ChapterBreakdown
import com.example.model.Profile
import com.example.model.UiState
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
import kotlin.math.roundToInt

enum class TimerMode { FOCUS, BREAK }

data class TimerUiState(
    val mode: TimerMode = TimerMode.FOCUS,
    val leftSeconds: Int = 25 * 60,
    val running: Boolean = false,
    val topicId: String = "",
    val completedToday: Int = 0
)

class QaViewModel(
    private val repository: QaRepository,
    val currentUserId: String,
    private val onCycleNotify: () -> Unit = {}
) : ViewModel() {

    private companion object {
        const val TAG = "QaViewModel"
        const val STOP_TIMEOUT_MILLIS = 5000L
    }

    val uiState: StateFlow<UiState<AppState>> = repository.observeAppState(currentUserId)
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

    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    private val _timerState = MutableStateFlow(TimerUiState())
    val timerState: StateFlow<TimerUiState> = _timerState.asStateFlow()

    private val _mutatingChapterIds = MutableStateFlow<Set<Int>>(emptySet())
    val mutatingChapterIds: StateFlow<Set<Int>> = _mutatingChapterIds.asStateFlow()

    private val _checkingInStreak = MutableStateFlow(false)
    val checkingInStreak: StateFlow<Boolean> = _checkingInStreak.asStateFlow()

    private var timerJob: Job? = null
    private var endAtMs: Long = 0L

    private val currentAppState: AppState
        get() = (uiState.value as? UiState.Success)?.data ?: AppState()

    init {
        viewModelScope.launch {
            repository.ensureUserProfile(currentUserId).onFailure { e ->
                _syncError.value = e.localizedMessage ?: "تعذّر تهيئة الملف الشخصي."
            }
        }
        viewModelScope.launch {
            uiState.collect { st ->
                if (st is UiState.Success) {
                    val profile = st.data.profile
                    _timerState.update { cur ->
                        if (!cur.running) {
                            val targetMin = if (cur.mode == TimerMode.FOCUS) profile.focusMin else profile.breakMin
                            cur.copy(leftSeconds = targetMin * 60)
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
        viewModelScope.launch {
            repository.ensureUserProfile(currentUserId).onFailure { e ->
                _syncError.value = e.localizedMessage ?: "تعذّر الاتصال بالسحابة."
            }
        }
    }

    private fun handleMutationResult(result: Result<*>, fallbackMsg: String) {
        result.onFailure { e ->
            _syncError.value = e.localizedMessage ?: fallbackMsg
        }
    }

    fun setTimerTopic(topicId: String) {
        _timerState.update { it.copy(topicId = topicId) }
    }

    fun startFocusOnTopic(topicId: String) {
        val focusMin = currentAppState.profile.focusMin
        timerJob?.cancel()
        _timerState.update {
            it.copy(
                mode = TimerMode.FOCUS,
                topicId = topicId,
                leftSeconds = focusMin * 60,
                running = false
            )
        }
        startTimer()
    }

    fun startTimer() {
        if (_timerState.value.running) return
        val leftSec = _timerState.value.leftSeconds.coerceAtLeast(1)
        endAtMs = System.currentTimeMillis() + leftSec * 1000L
        _timerState.update { it.copy(running = true) }
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
            val mins = if (cur.mode == TimerMode.FOCUS) profile.focusMin else profile.breakMin
            cur.copy(running = false, leftSeconds = mins * 60)
        }
    }

    fun skipTimer() {
        timerJob?.cancel()
        endAtMs = 0L
        val profile = currentAppState.profile
        _timerState.update { cur ->
            val nextMode = if (cur.mode == TimerMode.FOCUS) TimerMode.BREAK else TimerMode.FOCUS
            val mins = if (nextMode == TimerMode.FOCUS) profile.focusMin else profile.breakMin
            cur.copy(mode = nextMode, running = false, leftSeconds = mins * 60)
        }
    }

    private fun completeTimerCycle() {
        endAtMs = 0L
        onCycleNotify()
        val cur = _timerState.value
        val profile = currentAppState.profile
        if (cur.mode == TimerMode.FOCUS) {
            viewModelScope.launch {
                val res = repository.addSession(
                    userId = currentUserId,
                    minutes = profile.focusMin,
                    kind = "focus",
                    topic = cur.topicId.ifBlank { null }
                )
                handleMutationResult(res, "تعذّر حفظ جلسة التركيز.")
            }
            _timerState.update {
                it.copy(
                    running = false,
                    mode = TimerMode.BREAK,
                    leftSeconds = profile.breakMin * 60,
                    completedToday = it.completedToday + 1
                )
            }
        } else {
            _timerState.update {
                it.copy(
                    running = false,
                    mode = TimerMode.FOCUS,
                    leftSeconds = profile.focusMin * 60
                )
            }
        }
    }

    fun updateProfile(transform: (Profile) -> Profile) {
        viewModelScope.launch {
            val res = repository.updateProfile(currentUserId, currentAppState.profile, transform)
            handleMutationResult(res, "تعذّر تحديث الإعدادات.")
        }
    }

    fun setProgress(key: String, value: String?) {
        viewModelScope.launch {
            val res = repository.setProgress(currentUserId, key, value)
            handleMutationResult(res, "تعذّر حفظ التقدم.")
        }
    }

    fun setChapterCompleted(chapterId: Int, completed: Boolean) {
        if (chapterId in _mutatingChapterIds.value) return
        viewModelScope.launch {
            _mutatingChapterIds.update { it + chapterId }
            try {
                val res = repository.setChapterCompleted(currentUserId, chapterId, completed)
                handleMutationResult(res, "تعذّر تحديث حالة الفصل.")
            } finally {
                _mutatingChapterIds.update { it - chapterId }
            }
        }
    }

    fun checkInDailyStudy() {
        if (_checkingInStreak.value) return
        viewModelScope.launch {
            _checkingInStreak.value = true
            try {
                val res = repository.checkInDailyStudy(currentUserId)
                handleMutationResult(res, "تعذّر تسجيل نشاط اليوم.")
            } finally {
                _checkingInStreak.value = false
            }
        }
    }

    fun addSession(minutes: Int, kind: String, topic: String? = null) {
        viewModelScope.launch {
            val res = repository.addSession(currentUserId, minutes, kind, topic)
            handleMutationResult(res, "تعذّر تسجيل الجلسة.")
        }
    }

    fun deleteSession(id: String) {
        viewModelScope.launch {
            val res = repository.deleteSession(currentUserId, id)
            handleMutationResult(res, "تعذّر حذف الجلسة.")
        }
    }

    fun reviewCard(cardId: String, known: Boolean) {
        viewModelScope.launch {
            val res = repository.reviewCard(currentUserId, cardId, known)
            handleMutationResult(res, "تعذّر حفظ مراجعة البطاقة.")
        }
    }

    fun resetCards() {
        viewModelScope.launch {
            val res = repository.resetCards(currentUserId)
            handleMutationResult(res, "تعذّر تصفير البطاقات.")
        }
    }

    fun recordAttempt(questionId: String, correct: Boolean, mode: String) {
        viewModelScope.launch {
            val res = repository.recordAttempt(currentUserId, questionId, correct, mode)
            handleMutationResult(res, "تعذّر حفظ إجابة السؤال.")
        }
    }

    fun recordAttempts(items: List<Pair<String, Boolean>>, mode: String) {
        viewModelScope.launch {
            val res = repository.recordAttempts(currentUserId, items, mode)
            handleMutationResult(res, "تعذّر حفظ سجل الإجابات.")
        }
    }

    fun clearAttempts() {
        viewModelScope.launch {
            val res = repository.clearAttempts(currentUserId)
            handleMutationResult(res, "تعذّر مسح سجل الأسئلة.")
        }
    }

    fun saveExam(score: Int, total: Int, seconds: Int, breakdown: Map<String, ChapterBreakdown>) {
        viewModelScope.launch {
            val res = repository.saveExam(currentUserId, score, total, seconds, breakdown)
            handleMutationResult(res, "تعذّر حفظ نتيجة الامتحان.")
        }
    }

    suspend fun saveNote(id: String?, title: String, body: String, topic: String?): String? {
        val res = repository.saveNote(currentUserId, id, title, body, topic)
        handleMutationResult(res, "تعذّر حفظ الملاحظة.")
        return res.getOrNull()
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            val res = repository.deleteNote(currentUserId, id)
            handleMutationResult(res, "تعذّر حذف الملاحظة.")
        }
    }

    fun addTask(title: String, day: String) {
        viewModelScope.launch {
            val res = repository.addTask(currentUserId, title, day)
            handleMutationResult(res, "تعذّرت إضافة المهمة.")
        }
    }

    fun toggleTask(id: String) {
        val existingDone = currentAppState.tasks.firstOrNull { it.id == id }?.done ?: false
        viewModelScope.launch {
            val res = repository.toggleTask(currentUserId, id, existingDone)
            handleMutationResult(res, "تعذّر تحديث حالة المهمة.")
        }
    }

    fun deleteTask(id: String) {
        viewModelScope.launch {
            val res = repository.deleteTask(currentUserId, id)
            handleMutationResult(res, "تعذّر حذف المهمة.")
        }
    }

    fun resetAll() {
        viewModelScope.launch {
            val res = repository.resetAll(currentUserId)
            handleMutationResult(res, "تعذّرت إعادة تعيين البيانات.")
        }
    }

    fun exportBackupJson(): String = repository.exportJson(currentAppState)

    suspend fun importBackupJson(rawJson: String): Boolean {
        val res = repository.importJson(currentUserId, rawJson)
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
    private val currentUserId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QaViewModel::class.java)) {
            val databaseId = application.getString(R.string.firestore_database_id)
            val db = FirebaseFirestore.getInstance(databaseId)
            val repo = QaRepository(db)
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
            return QaViewModel(repo, currentUserId, notify) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
