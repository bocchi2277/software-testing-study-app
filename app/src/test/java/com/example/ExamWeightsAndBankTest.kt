package com.example

import com.example.data.QuestionsData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.model.QuizAttempt
import com.example.util.StatsUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamWeightsAndBankTest {

    @Test
    fun `official ISTQB CTFL v4 chapter weights sum to 40`() {
        val weights = SyllabusData.CHAPTERS.associate { it.id to it.examQ }
        assertEquals(
            mapOf(1 to 8, 2 to 6, 3 to 4, 4 to 11, 5 to 9, 6 to 2),
            weights
        )
        assertEquals(40, SyllabusData.CHAPTERS.sumOf { it.examQ })
    }

    @Test
    fun `question bank has 120 balanced bilingual questions with valid options`() {
        val all = QuestionsData.QUESTIONS
        assertEquals(120, all.size)

        val countsByChapter = all.groupingBy { it.ch }.eachCount()
        assertEquals(
            mapOf(1 to 20, 2 to 16, 3 to 14, 4 to 38, 5 to 22, 6 to 10),
            countsByChapter
        )

        val ids = all.map { it.id }
        assertEquals(120, ids.toSet().size)
        assertEquals((1..120).map { QuestionsData.idForIndex(it - 1) }, ids)

        all.forEach { q ->
            assertTrue("Question ${q.id} Arabic stem blank", q.q.isNotBlank())
            assertTrue("Question ${q.id} English stem blank", q.qEn.isNotBlank())
            assertTrue("Question ${q.id} Arabic exp blank", q.exp.isNotBlank())
            assertTrue("Question ${q.id} English exp blank", q.expEn.isNotBlank())

            assertEquals("Question ${q.id} AR option count", 4, q.options.size)
            assertEquals("Question ${q.id} EN option count", 4, q.optionsEn.size)
            assertEquals("Question ${q.id} AR options must be unique", 4, q.options.toSet().size)
            assertEquals("Question ${q.id} EN options must be unique", 4, q.optionsEn.toSet().size)
            q.options.forEach { opt ->
                assertTrue("Question ${q.id} blank AR option", opt.isNotBlank())
                assertFalse("Question ${q.id} uses shuffle-unsafe AR wording", opt.contains("أعلاه"))
            }
            q.optionsEn.forEach { opt ->
                assertTrue("Question ${q.id} blank EN option", opt.isNotBlank())
                assertFalse(
                    "Question ${q.id} uses shuffle-unsafe 'of the above'",
                    opt.contains("of the above", ignoreCase = true)
                )
            }
        }
    }

    @Test
    fun `prepare keeps correctIdx aligned with original index 0 after shuffling`() {
        val sample = QuestionsData.QUESTIONS.first()
        repeat(20) {
            val prepared = QuestionsData.prepare(sample)
            assertEquals(listOf(0, 1, 2, 3), prepared.order.sorted())
            assertEquals(0, prepared.order[prepared.correctIdx])
            assertEquals(sample.options[0], sample.options[prepared.order[prepared.correctIdx]])
            assertEquals(sample.optionsEn[0], sample.optionsEn[prepared.order[prepared.correctIdx]])
        }
    }

    @Test
    fun `readiness weights chapters by examQ and applies low-sample dampening`() {
        assertEquals(0, StatsUtils.readiness(AppState()))

        // Complete all Chapter 4 topics (weight 11/40) with 0 quiz attempts:
        // ((100 * 0.35) * 11) / 40 = 9.625 -> 10%
        val ch4Topics = SyllabusData.findChapter(4)!!.topics.associate { "t:${it.id}" to "done" }
        assertEquals(10, StatsUtils.readiness(AppState(progress = ch4Topics)))

        // 2 correct attempts in Chapter 1 (weight 8/40, < 3 attempts => 0.7 dampening):
        // effective quiz = 100 * 0.7 = 70; (70 * 0.65 * 8) / 40 = 9.1 -> 9%
        val twoCh1Attempts = listOf(
            QuizAttempt("q001", true, "practice", "2026-05-01T10:00:00Z"),
            QuizAttempt("q002", true, "practice", "2026-05-01T10:01:00Z")
        )
        assertEquals(9, StatsUtils.readiness(AppState(attempts = twoCh1Attempts)))

        // 3 correct attempts in Chapter 1 (>= 3 attempts => full 100% quiz weight):
        // (100 * 0.65 * 8) / 40 = 13.0 -> 13%
        val threeCh1Attempts = twoCh1Attempts + QuizAttempt("q003", true, "practice", "2026-05-01T10:02:00Z")
        assertEquals(13, StatsUtils.readiness(AppState(attempts = threeCh1Attempts)))

        // 100% syllabus + 3 correct questions per chapter => 100% readiness
        val allTopicsDone = SyllabusData.ALL_TOPICS.associate { "t:${it.id}" to "done" }
        val allChaptersPassed = SyllabusData.CHAPTERS.flatMap { ch ->
            QuestionsData.QUESTIONS.filter { it.ch == ch.id }.take(3).map { q ->
                QuizAttempt(q.id, true, "practice", "2026-05-01T10:00:00Z")
            }
        }
        assertEquals(100, StatsUtils.readiness(AppState(progress = allTopicsDone, attempts = allChaptersPassed)))
    }

    @Test
    fun `weakestChapter prioritizes high-weight low-accuracy chapter`() {
        // When untested across all chapters, Chapter 4 has highest examQ=11 -> lowest score (0 - 11*0.5 = -5.5)
        assertEquals(4, StatsUtils.weakestChapter(AppState()))

        // If Chapter 4 has 100% accuracy while Chapter 5 (examQ=9) is untested -> Chapter 5 becomes weakest
        val ch4Perfect = QuestionsData.QUESTIONS.filter { it.ch == 4 }.take(3).map { q ->
            QuizAttempt(q.id, true, "practice", "2026-05-01T10:00:00Z")
        }
        assertEquals(5, StatsUtils.weakestChapter(AppState(attempts = ch4Perfect)))
    }

    @Test
    fun `pointsForSession and focusLevel thresholds compute accurately`() {
        assertEquals(65, StatsUtils.pointsForSession(25, "focus")) // 25*2 + 15
        assertEquals(50, StatsUtils.pointsForSession(25, "manual")) // 25*2 + 0
        assertEquals(17, StatsUtils.pointsForSession(0, "focus")) // clamped min 1 * 2 + 15

        assertEquals(1, StatsUtils.focusLevel(0).level)
        assertEquals(2, StatsUtils.focusLevel(100).level)
        assertEquals(3, StatsUtils.focusLevel(300).level)
        assertEquals(4, StatsUtils.focusLevel(700).level)
        assertEquals(5, StatsUtils.focusLevel(1500).level)
    }
}
