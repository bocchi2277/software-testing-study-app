package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val examDate: String? = null,
    val startDate: String = "",
    val dailyHours: Double = 2.0,
    val startTime: String = "18:00",
    val theme: String = "auto",
    val focusMin: Int = 25,
    val breakMin: Int = 5,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastStudyDate: String? = null,
    val totalFocusMinutes: Int = 0,
    val focusPoints: Int = 0
)

@Serializable
data class StudySession(
    val id: String = "",
    val day: String,
    val minutes: Int,
    val kind: String = "focus",
    val topic: String? = null
)

@Serializable
data class CardState(
    val box: Int = 1,
    val due: String,
    val reps: Int = 0,
    val lapses: Int = 0
)

@Serializable
data class QuizAttempt(
    val questionId: String,
    val correct: Boolean,
    val mode: String = "practice",
    val at: String
)

@Serializable
data class ChapterBreakdown(
    val c: Int,
    val t: Int
)

@Serializable
data class ExamResult(
    val id: String = "",
    val score: Int,
    val total: Int,
    val seconds: Int,
    val breakdown: Map<String, ChapterBreakdown> = emptyMap(),
    val createdAt: String
)

@Serializable
data class NoteItem(
    val id: String = "",
    val title: String,
    val body: String,
    val topic: String? = null,
    val updatedAt: String
)

@Serializable
data class TaskItem(
    val id: String = "",
    val title: String,
    val day: String,
    val done: Boolean = false
)

@Serializable
data class IstqbFlashcard(
    val id: String = "",
    val term: String,
    val question: String,
    val answer: String,
    val chapter: Int = 1,
    val hint: String? = null,
    val source: String = "istqb"
)

@Serializable
data class AppState(
    val profile: Profile = Profile(),
    val progress: Map<String, String> = emptyMap(),
    val sessions: List<StudySession> = emptyList(),
    val cards: Map<String, CardState> = emptyMap(),
    val flashcards: List<IstqbFlashcard> = emptyList(),
    val attempts: List<QuizAttempt> = emptyList(),
    val exams: List<ExamResult> = emptyList(),
    val notes: List<NoteItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList()
)

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

enum class TopicStatus(val key: String, val label: String) {
    NONE("", "لم أبدأ"),
    LEARNING("learning", "أتعلم"),
    DONE("done", "أتقنته"),
    REVIEW("review", "يحتاج مراجعة");

    companion object {
        fun fromKey(key: String?): TopicStatus =
            entries.firstOrNull { it.key == (key ?: "") } ?: NONE
    }
}
