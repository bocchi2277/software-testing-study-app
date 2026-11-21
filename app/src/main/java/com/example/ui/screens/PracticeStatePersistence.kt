package com.example.ui.screens

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import com.example.data.PreparedQuestion
import com.example.data.QuestionsData
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.math.roundToInt

internal sealed interface PracticeView {
    data object Menu : PracticeView
    data class PracticeSession(val qs: List<PreparedQuestion>, val title: String, val initialEnglish: Boolean) : PracticeView
    data class ExamSession(val qs: List<PreparedQuestion>, val minutes: Int, val initialEnglish: Boolean) : PracticeView
}

@Serializable
internal data class SavedPreparedQuestion(
    val id: String,
    val order: List<Int>
)

@Serializable
internal sealed interface SavedPracticeView {
    @Serializable
    data object Menu : SavedPracticeView

    @Serializable
    data class PracticeSession(
        val qs: List<SavedPreparedQuestion>,
        val title: String,
        val initialEnglish: Boolean
    ) : SavedPracticeView

    @Serializable
    data class ExamSession(
        val qs: List<SavedPreparedQuestion>,
        val minutes: Int,
        val initialEnglish: Boolean
    ) : SavedPracticeView
}

internal object PracticeSessionPersistence {
    private val json = Json { ignoreUnknownKeys = true }

    fun encodePreparedList(qs: List<PreparedQuestion>): List<SavedPreparedQuestion> =
        qs.map { SavedPreparedQuestion(id = it.question.id, order = it.order) }

    fun decodePreparedList(saved: List<SavedPreparedQuestion>): List<PreparedQuestion> =
        saved.mapNotNull { item ->
            val question = QuestionsData.QMAP[item.id] ?: return@mapNotNull null
            val validOrder = if (item.order.sorted() == listOf(0, 1, 2, 3)) item.order else listOf(0, 1, 2, 3)
            PreparedQuestion(
                question = question,
                order = validOrder,
                correctIdx = validOrder.indexOf(0)
            )
        }

    fun encodeView(view: PracticeView): String {
        val dto: SavedPracticeView = when (view) {
            PracticeView.Menu -> SavedPracticeView.Menu
            is PracticeView.PracticeSession -> SavedPracticeView.PracticeSession(
                qs = encodePreparedList(view.qs),
                title = view.title,
                initialEnglish = view.initialEnglish
            )
            is PracticeView.ExamSession -> SavedPracticeView.ExamSession(
                qs = encodePreparedList(view.qs),
                minutes = view.minutes,
                initialEnglish = view.initialEnglish
            )
        }
        return json.encodeToString(SavedPracticeView.serializer(), dto)
    }

    fun decodeView(raw: String): PracticeView {
        if (raw.isBlank()) return PracticeView.Menu
        return runCatching {
            when (val dto = json.decodeFromString(SavedPracticeView.serializer(), raw)) {
                SavedPracticeView.Menu -> PracticeView.Menu
                is SavedPracticeView.PracticeSession -> {
                    val decoded = decodePreparedList(dto.qs)
                    if (decoded.isEmpty()) PracticeView.Menu
                    else PracticeView.PracticeSession(decoded, dto.title, dto.initialEnglish)
                }
                is SavedPracticeView.ExamSession -> {
                    val decoded = decodePreparedList(dto.qs)
                    if (decoded.isEmpty()) PracticeView.Menu
                    else PracticeView.ExamSession(decoded, dto.minutes, dto.initialEnglish)
                }
            }
        }.getOrDefault(PracticeView.Menu)
    }

    fun encodeAnswers(answers: List<Int?>): String =
        json.encodeToString(answers)

    fun decodeAnswers(raw: String, size: Int): List<Int?> {
        val parsed = runCatching { json.decodeFromString<List<Int?>>(raw) }.getOrNull()
        if (parsed != null && parsed.size == size) return parsed
        return List(size) { null }
    }

    fun encodeFlags(flags: Map<Int, Boolean>): String =
        json.encodeToString(flags.filterValues { it }.keys.toList())

    fun decodeFlags(raw: String): Map<Int, Boolean> {
        val keys = runCatching { json.decodeFromString<List<Int>>(raw) }.getOrDefault(emptyList())
        return keys.associateWith { true }
    }

    fun encodeIntList(items: List<Int>): String = json.encodeToString(items)

    fun decodeIntList(raw: String): List<Int> =
        runCatching { json.decodeFromString<List<Int>>(raw) }.getOrDefault(emptyList())

    fun computeRemainingSeconds(startAtMs: Long, minutes: Int, nowMs: Long = System.currentTimeMillis()): Int {
        val endMs = startAtMs + minutes * 60_000L
        return ((endMs - nowMs) / 1000.0).roundToInt().coerceAtLeast(0)
    }

    fun computeElapsedSeconds(startAtMs: Long, minutes: Int, nowMs: Long = System.currentTimeMillis()): Int {
        return ((nowMs - startAtMs) / 1000.0).roundToInt().coerceIn(0, minutes * 60)
    }

    val PracticeViewSaver: Saver<PracticeView, String> = Saver(
        save = { encodeView(it) },
        restore = { decodeView(it) }
    )

    fun answersSaver(size: Int): Saver<SnapshotStateList<Int?>, String> = Saver(
        save = { encodeAnswers(it.toList()) },
        restore = { decodeAnswers(it, size).toMutableStateList() }
    )

    val FlagsSaver: Saver<SnapshotStateMap<Int, Boolean>, String> = Saver(
        save = { encodeFlags(it.toMap()) },
        restore = {
            SnapshotStateMap<Int, Boolean>().apply {
                putAll(decodeFlags(it))
            }
        }
    )

    val IntListSaver: Saver<SnapshotStateList<Int>, String> = Saver(
        save = { encodeIntList(it.toList()) },
        restore = { decodeIntList(it).toMutableStateList() }
    )
}
