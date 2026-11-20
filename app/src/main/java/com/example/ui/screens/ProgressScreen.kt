package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaProgressBar
import com.example.ui.components.ReadinessRing
import com.example.ui.components.SectionCard
import com.example.ui.components.StatCard
import com.example.ui.components.WeeklyActivityChartCard
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun ProgressScreen(state: AppState) {
    val qa = LocalQaColors.current
    val minutesMap = remember(state.sessions) { StatsUtils.minutesByDay(state) }
    val acc = remember(state.attempts) { StatsUtils.chapterAccuracy(state) }
    val today = DateUtils.todayStr()
    val totalMin = remember(state) { StatsUtils.totalFocusMinutes(state) }
    val focusPoints = remember(state) { StatsUtils.focusPoints(state) }
    val focusLevel = remember(focusPoints) { StatsUtils.focusLevel(focusPoints) }
    val focusSessionCount = remember(state) { StatsUtils.focusSessionCount(state) }
    val streakDays = StatsUtils.streak(state)
    val bestStreakDays = StatsUtils.bestStreak(state)
    val answered = StatsUtils.totalAnswered(state)
    val overallAcc = StatsUtils.overallAccuracy(state)
    val readinessPct = StatsUtils.readiness(state)
    val syllabusPct = StatsUtils.syllabusPct(state)
    val mastered = StatsUtils.masteredCards(state)
    val topicsDone = SyllabusData.ALL_TOPICS.count { state.progress["t:${it.id}"] == "done" }
    val bestExam = state.exams.maxOfOrNull { ((it.score.toDouble() / it.total) * 100.0).roundToInt() } ?: 0
    val recentExams = state.exams.reversed().takeLast(10)

    val weeks = 12
    val days = remember(today) {
        (0 until weeks * 7).map { i -> DateUtils.addDays(today, i - (weeks * 7 - 1)) }
    }
    val maxDay = max(30, minutesMap.values.maxOrNull() ?: 0)
    var selectedHeatDay by remember { mutableStateOf<String?>(null) }

    val badges = remember(state) { StatsUtils.allBadges(state) }
    val unlockedCount = badges.count { it.unlocked }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "التقدم والإنجازات 📈",
                sub = "أرقامك الحقيقية: ما أنجزته وما ينقصك."
            )
        }

        // Top 4 Stats
        item {
            BoxWithConstraints {
                val wide = maxWidth >= 540.dp
                if (wide) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatCard(DateUtils.fmtMinutes(totalMin), "إجمالي وقت التركيز", modifier = Modifier.weight(1f))
                        StatCard("$focusPoints XP", "نقاط التقدم", modifier = Modifier.weight(1f))
                        StatCard("🔥 $streakDays", "أيام متتالية (الأفضل: $bestStreakDays)", modifier = Modifier.weight(1f))
                        StatCard(
                            "$overallAcc%",
                            "دقتك العامة ($answered سؤال)",
                            toneColor = if (overallAcc >= 65) qa.ok else qa.bad,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatCard(DateUtils.fmtMinutes(totalMin), "إجمالي وقت التركيز", modifier = Modifier.weight(1f))
                            StatCard("$focusPoints XP", "نقاط التقدم", modifier = Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            StatCard("🔥 $streakDays", "أيام متتالية (الأفضل: $bestStreakDays)", modifier = Modifier.weight(1f))
                            StatCard(
                                "$overallAcc%",
                                "دقتك العامة ($answered سؤال)",
                                toneColor = if (overallAcc >= 65) qa.ok else qa.bad,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // Focus XP Level Progress
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${focusLevel.emoji} المستوى ${focusLevel.level}: ${focusLevel.title}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${focusLevel.currentPoints} / ${focusLevel.nextLevelPoints} XP",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                QaProgressBar(pct = focusLevel.progressPct)
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "أكملت $focusSessionCount جلسة بومودورو · كل دقيقة تركيز تمنحك +2 نقطة ومكافأة +15 نقطة لكل جلسة بومودورو.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }

        // Readiness Breakdown
        item {
            SectionCard {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ReadinessRing(pct = readinessPct, size = 122.dp, sub = "الجاهزية")
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("المنهج: $syllabusPct% ($topicsDone/${SyllabusData.ALL_TOPICS.size} موضوعاً)", fontWeight = FontWeight.Medium)
                        Text("البطاقات المتقنة: $mastered/${StatsUtils.effectiveFlashcards(state).size}", fontWeight = FontWeight.Medium)
                        Text("أفضل نتيجة محاكاة: $bestExam%", fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "الجاهزية تُحسب من تقدمك في المنهج (35%) ودقتك في الأسئلة (65%) مرجّحة بأوزان الفصول في الامتحان.",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                }
            }
        }

        // Weekly Activity Chart (Pomodoro study visualization)
        item {
            WeeklyActivityChartCard(state = state)
        }

        // 12-Week Heatmap
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("نشاطك آخر $weeks أسبوعاً", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    selectedHeatDay?.let { day ->
                        Text(
                            text = "$day: ${minutesMap[day] ?: 0} د",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LtrContent {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        for (w in 0 until weeks) {
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                for (r in 0 until 7) {
                                    val d = days[w * 7 + r]
                                    val v = minutesMap[d] ?: 0
                                    val lvl = if (v == 0) 0 else min(4, ceil((v.toDouble() / maxDay) * 4.0).toInt())
                                    val bgColor = if (lvl == 0) qa.border.copy(alpha = 0.5f)
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.25f + lvl * 0.19f)
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(RoundedCornerShape(3.dp))
                                            .background(bgColor)
                                            .clickable { selectedHeatDay = d }
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "اضغط على أي مربع لعرض تاريخه ودقائقه.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }

        // Chapter Readiness Table
        item {
            SectionCard {
                Text("الجاهزية حسب الفصل", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Text("الفصل", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.8f))
                    Text("المنهج", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(0.7f))
                    Text("الدقة", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(1.1f))
                    Text("وزنه", fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.weight(0.5f))
                }
                SyllabusData.CHAPTERS.forEach { c ->
                    HorizontalDivider(color = qa.border)
                    val a = acc[c.id]
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = SyllabusData.chapterLabel(c),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1.8f)
                        )
                        Text(
                            text = "${StatsUtils.chapterProgress(state, c.id)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.7f)
                        )
                        val accText = if (a != null && a.t > 0) "${a.pct}% (${a.c}/${a.t})" else "—"
                        val accColor = when {
                            a == null || a.t == 0 -> qa.muted
                            a.pct >= 65 -> qa.ok
                            else -> qa.bad
                        }
                        Text(
                            text = accText,
                            color = accColor,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1.1f)
                        )
                        Text(
                            text = "${c.examQ}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
            }
        }

        // Exam Simulation Bar Chart
        item {
            SectionCard {
                Text("نتائج محاكاة الامتحان", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                if (recentExams.isEmpty()) {
                    Text(
                        text = "لا محاكاة بعد. جرّب من صفحة «اختبارات».",
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted
                    )
                } else {
                    LtrContent {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        ) {
                            // 65% threshold line
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 86.dp) // ~65% of 132dp bar area
                            ) {
                                HorizontalDivider(thickness = 1.dp, color = qa.warn)
                                Text(
                                    text = "65%",
                                    fontSize = 10.sp,
                                    color = qa.warn,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 4.dp)
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                recentExams.forEach { exam ->
                                    val p = ((exam.score.toDouble() / exam.total) * 100.0).roundToInt().coerceIn(0, 100)
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Bottom,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("$p%", fontSize = 10.sp, color = qa.muted)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height((128 * (p / 100f)).dp.coerceAtLeast(4.dp))
                                                .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                                .background(if (p >= 65) qa.ok else qa.bad)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Achievement Badges
        item {
            SectionCard {
                Text(
                    text = "🏅 الإنجازات ($unlockedCount/${badges.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    badges.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { b ->
                                val bg = if (b.unlocked) qa.okBg else MaterialTheme.colorScheme.surface
                                val border = if (b.unlocked) qa.ok else qa.border
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(bg)
                                        .border(1.dp, border, RoundedCornerShape(12.dp))
                                        .alpha(if (b.unlocked) 1f else 0.65f)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(if (b.unlocked) b.icon else "🔒", fontSize = 22.sp)
                                    Column {
                                        Text(b.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                        Text(b.desc, style = MaterialTheme.typography.labelSmall, color = qa.muted)
                                    }
                                }
                            }
                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                val badgePct = ((unlockedCount.toDouble() / badges.size) * 100.0).roundToInt()
                QaProgressBar(pct = badgePct)
            }
        }
    }
}
