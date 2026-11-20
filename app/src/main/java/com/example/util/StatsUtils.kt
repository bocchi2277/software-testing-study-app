package com.example.util

import com.example.data.CardsData
import com.example.data.QuestionsData
import com.example.data.SyllabusData
import com.example.data.Topic
import com.example.model.AppState
import com.example.model.IstqbFlashcard
import kotlin.math.roundToInt

data class DayMinutes(val day: String, val min: Int)
data class WeeklyDayActivity(
    val day: String,
    val focusMin: Int,
    val manualMin: Int,
    val totalMin: Int,
    val focusSessions: Int,
    val earnedPoints: Int
)
data class ChapterAcc(val c: Int, val t: Int, val pct: Int)

data class FocusLevelInfo(
    val level: Int,
    val title: String,
    val emoji: String,
    val currentPoints: Int,
    val levelStartPoints: Int,
    val nextLevelPoints: Int
) {
    val progressPct: Int
        get() {
            val span = (nextLevelPoints - levelStartPoints).coerceAtLeast(1)
            val gained = (currentPoints - levelStartPoints).coerceIn(0, span)
            return ((gained.toDouble() / span) * 100.0).roundToInt()
        }
}

data class AchievementBadge(
    val id: String,
    val name: String,
    val desc: String,
    val unlocked: Boolean,
    val icon: String
)

object StatsUtils {
    fun pointsForSession(minutes: Int, kind: String): Int {
        val safeMin = minutes.coerceIn(1, 600)
        val bonus = if (kind == "focus") 15 else 0
        return safeMin * 2 + bonus
    }

    fun totalFocusMinutes(s: AppState): Int {
        val fromSessions = s.sessions.sumOf { it.minutes.coerceAtLeast(0) }
        return maxOf(s.profile.totalFocusMinutes.coerceAtLeast(0), fromSessions)
    }

    fun focusSessionCount(s: AppState): Int =
        s.sessions.count { it.kind == "focus" }

    fun focusPoints(s: AppState): Int {
        val fromSessions = s.sessions.sumOf { pointsForSession(it.minutes, it.kind) }
        return maxOf(s.profile.focusPoints.coerceAtLeast(0), fromSessions)
    }

    fun focusLevel(points: Int): FocusLevelInfo {
        val p = points.coerceAtLeast(0)
        return when {
            p < 100 -> FocusLevelInfo(1, "مبتدئ التركيز", "🌱", p, 0, 100)
            p < 300 -> FocusLevelInfo(2, "دارس مثابر", "📘", p, 100, 300)
            p < 700 -> FocusLevelInfo(3, "محترف بومودورو", "🍅", p, 300, 700)
            p < 1500 -> FocusLevelInfo(4, "سيد التركيز", "🔥", p, 700, 1500)
            else -> FocusLevelInfo(5, "أسطورة ISTQB", "🏆", p, 1500, ((p / 500) + 1) * 500)
        }
    }

    fun focusBadges(s: AppState): List<AchievementBadge> {
        val totalMin = totalFocusMinutes(s)
        val focusSessions = focusSessionCount(s)
        val pts = focusPoints(s)
        return listOf(
            AchievementBadge("pomodoro_first", "أول طماطمة", "أكمل أول جلسة بومودورو", focusSessions >= 1, "🍅"),
            AchievementBadge("pomodoro_2h", "ساعتان تركيز", "اجمع 120 دقيقة تركيز", totalMin >= 120, "⏱️"),
            AchievementBadge("pomodoro_5", "بطل التركيز", "أكمل 5 جلسات بومودورو", focusSessions >= 5, "⚡"),
            AchievementBadge("pomodoro_xp", "جامع النقاط", "اجمع 500 نقطة تقدم", pts >= 500, "⭐")
        )
    }

    fun allBadges(s: AppState): List<AchievementBadge> {
        val totalMin = totalFocusMinutes(s)
        val streakDays = streak(s)
        val topicsDone = SyllabusData.ALL_TOPICS.count { topicStatus(s, it.id) == "done" }
        val answered = totalAnswered(s)
        val mastered = masteredCards(s)
        val bestExam = s.exams.maxOfOrNull { ((it.score.toDouble() / it.total) * 100.0).roundToInt() } ?: 0
        return focusBadges(s) + listOf(
            AchievementBadge("start", "البداية", "سجّل أول جلسة دراسة", s.sessions.isNotEmpty() || totalMin > 0, "🌱"),
            AchievementBadge("streak_7", "أسبوع متواصل", "7 أيام متتالية", streakDays >= 7, "🔥"),
            AchievementBadge("hours_10", "10 ساعات", "أكمل 10 ساعات دراسة", totalMin >= 600, "⏳"),
            AchievementBadge("hours_50", "50 ساعة", "أكمل 50 ساعة دراسة", totalMin >= 3000, "🏔️"),
            AchievementBadge("topics_10", "قارئ المنهج", "أتقن 10 مواضيع", topicsDone >= 10, "📚"),
            AchievementBadge("topics_all", "ختمة المنهج", "أتقن كل المواضيع", topicsDone == SyllabusData.ALL_TOPICS.size, "🏆"),
            AchievementBadge("questions_100", "100 سؤال", "أجب عن 100 سؤال", answered >= 100, "💯"),
            AchievementBadge("cards_30", "حافظ المصطلحات", "أتقن 30 بطاقة", mastered >= 30, "🃏"),
            AchievementBadge("exam_65", "ناجح في المحاكاة", "65% أو أكثر في محاكاة", bestExam >= 65, "✅"),
            AchievementBadge("exam_90", "جاهز للامتحان", "90% أو أكثر في محاكاة", bestExam >= 90, "🎓")
        )
    }

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

    fun weeklyActivity(s: AppState, daysCount: Int = 7): List<WeeklyDayActivity> {
        val days = DateUtils.lastNDays(daysCount)
        val byDay = s.sessions.groupBy { it.day }
        return days.map { day ->
            val list = byDay[day].orEmpty()
            var focusMin = 0
            var manualMin = 0
            var focusSessions = 0
            var earnedPoints = 0
            for (sess in list) {
                val mins = sess.minutes.coerceAtLeast(0)
                if (sess.kind == "focus") {
                    focusMin += mins
                    focusSessions++
                } else {
                    manualMin += mins
                }
                earnedPoints += pointsForSession(mins, sess.kind)
            }
            WeeklyDayActivity(
                day = day,
                focusMin = focusMin,
                manualMin = manualMin,
                totalMin = focusMin + manualMin,
                focusSessions = focusSessions,
                earnedPoints = earnedPoints
            )
        }
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

    fun effectiveFlashcards(s: AppState): List<IstqbFlashcard> =
        if (s.flashcards.isNotEmpty()) s.flashcards else CardsData.toDefaultFlashcards()

    fun dueCards(s: AppState): List<String> {
        val today = DateUtils.todayStr()
        return effectiveFlashcards(s).filter { c ->
            LeitnerScheduler.isDue(s.cards[c.id], today)
        }.map { it.id }
    }

    fun masteredCards(s: AppState): Int =
        effectiveFlashcards(s).count { LeitnerScheduler.isMastered(s.cards[it.id]) }

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
