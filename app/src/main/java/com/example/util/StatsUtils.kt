package com.example.util

import com.example.data.CardsData
import com.example.data.QuestionsData
import com.example.data.SyllabusData
import com.example.data.Topic
import com.example.model.AppState
import kotlin.math.roundToInt

data class DayMinutes(val day: String, val min: Int)
data class ChapterAcc(val c: Int, val t: Int, val pct: Int)

object StatsUtils {
    fun minutesByDay(s: AppState): Map<String, Int> {
        val m = mutableMapOf<String, Int>()
        for (x in s.sessions) {
            m[x.day] = (m[x.day] ?: 0) + x.minutes
        }
        return m
    }

    private fun sessionStreak(s: AppState): Int {
        val m = minutesByDay(s)
        var day = DateUtils.todayStr()
        if ((m[day] ?: 0) == 0) {
            day = DateUtils.addDays(day, -1)
        }
        var n = 0
        while ((m[day] ?: 0) > 0) {
            n++
            day = DateUtils.addDays(day, -1)
        }
        return n
    }

    fun studiedToday(s: AppState): Boolean {
        val today = DateUtils.todayStr()
        if (s.profile.lastStudyDate == today) return true
        return (minutesByDay(s)[today] ?: 0) > 0
    }

    fun streak(s: AppState): Int {
        val today = DateUtils.todayStr()
        val last = s.profile.lastStudyDate?.takeIf { DateUtils.isValidDay(it) }
        val storedActive = if (last != null) {
            val diff = DateUtils.daysBetween(last, today)
            if (diff in 0..1) s.profile.currentStreak.coerceAtLeast(0) else 0
        } else {
            0
        }
        return maxOf(storedActive, sessionStreak(s))
    }

    fun bestStreak(s: AppState): Int = maxOf(s.profile.bestStreak.coerceAtLeast(0), streak(s))

    fun weekMinutes(s: AppState): List<DayMinutes> {
        val m = minutesByDay(s)
        return DateUtils.lastNDays(7).map { d -> DayMinutes(d, m[d] ?: 0) }
    }

    fun topicStatus(s: AppState, id: String): String = s.progress["t:$id"] ?: ""

    fun syllabusPct(s: AppState): Int {
        val total = SyllabusData.ALL_TOPICS.size
        if (total == 0) return 0
        val done = SyllabusData.ALL_TOPICS.count { topicStatus(s, it.id) == "done" }
        return ((done.toDouble() / total) * 100.0).roundToInt()
    }

    fun latestByQuestion(s: AppState): Map<String, Boolean> {
        val out = LinkedHashMap<String, Boolean>()
        for (a in s.attempts) {
            if (!out.containsKey(a.questionId)) {
                out[a.questionId] = a.correct
            }
        }
        return out
    }

    fun mistakeIds(s: AppState): List<String> {
        val l = latestByQuestion(s)
        return l.entries
            .filter { (k, ok) -> !ok && QuestionsData.QMAP.containsKey(k) }
            .map { it.key }
    }

    fun chapterAccuracy(s: AppState): Map<Int, ChapterAcc> {
        val l = latestByQuestion(s)
        val counts = mutableMapOf<Int, Pair<Int, Int>>()
        for (ch in SyllabusData.CHAPTERS) {
            counts[ch.id] = 0 to 0
        }
        for ((id, ok) in l) {
            val q = QuestionsData.QMAP[id] ?: continue
            val (c, t) = counts[q.ch] ?: (0 to 0)
            counts[q.ch] = (c + if (ok) 1 else 0) to (t + 1)
        }
        return counts.mapValues { (_, pair) ->
            val (c, t) = pair
            val pct = if (t > 0) ((c.toDouble() / t) * 100.0).roundToInt() else 0
            ChapterAcc(c, t, pct)
        }
    }

    fun chapterProgress(s: AppState, chId: Int): Int {
        val ch = SyllabusData.findChapter(chId) ?: return 0
        if (ch.topics.isEmpty()) return 0
        val done = ch.topics.count { topicStatus(s, it.id) == "done" }
        return ((done.toDouble() / ch.topics.size) * 100.0).roundToInt()
    }

    fun weakestChapter(s: AppState): Int {
        val acc = chapterAccuracy(s)
        var best = 4
        var score = Double.POSITIVE_INFINITY
        for (ch in SyllabusData.CHAPTERS) {
            val a = acc[ch.id] ?: ChapterAcc(0, 0, 0)
            val v = (if (a.t > 0) a.pct.toDouble() else 0.0) - ch.examQ * 0.5
            if (v < score) {
                score = v
                best = ch.id
            }
        }
        return best
    }

    fun readiness(s: AppState): Int {
        val acc = chapterAccuracy(s)
        var total = 0.0
        var w = 0
        for (ch in SyllabusData.CHAPTERS) {
            val study = chapterProgress(s, ch.id).toDouble()
            val a = acc[ch.id] ?: ChapterAcc(0, 0, 0)
            val quiz = when {
                a.t >= 3 -> a.pct.toDouble()
                a.t > 0 -> a.pct * 0.7
                else -> 0.0
            }
            total += (study * 0.35 + quiz * 0.65) * ch.examQ
            w += ch.examQ
        }
        return if (w > 0) (total / w).roundToInt() else 0
    }

    fun dueCards(s: AppState): List<String> {
        val today = DateUtils.todayStr()
        return CardsData.CARDS.filter { c ->
            val st = s.cards[c.id]
            st == null || st.due <= today
        }.map { it.id }
    }

    fun masteredCards(s: AppState): Int =
        CardsData.CARDS.count { (s.cards[it.id]?.box ?: 0) >= 4 }

    fun nextTopic(s: AppState): Topic? {
        return SyllabusData.ALL_TOPICS.firstOrNull { topicStatus(s, it.id) == "learning" }
            ?: SyllabusData.ALL_TOPICS.firstOrNull { topicStatus(s, it.id).isEmpty() }
            ?: SyllabusData.ALL_TOPICS.firstOrNull { topicStatus(s, it.id) == "review" }
    }

    fun totalAnswered(s: AppState): Int = s.attempts.size

    fun overallAccuracy(s: AppState): Int {
        if (s.attempts.isEmpty()) return 0
        val correct = s.attempts.count { it.correct }
        return ((correct.toDouble() / s.attempts.size) * 100.0).roundToInt()
    }
}
