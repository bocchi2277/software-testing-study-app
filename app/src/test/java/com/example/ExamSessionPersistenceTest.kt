package com.example

import com.example.data.PreparedQuestion
import com.example.data.QuestionsData
import com.example.ui.screens.PracticeSessionPersistence
import com.example.ui.screens.PracticeView
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamSessionPersistenceTest {

    @Test
    fun `exam session view round-trip preserves exact question IDs, option order, duration, and language`() {
        val sampleQuestions = QuestionsData.QUESTIONS.take(40).map { q ->
            val fixedOrder = listOf(2, 0, 3, 1)
            PreparedQuestion(question = q, order = fixedOrder, correctIdx = fixedOrder.indexOf(0))
        }
        val original = PracticeView.ExamSession(
            qs = sampleQuestions,
            minutes = 75,
            initialEnglish = true
        )

        val encoded = PracticeSessionPersistence.encodeView(original)
        val restored = PracticeSessionPersistence.decodeView(encoded)

        assertTrue(restored is PracticeView.ExamSession)
        restored as PracticeView.ExamSession
        assertEquals(75, restored.minutes)
        assertEquals(true, restored.initialEnglish)
        assertEquals(40, restored.qs.size)
        restored.qs.forEachIndexed { idx, pq ->
            assertEquals(sampleQuestions[idx].question.id, pq.question.id)
            assertEquals(sampleQuestions[idx].order, pq.order)
            assertEquals(sampleQuestions[idx].correctIdx, pq.correctIdx)
        }
    }

    @Test
    fun `practice session view round-trip preserves title, order, and language`() {
        val sampleQuestions = QuestionsData.QUESTIONS.take(10).map { QuestionsData.prepare(it) }
        val original = PracticeView.PracticeSession(
            qs = sampleQuestions,
            title = "الفصل 4: تقنيات الاختبار",
            initialEnglish = false
        )

        val encoded = PracticeSessionPersistence.encodeView(original)
        val restored = PracticeSessionPersistence.decodeView(encoded)

        assertTrue(restored is PracticeView.PracticeSession)
        restored as PracticeView.PracticeSession
        assertEquals("الفصل 4: تقنيات الاختبار", restored.title)
        assertEquals(false, restored.initialEnglish)
        assertEquals(sampleQuestions.map { it.question.id }, restored.qs.map { it.question.id })
        assertEquals(sampleQuestions.map { it.order }, restored.qs.map { it.order })
    }

    @Test
    fun `answers and flagged questions survive serialization round-trip`() {
        val originalAnswers = MutableList<Int?>(40) { null }.apply {
            this[0] = 3
            this[5] = 1
            this[39] = 0
        }
        val encodedAnswers = PracticeSessionPersistence.encodeAnswers(originalAnswers)
        val restoredAnswers = PracticeSessionPersistence.decodeAnswers(encodedAnswers, 40)
        assertEquals(originalAnswers, restoredAnswers)

        val originalFlags = mapOf(2 to true, 7 to false, 15 to true, 38 to true)
        val encodedFlags = PracticeSessionPersistence.encodeFlags(originalFlags)
        val restoredFlags = PracticeSessionPersistence.decodeFlags(encodedFlags)
        assertEquals(mapOf(2 to true, 15 to true, 38 to true), restoredFlags)
    }

    @Test
    fun `exam countdown preserves remaining seconds from saved start timestamp across rotation`() {
        val startAtMs = 1_700_000_000_000L
        val afterTwelveMinutesMs = startAtMs + 12 * 60_000L + 30_000L

        val remaining = PracticeSessionPersistence.computeRemainingSeconds(
            startAtMs = startAtMs,
            minutes = 60,
            nowMs = afterTwelveMinutesMs
        )
        val elapsed = PracticeSessionPersistence.computeElapsedSeconds(
            startAtMs = startAtMs,
            minutes = 60,
            nowMs = afterTwelveMinutesMs
        )

        assertEquals(47 * 60 + 30, remaining)
        assertEquals(12 * 60 + 30, elapsed)

        val expired = PracticeSessionPersistence.computeRemainingSeconds(
            startAtMs = startAtMs,
            minutes = 60,
            nowMs = startAtMs + 65 * 60_000L
        )
        assertEquals(0, expired)
    }
}
