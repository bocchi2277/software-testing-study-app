package com.example.util

import com.example.model.CardState

/**
 * Shared pure Leitner spaced-repetition scheduler used consistently by both
 * Guest Mode (in-memory) and Firestore transactions.
 */
object LeitnerScheduler {
    val BOX_DAYS: List<Int> = listOf(0, 1, 2, 4, 8, 16)
    const val MIN_BOX = 1
    const val MAX_BOX = 5
    const val MASTERED_BOX_THRESHOLD = 4

    fun intervalDaysForBox(box: Int): Int =
        BOX_DAYS[box.coerceIn(MIN_BOX, MAX_BOX)]

    fun nextCardState(
        prev: CardState?,
        known: Boolean,
        today: String
    ): CardState {
        val oldBox = (prev?.box ?: MIN_BOX).coerceIn(MIN_BOX, MAX_BOX)
        val newBox = if (known) minOf(MAX_BOX, oldBox + 1) else MIN_BOX
        val daysToAdd = if (known) intervalDaysForBox(newBox) else 0
        val nextDue = DateUtils.addDays(today, daysToAdd)
        val newReps = ((prev?.reps ?: 0).coerceAtLeast(0) + 1).coerceAtMost(100_000)
        val newLapses = ((prev?.lapses ?: 0).coerceAtLeast(0) + if (known) 0 else 1).coerceAtMost(100_000)
        return CardState(
            box = newBox,
            due = nextDue,
            reps = newReps,
            lapses = newLapses
        )
    }

    fun isMastered(state: CardState?): Boolean =
        (state?.box ?: 0) >= MASTERED_BOX_THRESHOLD

    fun isDue(state: CardState?, today: String): Boolean =
        state == null || state.due <= today
}
