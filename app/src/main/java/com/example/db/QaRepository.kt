package com.example.db

import com.example.data.CardsData
import com.example.data.SyllabusData
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
import com.example.util.DateUtils
import com.example.util.StatsUtils
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class QaRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    private val json = kotlinx.serialization.json.Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    companion object {
        val BOX_DAYS = com.example.util.LeitnerScheduler.BOX_DAYS
    }

    private fun requireUserId(): String =
        auth.currentUser?.uid?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")

    private fun userDoc(uid: String): DocumentReference =
        db.collection("users").document(uid)

    private fun sanitizeDocId(raw: String): String =
        raw.replace(Regex("[^a-zA-Z0-9_\\-]"), "_").ifBlank { "item" }.take(128)

    private fun <T> Flow<T>.logFirestoreErrors(
        expectedUid: String,
        operationType: OperationType,
        path: String
    ): Flow<T> = catch { error ->
        val firestoreError = generateSequence(error) { it.cause }
            .filterIsInstance<FirebaseFirestoreException>()
            .firstOrNull()
        if (firestoreError == null && error is CancellationException) throw error
        val exceptionToLog = firestoreError ?: (error as? Exception)
        if (exceptionToLog != null && auth.currentUser?.uid == expectedUid) {
            handleFirestoreError(exceptionToLog, operationType, path)
        }
        throw error
    }

    fun observeAppState(userId: String): Flow<AppState> = flow {
        val activeUid = requireUserId()
        check(activeUid == userId) { "Signed-in user changed; refusing cross-user observe." }
        val base = userDoc(userId)

        val profileFlow: Flow<Profile> = base.snapshots()
            .map { snap -> parseProfile(snap) }
            .logFirestoreErrors(userId, OperationType.GET, "users/$userId")

        val progressFlow: Flow<Map<String, String>> = base.collection("progress").snapshots()
            .map { qs ->
                buildMap {
                    for (doc in qs.documents) {
                        val k = doc.getString("key") ?: continue
                        val v = doc.getString("value") ?: continue
                        put(k, v)
                    }
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/progress")

        val sessionsFlow: Flow<List<StudySession>> = base.collection("sessions")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val day = data["day"] as? String ?: return@mapNotNull null
                    val minutes = (data["minutes"] as? Number)?.toInt() ?: return@mapNotNull null
                    val kind = data["kind"] as? String ?: "focus"
                    val topic = data["topic"] as? String
                    StudySession(id = doc.id, day = day, minutes = minutes, kind = kind, topic = topic)
                }.sortedByDescending { it.day }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/sessions")

        val cardsFlow: Flow<Map<String, CardState>> = base.collection("cards").snapshots()
            .map { qs ->
                buildMap {
                    for (doc in qs.documents) {
                        val data = doc.data ?: continue
                        val box = (data["box"] as? Number)?.toInt() ?: 1
                        val due = data["due"] as? String ?: continue
                        val reps = (data["reps"] as? Number)?.toInt() ?: 0
                        val lapses = (data["lapses"] as? Number)?.toInt() ?: 0
                        put(doc.id, CardState(box = box, due = due, reps = reps, lapses = lapses))
                    }
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/cards")

        val attemptsFlow: Flow<List<QuizAttempt>> = base.collection("attempts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val qId = data["questionId"] as? String ?: return@mapNotNull null
                    val correct = data["correct"] as? Boolean ?: false
                    val mode = data["mode"] as? String ?: "practice"
                    val at = data["at"] as? String ?: ""
                    QuizAttempt(questionId = qId, correct = correct, mode = mode, at = at)
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/attempts")

        val examsFlow: Flow<List<ExamResult>> = base.collection("exams")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val score = (data["score"] as? Number)?.toInt() ?: 0
                    val total = (data["total"] as? Number)?.toInt() ?: 40
                    val seconds = (data["seconds"] as? Number)?.toInt() ?: 0
                    val at = data["at"] as? String ?: ""
                    val rawBreakdown = data["breakdown"] as? Map<*, *> ?: emptyMap<Any, Any>()
                    val breakdown = buildMap {
                        for ((k, v) in rawBreakdown) {
                            val keyStr = k as? String ?: continue
                            val entryMap = v as? Map<*, *> ?: continue
                            val c = (entryMap["c"] as? Number)?.toInt() ?: 0
                            val t = (entryMap["t"] as? Number)?.toInt() ?: 0
                            put(keyStr, ChapterBreakdown(c = c, t = t))
                        }
                    }
                    ExamResult(
                        id = doc.id,
                        score = score,
                        total = total,
                        seconds = seconds,
                        breakdown = breakdown,
                        createdAt = at
                    )
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/exams")

        val notesFlow: Flow<List<NoteItem>> = base.collection("notes")
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val title = data["title"] as? String ?: "بدون عنوان"
                    val body = data["body"] as? String ?: ""
                    val topic = data["topic"] as? String
                    val updatedIso = data["updatedAtIso"] as? String ?: DateUtils.nowIso()
                    NoteItem(id = doc.id, title = title, body = body, topic = topic, updatedAt = updatedIso)
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/notes")

        val tasksFlow: Flow<List<TaskItem>> = base.collection("tasks")
            .orderBy("day", Query.Direction.ASCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val title = data["title"] as? String ?: return@mapNotNull null
                    val day = data["day"] as? String ?: return@mapNotNull null
                    val done = data["done"] as? Boolean ?: false
                    TaskItem(id = doc.id, title = title, day = day, done = done)
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/tasks")

        val flashcardsFlow: Flow<List<IstqbFlashcard>> = base.collection("flashcards")
            .orderBy("chapter", Query.Direction.ASCENDING)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val term = data["term"] as? String ?: return@mapNotNull null
                    val question = data["question"] as? String ?: return@mapNotNull null
                    val answer = data["answer"] as? String ?: return@mapNotNull null
                    val chapter = (data["chapter"] as? Number)?.toInt()?.coerceIn(1, 6) ?: 1
                    val hint = data["hint"] as? String
                    val source = (data["source"] as? String)?.takeIf { it in listOf("istqb", "custom") } ?: "istqb"
                    IstqbFlashcard(
                        id = doc.id,
                        term = term,
                        question = question,
                        answer = answer,
                        chapter = chapter,
                        hint = hint,
                        source = source
                    )
                }
            }
            .logFirestoreErrors(userId, OperationType.LIST, "users/$userId/flashcards")

        emitAll(
            combine(
                listOf(
                    profileFlow,
                    progressFlow,
                    sessionsFlow,
                    cardsFlow,
                    attemptsFlow,
                    examsFlow,
                    notesFlow,
                    tasksFlow,
                    flashcardsFlow
                )
            ) { arr ->
                @Suppress("UNCHECKED_CAST")
                AppState(
                    profile = arr[0] as Profile,
                    progress = arr[1] as Map<String, String>,
                    sessions = arr[2] as List<StudySession>,
                    cards = arr[3] as Map<String, CardState>,
                    flashcards = arr[8] as List<IstqbFlashcard>,
                    attempts = arr[4] as List<QuizAttempt>,
                    exams = arr[5] as List<ExamResult>,
                    notes = arr[6] as List<NoteItem>,
                    tasks = arr[7] as List<TaskItem>
                )
            }
        )
    }

    private fun parseProfile(snap: DocumentSnapshot): Profile {
        val data = snap.data ?: return Profile(startDate = DateUtils.todayStr())
        val streakVal = (data["currentStreak"] as? Number)?.toInt()?.coerceIn(0, 36500) ?: 0
        val bestVal = (data["bestStreak"] as? Number)?.toInt()?.coerceIn(0, 36500) ?: 0
        val lastStudy = (data["lastStudyDate"] as? String)?.takeIf { DateUtils.isValidDay(it) }
        val totalFocusMin = (data["totalFocusMinutes"] as? Number)?.toInt()?.coerceIn(0, 1_000_000) ?: 0
        val focusPts = (data["focusPoints"] as? Number)?.toInt()?.coerceIn(0, 10_000_000) ?: 0
        return Profile(
            examDate = (data["examDate"] as? String)?.takeIf { DateUtils.isValidDay(it) },
            startDate = (data["startDate"] as? String)?.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr(),
            dailyHours = (data["dailyHours"] as? Number)?.toDouble()?.coerceIn(0.5, 12.0) ?: 2.0,
            startTime = (data["startTime"] as? String)?.takeIf { DateUtils.isValidTime(it) } ?: "18:00",
            theme = (data["theme"] as? String)?.takeIf { it in listOf("auto", "light", "dark") } ?: "auto",
            focusMin = (data["focusMin"] as? Number)?.toInt()?.coerceIn(5, 120) ?: 25,
            breakMin = (data["breakMin"] as? Number)?.toInt()?.coerceIn(1, 60) ?: 5,
            currentStreak = streakVal,
            bestStreak = maxOf(bestVal, streakVal),
            lastStudyDate = lastStudy,
            totalFocusMinutes = totalFocusMin,
            focusPoints = focusPts
        )
    }

    suspend fun ensureUserProfile(userId: String): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val ref = userDoc(uid)
            val existing = ref.get().await()
            if (!existing.exists()) {
                val payload = mapOf(
                    "userId" to uid,
                    "startDate" to DateUtils.todayStr(),
                    "dailyHours" to 2.0,
                    "startTime" to "18:00",
                    "theme" to "auto",
                    "focusMin" to 25,
                    "breakMin" to 5,
                    "currentStreak" to 0,
                    "bestStreak" to 0,
                    "totalFocusMinutes" to 0,
                    "focusPoints" to 0,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                ref.set(payload).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun updateProfile(userId: String, currentProfile: Profile, transform: (Profile) -> Profile): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val updated = transform(currentProfile)
            val safeExamDate = updated.examDate?.takeIf { DateUtils.isValidDay(it) }
            val safeStartDate = updated.startDate.takeIf { DateUtils.isValidDay(it) }
                ?: currentProfile.startDate.takeIf { DateUtils.isValidDay(it) }
                ?: DateUtils.todayStr()
            val safeDailyHours = updated.dailyHours.coerceIn(0.5, 12.0)
            val safeStartTime = updated.startTime.takeIf { DateUtils.isValidTime(it) } ?: "18:00"
            val safeTheme = if (updated.theme in listOf("auto", "light", "dark")) updated.theme else "auto"
            val safeFocus = updated.focusMin.coerceIn(5, 120)
            val safeBreak = updated.breakMin.coerceIn(1, 60)

            val ref = userDoc(uid)
            val snap = ref.get().await()
            if (!snap.exists()) {
                val createMap = buildMap<String, Any> {
                    put("userId", uid)
                    if (safeExamDate != null) put("examDate", safeExamDate)
                    put("startDate", safeStartDate)
                    put("dailyHours", safeDailyHours)
                    put("startTime", safeStartTime)
                    put("theme", safeTheme)
                    put("focusMin", safeFocus)
                    put("breakMin", safeBreak)
                    put("currentStreak", updated.currentStreak.coerceIn(0, 36500))
                    put("bestStreak", updated.bestStreak.coerceIn(0, 36500))
                    updated.lastStudyDate?.takeIf { DateUtils.isValidDay(it) }?.let { put("lastStudyDate", it) }
                    put("totalFocusMinutes", updated.totalFocusMinutes.coerceIn(0, 1_000_000))
                    put("focusPoints", updated.focusPoints.coerceIn(0, 10_000_000))
                    put("createdAt", FieldValue.serverTimestamp())
                    put("updatedAt", FieldValue.serverTimestamp())
                }
                ref.set(createMap).await()
            } else {
                val updateMap = mapOf<String, Any>(
                    "examDate" to (safeExamDate ?: FieldValue.delete()),
                    "startDate" to safeStartDate,
                    "dailyHours" to safeDailyHours,
                    "startTime" to safeStartTime,
                    "theme" to safeTheme,
                    "focusMin" to safeFocus,
                    "breakMin" to safeBreak,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                ref.update(updateMap).await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun setProgress(userId: String, key: String, value: String?): Result<Unit> {
        val cleanKey = key.trim().take(120)
        if (cleanKey.isEmpty()) return Result.success(Unit)
        val docId = sanitizeDocId(cleanKey)
        val path = "users/$userId/progress/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val ref = userDoc(uid).collection("progress").document(docId)
            if (value.isNullOrEmpty()) {
                ref.delete().await()
            } else {
                val cleanValue = value.take(200)
                val snap = ref.get().await()
                if (!snap.exists()) {
                    ref.set(
                        mapOf(
                            "key" to cleanKey,
                            "value" to cleanValue,
                            "createdAt" to FieldValue.serverTimestamp(),
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    ).await()
                } else {
                    ref.update(
                        mapOf(
                            "value" to cleanValue,
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    ).await()
                }
                recordDailyStudyBestEffort(uid)
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun setChapterCompleted(userId: String, chapterId: Int, completed: Boolean): Result<Unit> {
        val chapter = SyllabusData.findChapter(chapterId) ?: return Result.success(Unit)
        val basePath = "users/$userId/progress"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val progressCol = userDoc(uid).collection("progress")
            val refs = chapter.topics.map { topic ->
                val cleanKey = "t:${topic.id}"
                val docId = sanitizeDocId(cleanKey)
                cleanKey to progressCol.document(docId)
            }
            val existingSnaps = if (completed) {
                refs.map { (_, ref) -> ref.get().await().exists() }
            } else {
                emptyList()
            }
            val batch = db.batch()
            refs.forEachIndexed { index, (cleanKey, ref) ->
                if (completed) {
                    if (existingSnaps[index]) {
                        batch.update(
                            ref,
                            mapOf(
                                "value" to "done",
                                "updatedAt" to FieldValue.serverTimestamp()
                            )
                        )
                    } else {
                        batch.set(
                            ref,
                            mapOf(
                                "key" to cleanKey,
                                "value" to "done",
                                "createdAt" to FieldValue.serverTimestamp(),
                                "updatedAt" to FieldValue.serverTimestamp()
                            )
                        )
                    }
                } else {
                    batch.delete(ref)
                }
            }
            batch.commit().await()
            if (completed) {
                recordDailyStudyBestEffort(uid)
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, basePath)
            Result.failure(e)
        }
    }

    suspend fun checkInDailyStudy(userId: String, studyDay: String = DateUtils.todayStr()): Result<Unit> {
        val path = "users/$userId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            recordDailyStudyTransaction(uid, studyDay)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    private suspend fun recordDailyStudyBestEffort(uid: String, studyDay: String = DateUtils.todayStr()) {
        try {
            recordDailyStudyTransaction(uid, studyDay)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, "users/$uid")
        }
    }

    private suspend fun recordDailyStudyTransaction(uid: String, studyDay: String) {
        val safeDay = studyDay.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr()
        val ref = userDoc(uid)
        db.runTransaction { tx ->
            val snap = tx.get(ref)
            if (!snap.exists()) {
                tx.set(
                    ref,
                    mapOf(
                        "userId" to uid,
                        "startDate" to safeDay,
                        "dailyHours" to 2.0,
                        "startTime" to "18:00",
                        "theme" to "auto",
                        "focusMin" to 25,
                        "breakMin" to 5,
                        "currentStreak" to 1,
                        "bestStreak" to 1,
                        "lastStudyDate" to safeDay,
                        "totalFocusMinutes" to 0,
                        "focusPoints" to 0,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
            } else {
                val prevStreak = (snap.getLong("currentStreak") ?: 0L).toInt().coerceIn(0, 36500)
                val prevBest = (snap.getLong("bestStreak") ?: 0L).toInt().coerceIn(0, 36500)
                val prevLast = snap.getString("lastStudyDate")?.takeIf { DateUtils.isValidDay(it) }

                if (prevLast == safeDay && prevStreak >= 1) {
                    return@runTransaction null
                }

                val (nextStreak, nextLast) = if (prevLast != null) {
                    val diff = DateUtils.daysBetween(prevLast, safeDay)
                    when {
                        diff < 0 -> return@runTransaction null
                        diff == 0 -> maxOf(1, prevStreak) to safeDay
                        diff == 1 -> (prevStreak + 1).coerceAtMost(36500) to safeDay
                        else -> 1 to safeDay
                    }
                } else {
                    1 to safeDay
                }
                val nextBest = maxOf(prevBest, nextStreak).coerceAtMost(36500)
                tx.update(
                    ref,
                    mapOf(
                        "currentStreak" to nextStreak,
                        "bestStreak" to nextBest,
                        "lastStudyDate" to nextLast,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                )
            }
            null
        }.await()
    }

    suspend fun addSession(
        userId: String,
        minutes: Int,
        kind: String,
        topic: String? = null,
        day: String = DateUtils.todayStr()
    ): Result<Unit> {
        val sessionRef = userDoc(userId).collection("sessions").document()
        val path = sessionRef.path
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val safeMinutes = minutes.coerceIn(1, 600)
            val safeDay = day.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr()
            val safeKind = kind.trim().ifEmpty { "focus" }.take(30)
            val safeTopic = topic?.trim()?.takeIf { it.isNotEmpty() }?.take(30)
            val earnedPoints = StatsUtils.pointsForSession(safeMinutes, safeKind)
            val sessionPayload = buildMap<String, Any> {
                put("day", safeDay)
                put("minutes", safeMinutes)
                put("kind", safeKind)
                if (safeTopic != null) put("topic", safeTopic)
                put("createdAt", FieldValue.serverTimestamp())
            }
            val uRef = userDoc(uid)
            db.runTransaction { tx ->
                val userSnap = tx.get(uRef)
                tx.set(sessionRef, sessionPayload)
                if (!userSnap.exists()) {
                    tx.set(
                        uRef,
                        mapOf(
                            "userId" to uid,
                            "startDate" to safeDay,
                            "dailyHours" to 2.0,
                            "startTime" to "18:00",
                            "theme" to "auto",
                            "focusMin" to 25,
                            "breakMin" to 5,
                            "currentStreak" to 1,
                            "bestStreak" to 1,
                            "lastStudyDate" to safeDay,
                            "totalFocusMinutes" to safeMinutes.coerceAtMost(1_000_000),
                            "focusPoints" to earnedPoints.coerceAtMost(10_000_000),
                            "createdAt" to FieldValue.serverTimestamp(),
                            "updatedAt" to FieldValue.serverTimestamp()
                        )
                    )
                } else {
                    val prevTotalFocus = (userSnap.getLong("totalFocusMinutes") ?: 0L).toInt().coerceIn(0, 1_000_000)
                    val prevFocusPoints = (userSnap.getLong("focusPoints") ?: 0L).toInt().coerceIn(0, 10_000_000)
                    val prevStreak = (userSnap.getLong("currentStreak") ?: 0L).toInt().coerceIn(0, 36500)
                    val prevBest = (userSnap.getLong("bestStreak") ?: 0L).toInt().coerceIn(0, 36500)
                    val prevLast = userSnap.getString("lastStudyDate")?.takeIf { DateUtils.isValidDay(it) }

                    val nextTotalFocus = (prevTotalFocus + safeMinutes).coerceIn(0, 1_000_000)
                    val nextFocusPoints = (prevFocusPoints + earnedPoints).coerceIn(0, 10_000_000)

                    val updateMap = mutableMapOf<String, Any>(
                        "totalFocusMinutes" to nextTotalFocus,
                        "focusPoints" to nextFocusPoints,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )

                    if (prevLast == null) {
                        updateMap["currentStreak"] = 1
                        updateMap["bestStreak"] = maxOf(prevBest, 1).coerceAtMost(36500)
                        updateMap["lastStudyDate"] = safeDay
                    } else {
                        val diff = DateUtils.daysBetween(prevLast, safeDay)
                        if (diff == 0 && prevStreak < 1) {
                            updateMap["currentStreak"] = 1
                            updateMap["bestStreak"] = maxOf(prevBest, 1).coerceAtMost(36500)
                        } else if (diff == 1) {
                            val nextStreak = (prevStreak + 1).coerceAtMost(36500)
                            updateMap["currentStreak"] = nextStreak
                            updateMap["bestStreak"] = maxOf(prevBest, nextStreak).coerceAtMost(36500)
                            updateMap["lastStudyDate"] = safeDay
                        } else if (diff > 1) {
                            updateMap["currentStreak"] = 1
                            updateMap["bestStreak"] = maxOf(prevBest, 1).coerceAtMost(36500)
                            updateMap["lastStudyDate"] = safeDay
                        }
                    }

                    tx.update(uRef, updateMap)
                }
                null
            }.await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteSession(userId: String, id: String): Result<Unit> {
        if (id.isBlank()) return Result.success(Unit)
        val docId = sanitizeDocId(id)
        val path = "users/$userId/sessions/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user delete." }
            userDoc(uid).collection("sessions").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun reviewCard(userId: String, cardId: String, known: Boolean): Result<Unit> {
        val docId = sanitizeDocId(cardId)
        val path = "users/$userId/cards/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val today = DateUtils.todayStr()
            val ref = userDoc(uid).collection("cards").document(docId)
            val snap = ref.get().await()
            val prev = if (snap.exists()) {
                CardState(
                    box = (snap.getLong("box") ?: 1L).toInt(),
                    due = snap.getString("due") ?: today,
                    reps = (snap.getLong("reps") ?: 0L).toInt(),
                    lapses = (snap.getLong("lapses") ?: 0L).toInt()
                )
            } else null
            val next = com.example.util.LeitnerScheduler.nextCardState(prev, known, today)

            if (!snap.exists()) {
                ref.set(
                    mapOf(
                        "box" to next.box,
                        "due" to next.due,
                        "reps" to next.reps,
                        "lapses" to next.lapses,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                ref.update(
                    mapOf(
                        "box" to next.box,
                        "due" to next.due,
                        "reps" to next.reps,
                        "lapses" to next.lapses,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
            recordDailyStudyBestEffort(uid, today)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun resetCards(userId: String): Result<Unit> =
        clearSubcollection(userId, "cards")

    suspend fun ensureFlashcardsSeeded(userId: String): Result<Int> {
        val path = "users/$userId/flashcards"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val col = userDoc(uid).collection("flashcards")
            val existing = col.limit(1).get().await()
            if (!existing.isEmpty) {
                return Result.success(existing.size())
            }
            val defaults = CardsData.toDefaultFlashcards()
            for (chunk in defaults.chunked(450)) {
                val batch = db.batch()
                for (card in chunk) {
                    val docId = sanitizeDocId(card.id)
                    val payload = buildMap<String, Any> {
                        put("term", card.term.trim().take(120))
                        put("question", card.question.trim().take(400))
                        put("answer", card.answer.trim().take(600))
                        put("chapter", card.chapter.coerceIn(1, 6))
                        card.hint?.trim()?.takeIf { it.isNotEmpty() }?.take(200)?.let { put("hint", it) }
                        put("source", "istqb")
                        put("createdAt", FieldValue.serverTimestamp())
                        put("updatedAt", FieldValue.serverTimestamp())
                    }
                    batch.set(col.document(docId), payload)
                }
                batch.commit().await()
            }
            Result.success(defaults.size)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun refreshFlashcards(userId: String): Result<Int> {
        val path = "users/$userId/flashcards"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user read." }
            ensureFlashcardsSeeded(uid).getOrThrow()
            val qs = userDoc(uid).collection("flashcards")
                .orderBy("chapter", Query.Direction.ASCENDING)
                .get()
                .await()
            Result.success(qs.size())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.LIST, path)
            Result.failure(e)
        }
    }

    suspend fun saveFlashcard(
        userId: String,
        term: String,
        question: String,
        answer: String,
        chapter: Int,
        hint: String? = null
    ): Result<Unit> {
        val cleanTerm = term.trim().take(120)
        val cleanQuestion = question.trim().ifEmpty { "ما المقصود بمصطلح $cleanTerm؟" }.take(400)
        val cleanAnswer = answer.trim().take(600)
        if (cleanTerm.isEmpty() || cleanAnswer.isEmpty()) return Result.success(Unit)
        val docId = sanitizeDocId("custom-${java.util.UUID.randomUUID().toString().take(12)}")
        val path = "users/$userId/flashcards/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val safeHint = hint?.trim()?.takeIf { it.isNotEmpty() }?.take(200)
            val payload = buildMap<String, Any> {
                put("term", cleanTerm)
                put("question", cleanQuestion)
                put("answer", cleanAnswer)
                put("chapter", chapter.coerceIn(1, 6))
                if (safeHint != null) put("hint", safeHint)
                put("source", "custom")
                put("createdAt", FieldValue.serverTimestamp())
                put("updatedAt", FieldValue.serverTimestamp())
            }
            userDoc(uid).collection("flashcards").document(docId).set(payload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteFlashcard(userId: String, id: String): Result<Unit> {
        if (id.isBlank()) return Result.success(Unit)
        val docId = sanitizeDocId(id)
        val path = "users/$userId/flashcards/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user delete." }
            userDoc(uid).collection("flashcards").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun recordAttempt(
        userId: String,
        questionId: String,
        correct: Boolean,
        mode: String
    ): Result<Unit> = recordAttempts(userId, listOf(questionId to correct), mode)

    suspend fun recordAttempts(
        userId: String,
        items: List<Pair<String, Boolean>>,
        mode: String
    ): Result<Unit> {
        if (items.isEmpty()) return Result.success(Unit)
        val path = "users/$userId/attempts"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val safeMode = mode.trim().ifEmpty { "practice" }.take(20)
            val now = DateUtils.nowIso()
            val col = userDoc(uid).collection("attempts")
            for (chunk in items.chunked(450)) {
                val batch = db.batch()
                for ((qId, ok) in chunk) {
                    val cleanQ = qId.trim().take(30)
                    if (cleanQ.isEmpty()) continue
                    val ref = col.document()
                    batch.set(
                        ref,
                        mapOf(
                            "questionId" to cleanQ,
                            "correct" to ok,
                            "mode" to safeMode,
                            "at" to now,
                            "createdAt" to FieldValue.serverTimestamp()
                        )
                    )
                }
                batch.commit().await()
            }
            recordDailyStudyBestEffort(uid)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun clearAttempts(userId: String): Result<Unit> =
        clearSubcollection(userId, "attempts")

    suspend fun saveExam(
        userId: String,
        score: Int,
        total: Int,
        seconds: Int,
        breakdown: Map<String, ChapterBreakdown>
    ): Result<Unit> {
        val ref = userDoc(userId).collection("exams").document()
        val path = ref.path
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val breakdownMap = breakdown.entries.take(10).associate { (k, v) ->
                k.take(10) to mapOf("c" to v.c.coerceAtLeast(0), "t" to v.t.coerceAtLeast(0))
            }
            ref.set(
                mapOf(
                    "score" to score.coerceIn(0, 100),
                    "total" to total.coerceIn(1, 100),
                    "seconds" to seconds.coerceIn(0, 36000),
                    "breakdown" to breakdownMap,
                    "at" to DateUtils.nowIso(),
                    "createdAt" to FieldValue.serverTimestamp()
                )
            ).await()
            recordDailyStudyBestEffort(uid)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun saveNote(
        userId: String,
        id: String?,
        title: String,
        body: String,
        topic: String?
    ): Result<String> {
        val cleanTitle = title.trim().ifEmpty { "بدون عنوان" }.take(200)
        val cleanBody = body.take(20000)
        val cleanTopic = topic?.trim()?.takeIf { it.isNotEmpty() }?.take(30)
        val nowIso = DateUtils.nowIso()
        val col = userDoc(userId).collection("notes")
        val targetRef = if (!id.isNullOrBlank()) col.document(sanitizeDocId(id)) else col.document()
        val path = targetRef.path
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            val existingSnap = if (!id.isNullOrBlank()) targetRef.get().await() else null
            if (existingSnap != null && existingSnap.exists()) {
                val updateMap = mapOf<String, Any>(
                    "title" to cleanTitle,
                    "body" to cleanBody,
                    "topic" to (cleanTopic ?: FieldValue.delete()),
                    "updatedAtIso" to nowIso,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                targetRef.update(updateMap).await()
            } else {
                val createMap = buildMap<String, Any> {
                    put("title", cleanTitle)
                    put("body", cleanBody)
                    if (cleanTopic != null) put("topic", cleanTopic)
                    put("updatedAtIso", nowIso)
                    put("createdAt", FieldValue.serverTimestamp())
                    put("updatedAt", FieldValue.serverTimestamp())
                }
                targetRef.set(createMap).await()
            }
            Result.success(targetRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteNote(userId: String, id: String): Result<Unit> {
        if (id.isBlank()) return Result.success(Unit)
        val docId = sanitizeDocId(id)
        val path = "users/$userId/notes/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user delete." }
            userDoc(uid).collection("notes").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun addTask(userId: String, title: String, day: String): Result<Unit> {
        val clean = title.trim().take(200)
        if (clean.isEmpty()) return Result.success(Unit)
        val safeDay = day.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr()
        val ref = userDoc(userId).collection("tasks").document()
        val path = ref.path
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user write." }
            ref.set(
                mapOf(
                    "title" to clean,
                    "day" to safeDay,
                    "done" to false,
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
            Result.failure(e)
        }
    }

    suspend fun toggleTask(userId: String, id: String, currentDone: Boolean): Result<Unit> {
        if (id.isBlank()) return Result.success(Unit)
        val docId = sanitizeDocId(id)
        val path = "users/$userId/tasks/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user update." }
            userDoc(uid).collection("tasks").document(docId).update(
                mapOf(
                    "done" to !currentDone,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
            Result.failure(e)
        }
    }

    suspend fun deleteTask(userId: String, id: String): Result<Unit> {
        if (id.isBlank()) return Result.success(Unit)
        val docId = sanitizeDocId(id)
        val path = "users/$userId/tasks/$docId"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user delete." }
            userDoc(uid).collection("tasks").document(docId).delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    suspend fun resetAll(userId: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user reset." }
            val subcollections = listOf("progress", "sessions", "cards", "attempts", "exams", "notes", "tasks")
            for (sub in subcollections) {
                clearSubcollection(uid, sub).getOrThrow()
            }
            val profileRef = userDoc(uid)
            val snap = profileRef.get().await()
            val today = DateUtils.todayStr()
            if (snap.exists()) {
                profileRef.update(
                    mapOf(
                        "examDate" to FieldValue.delete(),
                        "startDate" to today,
                        "currentStreak" to 0,
                        "bestStreak" to 0,
                        "lastStudyDate" to FieldValue.delete(),
                        "totalFocusMinutes" to 0,
                        "focusPoints" to 0,
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                ensureUserProfile(uid).getOrThrow()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, "users/$userId")
            Result.failure(e)
        }
    }

    private suspend fun clearSubcollection(userId: String, collectionName: String): Result<Unit> {
        val path = "users/$userId/$collectionName"
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user clear." }
            val docs = userDoc(uid).collection(collectionName).get().await().documents
            for (chunk in docs.chunked(450)) {
                val batch = db.batch()
                chunk.forEach { batch.delete(it.reference) }
                batch.commit().await()
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, path)
            Result.failure(e)
        }
    }

    fun exportJson(state: AppState): String = json.encodeToString(state)

    suspend fun importJson(userId: String, rawJson: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            check(uid == userId) { "Signed-in user changed; refusing cross-user import." }
            val imported = json.decodeFromString<AppState>(rawJson)
            resetAll(uid).getOrThrow()

            val p = imported.profile
            val importedMinutes = maxOf(
                p.totalFocusMinutes.coerceAtLeast(0),
                imported.sessions.sumOf { it.minutes.coerceIn(1, 600) }
            ).coerceIn(0, 1_000_000)
            val importedPoints = maxOf(
                p.focusPoints.coerceAtLeast(0),
                imported.sessions.sumOf { StatsUtils.pointsForSession(it.minutes.coerceIn(1, 600), it.kind) }
            ).coerceIn(0, 10_000_000)
            updateProfile(uid, Profile(startDate = DateUtils.todayStr())) {
                Profile(
                    examDate = p.examDate?.takeIf { DateUtils.isValidDay(it) },
                    startDate = p.startDate.takeIf { DateUtils.isValidDay(it) } ?: DateUtils.todayStr(),
                    dailyHours = p.dailyHours.coerceIn(0.5, 12.0),
                    startTime = p.startTime.takeIf { DateUtils.isValidTime(it) } ?: "18:00",
                    theme = if (p.theme in listOf("auto", "light", "dark")) p.theme else "auto",
                    focusMin = p.focusMin.coerceIn(5, 120),
                    breakMin = p.breakMin.coerceIn(1, 60),
                    currentStreak = p.currentStreak.coerceIn(0, 36500),
                    bestStreak = maxOf(p.bestStreak, p.currentStreak).coerceIn(0, 36500),
                    lastStudyDate = p.lastStudyDate?.takeIf { DateUtils.isValidDay(it) },
                    totalFocusMinutes = importedMinutes,
                    focusPoints = importedPoints
                )
            }.getOrThrow()
            userDoc(uid).update(
                mapOf(
                    "totalFocusMinutes" to importedMinutes,
                    "focusPoints" to importedPoints,
                    "updatedAt" to FieldValue.serverTimestamp()
                )
            ).await()

            val userRef = userDoc(uid)
            val ops = mutableListOf<Pair<DocumentReference, Map<String, Any>>>()

            imported.progress.entries.take(500).forEach { (k, v) ->
                val ck = k.trim().take(120)
                val cv = v.trim().take(200)
                if (ck.isNotEmpty() && cv.isNotEmpty()) {
                    ops += userRef.collection("progress").document(sanitizeDocId(ck)) to mapOf(
                        "key" to ck,
                        "value" to cv,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                }
            }
            imported.sessions.take(1000).forEach { s ->
                if (DateUtils.isValidDay(s.day)) {
                    val map = buildMap<String, Any> {
                        put("day", s.day)
                        put("minutes", s.minutes.coerceIn(1, 600))
                        put("kind", s.kind.ifBlank { "focus" }.take(30))
                        s.topic?.takeIf { it.isNotBlank() }?.take(30)?.let { put("topic", it) }
                        put("createdAt", FieldValue.serverTimestamp())
                    }
                    ops += userRef.collection("sessions").document() to map
                }
            }
            imported.cards.entries.take(500).forEach { (id, c) ->
                if (id.isNotBlank() && DateUtils.isValidDay(c.due)) {
                    ops += userRef.collection("cards").document(sanitizeDocId(id)) to mapOf(
                        "box" to c.box.coerceIn(1, 5),
                        "due" to c.due,
                        "reps" to c.reps.coerceIn(0, 100000),
                        "lapses" to c.lapses.coerceIn(0, 100000),
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                }
            }
            imported.attempts.take(2000).forEach { a ->
                val qId = a.questionId.trim().take(30)
                if (qId.isNotEmpty()) {
                    ops += userRef.collection("attempts").document() to mapOf(
                        "questionId" to qId,
                        "correct" to a.correct,
                        "mode" to a.mode.ifBlank { "practice" }.take(20),
                        "at" to a.at.ifBlank { DateUtils.nowIso() }.take(40),
                        "createdAt" to FieldValue.serverTimestamp()
                    )
                }
            }
            imported.exams.take(300).forEach { e ->
                val breakdownMap = e.breakdown.entries.take(10).associate { (k, v) ->
                    k.take(10) to mapOf("c" to v.c.coerceAtLeast(0), "t" to v.t.coerceAtLeast(0))
                }
                ops += userRef.collection("exams").document() to mapOf(
                    "score" to e.score.coerceIn(0, 100),
                    "total" to e.total.coerceIn(1, 100),
                    "seconds" to e.seconds.coerceIn(0, 36000),
                    "breakdown" to breakdownMap,
                    "at" to e.createdAt.ifBlank { DateUtils.nowIso() }.take(40),
                    "createdAt" to FieldValue.serverTimestamp()
                )
            }
            imported.notes.take(500).forEach { n ->
                val map = buildMap<String, Any> {
                    put("title", n.title.trim().ifEmpty { "بدون عنوان" }.take(200))
                    put("body", n.body.take(20000))
                    n.topic?.takeIf { it.isNotBlank() }?.take(30)?.let { put("topic", it) }
                    put("updatedAtIso", n.updatedAt.ifBlank { DateUtils.nowIso() }.take(40))
                    put("createdAt", FieldValue.serverTimestamp())
                    put("updatedAt", FieldValue.serverTimestamp())
                }
                ops += userRef.collection("notes").document() to map
            }
            imported.tasks.take(500).forEach { t ->
                val title = t.title.trim().take(200)
                if (title.isNotEmpty() && DateUtils.isValidDay(t.day)) {
                    ops += userRef.collection("tasks").document() to mapOf(
                        "title" to title,
                        "day" to t.day,
                        "done" to t.done,
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                }
            }

            for (chunk in ops.chunked(450)) {
                val batch = db.batch()
                for ((docRef, data) in chunk) {
                    batch.set(docRef, data)
                }
                batch.commit().await()
            }

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, "users/$userId")
            Result.failure(e)
        }
    }
}
