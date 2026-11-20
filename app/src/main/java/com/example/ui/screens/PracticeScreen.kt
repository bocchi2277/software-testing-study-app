package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PreparedQuestion
import com.example.data.QuestionsData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.QaProgressBar
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlin.math.roundToInt

private sealed interface PracticeView {
    data object Menu : PracticeView
    data class PracticeSession(val qs: List<PreparedQuestion>, val title: String) : PracticeView
    data class ExamSession(val qs: List<PreparedQuestion>, val minutes: Int) : PracticeView
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PracticeScreen(
    initialCh: Int?,
    initialMode: String?,
    state: AppState,
    viewModel: QaViewModel,
    onBackToMenu: () -> Unit
) {
    val qa = LocalQaColors.current
    var view by remember { mutableStateOf<PracticeView>(PracticeView.Menu) }
    var chFilter by remember { mutableStateOf(initialCh?.toString() ?: "all") }
    var qFilter by remember { mutableStateOf("all") }
    var qCount by remember { mutableIntStateOf(10) }
    var examMinutes by remember { mutableIntStateOf(60) }
    var autoLaunched by remember { mutableStateOf(false) }

    var chMenuExpanded by remember { mutableStateOf(false) }

    val acc = StatsUtils.chapterAccuracy(state)
    val latest = StatsUtils.latestByQuestion(state)
    val mistakeCount = StatsUtils.mistakeIds(state).size

    fun buildPractice(chapter: String, filter: String, count: Int): List<PreparedQuestion> {
        var pool = QuestionsData.QUESTIONS.filter { chapter == "all" || it.ch == chapter.toIntOrNull() }
        pool = when (filter) {
            "mistakes" -> pool.filter { latest[it.id] == false }
            "unseen" -> {
                val unseen = pool.filter { !latest.containsKey(it.id) }.shuffled()
                val rest = pool.filter { latest.containsKey(it.id) }.shuffled()
                unseen + rest
            }
            else -> pool.shuffled()
        }
        return pool.take(count).map { QuestionsData.prepare(it) }
    }

    fun startPractice(chapter: String, filter: String, count: Int): Boolean {
        val qs = buildPractice(chapter, filter, count)
        if (qs.isEmpty()) return false
        val title = when {
            filter == "mistakes" -> "مراجعة الأخطاء"
            chapter == "all" -> "تدريب شامل"
            else -> {
                val c = chapter.toIntOrNull()?.let { SyllabusData.findChapter(it) }
                "الفصل $chapter: ${c?.ar ?: ""}"
            }
        }
        view = PracticeView.PracticeSession(qs, title)
        return true
    }

    fun startExam() {
        val picked = mutableListOf<com.example.data.Question>()
        for (c in SyllabusData.CHAPTERS) {
            picked += QuestionsData.QUESTIONS.filter { it.ch == c.id }.shuffled().take(c.examQ)
        }
        if (picked.size < 40) {
            val rest = QuestionsData.QUESTIONS.filter { it !in picked }.shuffled()
            picked += rest.take(40 - picked.size)
        }
        view = PracticeView.ExamSession(picked.shuffled().map { QuestionsData.prepare(it) }, examMinutes)
    }

    LaunchedEffect(initialCh, initialMode) {
        if (!autoLaunched) {
            autoLaunched = true
            when {
                initialCh != null && initialCh in 1..6 -> {
                    chFilter = initialCh.toString()
                    qFilter = "unseen"
                    startPractice(initialCh.toString(), "unseen", 10)
                }
                initialMode == "mistakes" -> {
                    qFilter = "mistakes"
                    startPractice("all", "mistakes", 20)
                }
                initialMode == "exam" -> {
                    startExam()
                }
            }
        }
    }

    when (val current = view) {
        is PracticeView.PracticeSession -> {
            PracticeSessionView(
                qs = current.qs,
                title = current.title,
                viewModel = viewModel,
                onExit = {
                    view = PracticeView.Menu
                    onBackToMenu()
                }
            )
            return
        }
        is PracticeView.ExamSession -> {
            ExamSessionView(
                qs = current.qs,
                minutes = current.minutes,
                viewModel = viewModel,
                onExit = {
                    view = PracticeView.Menu
                    onBackToMenu()
                }
            )
            return
        }
        PracticeView.Menu -> Unit
    }

    val poolCount = if (qFilter == "mistakes") {
        QuestionsData.QUESTIONS.count { (chFilter == "all" || it.ch == chFilter.toIntOrNull()) && latest[it.id] == false }
    } else {
        QuestionsData.QUESTIONS.count { chFilter == "all" || it.ch == chFilter.toIntOrNull() }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "اختبارات وتدريب",
                sub = "${QuestionsData.QUESTIONS.size} سؤالاً بتفسير لكل إجابة، والبنك يتوسع باستمرار."
            )
        }

        // Custom Practice Card
        item {
            SectionCard {
                Text("📝 تدريب مخصص", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                Text("الفصل", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    OutlinedButton(
                        onClick = { chMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val chText = if (chFilter == "all") "كل الفصول"
                        else chFilter.toIntOrNull()?.let { SyllabusData.findChapter(it) }?.let { SyllabusData.chapterLabel(it) } ?: "كل الفصول"
                        Text(chText, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "اختر الفصل")
                    }
                    DropdownMenu(expanded = chMenuExpanded, onDismissRequest = { chMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("كل الفصول") },
                            onClick = { chFilter = "all"; chMenuExpanded = false }
                        )
                        SyllabusData.CHAPTERS.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(SyllabusData.chapterLabel(c)) },
                                onClick = { chFilter = c.id.toString(); chMenuExpanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("نوع الأسئلة", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QaFilterChip("عشوائي", qFilter == "all", { qFilter = "all" })
                    QaFilterChip("الجديدة أولاً", qFilter == "unseen", { qFilter = "unseen" })
                    QaFilterChip("أخطائي فقط ($mistakeCount)", qFilter == "mistakes", { qFilter = "mistakes" })
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text("العدد", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                Spacer(modifier = Modifier.height(4.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(5, 10, 20, 40).forEach { n ->
                        QaFilterChip("$n أسئلة", qCount == n, { qCount = n })
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        enabled = poolCount > 0,
                        onClick = { startPractice(chFilter, qFilter, qCount) },
                        modifier = Modifier.testTag("start_practice_button")
                    ) {
                        Text("ابدأ التدريب")
                    }
                    Text("$poolCount سؤالاً متاحاً بهذه الإعدادات", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                }
            }
        }

        // Exam Simulation Card
        item {
            SectionCard(borderColor = MaterialTheme.colorScheme.primary) {
                Text("⏱️ محاكاة الامتحان الحقيقي", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "40 سؤالاً بنفس توزيع الفصول في الامتحان، بدون إظهار الإجابات حتى النهاية. النجاح من 65% (26/40).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QaFilterChip("60 دقيقة (قياسي)", examMinutes == 60, { examMinutes = 60 })
                    QaFilterChip("75 دقيقة (غير ناطق بالإنجليزية)", examMinutes == 75, { examMinutes = 75 })
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { startExam() },
                    modifier = Modifier.testTag("start_exam_button")
                ) {
                    Text("ابدأ المحاكاة")
                }

                if (state.exams.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("آخر النتائج", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        state.exams.take(8).forEach { e ->
                            val p = ((e.score.toDouble() / e.total) * 100.0).roundToInt()
                            val pass = p >= 65
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (pass) qa.okBg else qa.badBg,
                                border = BorderStroke(1.dp, if (pass) qa.ok else qa.bad)
                            ) {
                                Text(
                                    text = "${e.score}/${e.total} ($p%)",
                                    color = if (pass) qa.ok else qa.bad,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Chapter Accuracy
        item {
            SectionCard {
                Text("دقتك حسب الفصل", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                SyllabusData.CHAPTERS.forEachIndexed { idx, c ->
                    val a = acc[c.id]
                    if (idx > 0) Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(SyllabusData.chapterLabel(c), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        Text(
                            text = if (a != null && a.t > 0) "${a.c}/${a.t} (${a.pct}%)" else "لم تجرّب بعد",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    QaProgressBar(
                        pct = a?.pct ?: 0,
                        color = if ((a?.pct ?: 0) >= 65) qa.ok else qa.bad
                    )
                }
            }
        }
    }
}
