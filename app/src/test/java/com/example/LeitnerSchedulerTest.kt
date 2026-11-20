package com.example

import com.example.model.AppState
import com.example.model.CardState
import com.example.model.IstqbFlashcard
import com.example.util.DateUtils
import com.example.util.LeitnerScheduler
import com.example.util.StatsUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LeitnerSchedulerTest {

    @Test
    fun `box intervals match Leitner schedule`() {
        assertEquals(listOf(0, 1, 2, 4, 8, 16), LeitnerScheduler.BOX_DAYS)
        assertEquals(1, LeitnerScheduler.intervalDaysForBox(0)) // clamped to MIN_BOX=1
        assertEquals(1, LeitnerScheduler.intervalDaysForBox(1))
        assertEquals(2, LeitnerScheduler.intervalDaysForBox(2))
        assertEquals(4, LeitnerScheduler.intervalDaysForBox(3))
        assertEquals(8, LeitnerScheduler.intervalDaysForBox(4))
        assertEquals(16, LeitnerScheduler.intervalDaysForBox(5))
        assertEquals(16, LeitnerScheduler.intervalDaysForBox(9)) // clamped to MAX_BOX=5
    }

    @Test
    fun `new card answered known advances from box 1 to box 2 with +2 days`() {
        val today = "2026-05-01"
        val next = LeitnerScheduler.nextCardState(prev = null, known = true, today = today)

        assertEquals(2, next.box)
        assertEquals("2026-05-03", next.due)
        assertEquals(1, next.reps)
        assertEquals(0, next.lapses)
    }

    @Test
    fun `new card answered forgotten stays in box 1 due today with 1 lapse`() {
        val today = "2026-05-01"
        val next = LeitnerScheduler.nextCardState(prev = null, known = false, today = today)

        assertEquals(1, next.box)
        assertEquals(today, next.due)
        assertEquals(1, next.reps)
        assertEquals(1, next.lapses)
    }

    @Test
    fun `full mastery path progresses 2, 3, 4, 5 and caps at box 5`() {
        var today = "2026-06-01"
        var state = LeitnerScheduler.nextCardState(null, known = true, today = today)
        assertEquals(2 to "2026-06-03", state.box to state.due)

        today = state.due
        state = LeitnerScheduler.nextCardState(state, known = true, today = today)
        assertEquals(3 to "2026-06-07", state.box to state.due) // +4 days

        today = state.due
        state = LeitnerScheduler.nextCardState(state, known = true, today = today)
        assertEquals(4 to "2026-06-15", state.box to state.due) // +8 days

        today = state.due
        state = LeitnerScheduler.nextCardState(state, known = true, today = today)
        assertEquals(5 to "2026-07-01", state.box to state.due) // +16 days

        // Reviewing again at box 5 keeps box capped at 5 (+16 days)
        today = state.due
        state = LeitnerScheduler.nextCardState(state, known = true, today = today)
        assertEquals(5, state.box)
        assertEquals("2026-07-17", state.due)
        assertEquals(5, state.reps)
        assertEquals(0, state.lapses)
    }

    @Test
    fun `lapse in box 4 resets back to box 1 due immediately`() {
        val highCard = CardState(box = 4, due = "2026-05-10", reps = 3, lapses = 0)
        val afterLapse = LeitnerScheduler.nextCardState(highCard, known = false, today = "2026-05-10")

        assertEquals(1, afterLapse.box)
        assertEquals("2026-05-10", afterLapse.due)
        assertEquals(4, afterLapse.reps)
        assertEquals(1, afterLapse.lapses)
    }

    @Test
    fun `mastered and due helpers respect threshold and today comparison`() {
        assertFalse(LeitnerScheduler.isMastered(null))
        assertFalse(LeitnerScheduler.isMastered(CardState(box = 3, due = "2026-05-05")))
        assertTrue(LeitnerScheduler.isMastered(CardState(box = 4, due = "2026-05-10")))
        assertTrue(LeitnerScheduler.isMastered(CardState(box = 5, due = "2026-05-20")))

        assertTrue(LeitnerScheduler.isDue(null, "2026-05-05"))
        assertTrue(LeitnerScheduler.isDue(CardState(box = 2, due = "2026-05-04"), "2026-05-05"))
        assertTrue(LeitnerScheduler.isDue(CardState(box = 2, due = "2026-05-05"), "2026-05-05"))
        assertFalse(LeitnerScheduler.isDue(CardState(box = 2, due = "2026-05-06"), "2026-05-05"))
    }

    @Test
    fun `StatsUtils dueCards and masteredCards aggregate custom deck correctly`() {
        val today = DateUtils.todayStr()
        val yesterday = DateUtils.addDays(today, -1)
        val nextWeek = DateUtils.addDays(today, 7)

        val deck = listOf(
            IstqbFlashcard(id = "c1", term = "T1", question = "Q1", answer = "A1", chapter = 1),
            IstqbFlashcard(id = "c2", term = "T2", question = "Q2", answer = "A2", chapter = 1),
            IstqbFlashcard(id = "c3", term = "T3", question = "Q3", answer = "A3", chapter = 2)
        )
        val state = AppState(
            flashcards = deck,
            cards = mapOf(
                "c1" to CardState(box = 4, due = nextWeek, reps = 3, lapses = 0),
                "c2" to CardState(box = 2, due = yesterday, reps = 1, lapses = 0)
                // c3 is unreviewed -> due
            )
        )

        assertEquals(1, StatsUtils.masteredCards(state))
        assertEquals(listOf("c2", "c3"), StatsUtils.dueCards(state))
    }
}
