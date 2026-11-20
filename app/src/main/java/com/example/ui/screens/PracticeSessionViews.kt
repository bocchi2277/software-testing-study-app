package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PreparedQuestion
import com.example.data.SyllabusData
import com.example.model.ChapterBreakdown
import com.example.ui.QaViewModel
import com.example.ui.components.ConfirmActionDialog
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.QaProgressBar
import com.example.ui.components.SectionCard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun PracticeSessionView(
    qs: List<PreparedQuestion>,
    title: String,
    viewModel: QaViewModel,
    onExit: () -> Unit
) {
    BackHandler { onExit() }

    val qa = LocalQaColors.current
    var idx by remember { mutableIntStateOf(0) }
    var selectedDisplayIdx by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    val wrongList = remember { mutableStateListOf<PreparedQuestion>() }

    val q = qs.getOrNull(idx)
    if (q == null) {
        val pct = if (qs.isNotEmpty()) ((score.toDouble() / qs.size) * 100.0).roundToInt() else 0
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PageHeader(title = "النتيجة", sub = title)
                SectionCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$score/${qs.size}",
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text("$pct%", color = qa.muted)
                    }
                    if (wrongList.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text("أسئلة أخطأت فيها:", fontWeight = FontWeight.Bold)
                        wrongList.forEach { w ->
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = qa.border)
                            Text(w.question.q, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(4.dp))
                            LtrContent {
                                Text("✓ ${w.question.options[0]}", color = qa.ok, fontWeight = FontWeight.Medium)
                            }
                            Text(w.question.exp, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                        Text("العودة")
                    }
                }
            }
        }
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = title,
                sub = "سؤال ${idx + 1} من ${qs.size}",
                trailing = {
                    OutlinedButton(onClick = onExit) { Text("إنهاء") }
                }
            )
            QaProgressBar(pct = ((idx.toDouble() / qs.size) * 100.0).roundToInt())
        }

        item {
            SectionCard {
                Text(q.question.q, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    q.order.forEachIndexed { displayIdx, origIdx ->
                        val isAnswered = selectedDisplayIdx != null
                        val isCorrectOption = displayIdx == q.correctIdx
                        val isChosenWrong = selectedDisplayIdx == displayIdx && !isCorrectOption
                        val bg = when {
                            isAnswered && isCorrectOption -> qa.okBg
                            isChosenWrong -> qa.badBg
                            else -> MaterialTheme.colorScheme.background
                        }
                        val border = when {
                            isAnswered && isCorrectOption -> qa.ok
                            isChosenWrong -> qa.bad
                            else -> qa.border
                        }
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("practice_option_$displayIdx")
                                .clickable(enabled = !isAnswered) {
                                    selectedDisplayIdx = displayIdx
                                    val ok = displayIdx == q.correctIdx
                                    viewModel.recordAttempt(q.question.id, ok, "practice")
                                    if (ok) score++ else wrongList.add(q)
                                },
                            shape = RoundedCornerShape(10.dp),
                            color = bg,
                            border = BorderStroke(1.dp, border)
                        ) {
                            LtrContent {
                                Text(
                                    text = q.question.options[origIdx],
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                if (selectedDisplayIdx != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (selectedDisplayIdx == q.correctIdx) "✅ صحيح" else "❌ إجابة خاطئة",
                                fontWeight = FontWeight.Bold,
                                color = if (selectedDisplayIdx == q.correctIdx) qa.ok else qa.bad
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(q.question.exp, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            idx++
                            selectedDisplayIdx = null
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("practice_next_button")
                    ) {
                        Text(if (idx + 1 == qs.size) "عرض النتيجة" else "التالي")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExamSessionView(
    qs: List<PreparedQuestion>,
    minutes: Int,
    viewModel: QaViewModel,
    onExit: () -> Unit
) {
    val qa = LocalQaColors.current
    val answers = remember(qs) { mutableStateListOf<Int?>().apply { repeat(qs.size) { add(null) } } }
    val flags = remember(qs) { mutableStateMapOf<Int, Boolean>() }
    var idx by remember { mutableIntStateOf(0) }
    var leftSec by remember { mutableIntStateOf(minutes * 60) }
    var done by remember { mutableStateOf(false) }
    var showConfirmSubmit by remember { mutableStateOf(false) }
    var showConfirmCancel by remember { mutableStateOf(false) }
    var elapsedSec by remember { mutableIntStateOf(0) }
    val startAtMs = remember { System.currentTimeMillis() }

    fun submitExam() {
        if (done) return
        val used = ((System.currentTimeMillis() - startAtMs) / 1000.0).roundToInt().coerceIn(0, minutes * 60)
        elapsedSec = used
        var score = 0
        val breakdownCounts = mutableMapOf<String, Pair<Int, Int>>()
        val attemptItems = mutableListOf<Pair<String, Boolean>>()
        qs.forEachIndexed { k, pq ->
            val ok = answers[k] == pq.correctIdx
            if (ok) score++
            val key = pq.question.ch.toString()
            val cur = breakdownCounts[key] ?: (0 to 0)
            breakdownCounts[key] = (cur.first + if (ok) 1 else 0) to (cur.second + 1)
            attemptItems += pq.question.id to ok
        }
        val breakdown = breakdownCounts.mapValues { (_, pair) -> ChapterBreakdown(pair.first, pair.second) }
        viewModel.recordAttempts(attemptItems, "exam")
        viewModel.saveExam(score, qs.size, used, breakdown)
        done = true
    }

    BackHandler {
        if (done) onExit() else showConfirmCancel = true
    }

    if (showConfirmCancel) {
        ConfirmActionDialog(
            title = "إلغاء المحاكاة",
            message = "سيتم إلغاء المحاكاة بدون حفظ نتيجتك. هل تريد الخروج؟",
            confirmText = "إلغاء المحاكاة",
            onConfirm = onExit,
            onDismiss = { showConfirmCancel = false }
        )
    }

    if (showConfirmSubmit) {
        val unanswered = answers.count { it == null }
        ConfirmActionDialog(
            title = "إنهاء وتسليم المحاكاة",
            message = if (unanswered > 0) "لديك $unanswered سؤالاً بلا إجابة. هل تريد التسليم الآن؟" else "هل تريد تسليم المحاكاة الآن؟",
            confirmText = "نعم، سلّم",
            isDestructive = false,
            onConfirm = { submitExam() },
            onDismiss = { showConfirmSubmit = false }
        )
    }

    LaunchedEffect(done) {
        if (done) return@LaunchedEffect
        val endMs = startAtMs + minutes * 60_000L
        while (!done) {
            delay(500)
            val l = ((endMs - System.currentTimeMillis()) / 1000.0).roundToInt().coerceAtLeast(0)
            leftSec = l
            if (l <= 0) {
                submitExam()
                break
            }
        }
    }

    if (done) {
        val totalScore = qs.indices.count { answers[it] == qs[it].correctIdx }
        val pct = if (qs.isNotEmpty()) ((totalScore.toDouble() / qs.size) * 100.0).roundToInt() else 0
        val pass = pct >= 65
        val byCh = mutableMapOf<Int, Pair<Int, Int>>()
        qs.forEachIndexed { k, pq ->
            val cur = byCh[pq.question.ch] ?: (0 to 0)
            byCh[pq.question.ch] = (cur.first + if (answers[k] == pq.correctIdx) 1 else 0) to (cur.second + 1)
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PageHeader(title = "نتيجة المحاكاة")
                SectionCard(borderColor = if (pass) qa.ok else qa.bad) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$totalScore/${qs.size}",
                            style = MaterialTheme.typography.displayLarge,
                            color = if (pass) qa.ok else qa.bad
                        )
                        Text(
                            text = "$pct% — ${if (pass) "ناجح ✅ (الحد 65%)" else "لم تبلغ حد النجاح 65% ❌"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("الزمن المستغرق: ${DateUtils.fmtTime(elapsedSec)}", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                        if (pass && pct < 80) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "نصيحة: كرر المحاكاة حتى تتجاوز 80% باستمرار قبل الحجز.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = qa.warn
                            )
                        }
                    }
                }
            }

            item {
                SectionCard {
                    Text("الأداء حسب الفصل", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(10.dp))
                    SyllabusData.CHAPTERS.forEach { c ->
                        val r = byCh[c.id]
                        if (r != null) {
                            val p = ((r.first.toDouble() / r.second) * 100.0).roundToInt()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(SyllabusData.chapterLabel(c), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                                Text("${r.first}/${r.second}", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            QaProgressBar(pct = p, color = if (p >= 65) qa.ok else qa.bad)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            item {
                Text("مراجعة الإجابات", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            itemsIndexed(qs) { k, pq ->
                val ans = answers[k]
                val ok = ans == pq.correctIdx
                SectionCard {
                    Text(
                        text = "${k + 1}. ${if (ok) "✅" else if (ans == null) "⚪" else "❌"} ${pq.question.q}",
                        fontWeight = FontWeight.Medium
                    )
                    if (!ok && ans != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LtrContent {
                            Text("✗ ${pq.question.options[pq.order[ans]]}", color = qa.bad)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LtrContent {
                        Text("✓ ${pq.question.options[0]}", color = qa.ok)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(pq.question.exp, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                }
            }

            item {
                Button(onClick = onExit, modifier = Modifier.fillMaxWidth()) {
                    Text("العودة")
                }
            }
        }
        return
    }

    val q = qs[idx]
    val answeredCount = answers.count { it != null }
    val lowTime = leftSec < 300

    Column(modifier = Modifier.fillMaxSize()) {
        // Sticky header
        Surface(
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, qa.border)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("سؤال ${idx + 1}/${qs.size} · أجبت $answeredCount", style = MaterialTheme.typography.bodyMedium)
                LtrContent {
                    Text(
                        text = "⏱ ${DateUtils.fmtTime(leftSec)}",
                        fontFamily = JetBrainsMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = if (lowTime) qa.bad else MaterialTheme.colorScheme.onBackground
                    )
                }
                Button(
                    onClick = { showConfirmSubmit = true },
                    modifier = Modifier.testTag("exam_submit_button")
                ) {
                    Text("تسليم")
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("الفصل ${q.question.ch}", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        QaFilterChip(
                            text = "🚩 علّمه للمراجعة",
                            selected = flags[idx] == true,
                            onClick = { flags[idx] = !(flags[idx] ?: false) }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(q.question.q, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        q.order.forEachIndexed { d, orig ->
                            val isSelected = answers[idx] == d
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { answers[idx] = d },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else qa.border
                                )
                            ) {
                                LtrContent {
                                    Text(
                                        text = q.question.options[orig],
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(14.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(enabled = idx > 0, onClick = { idx-- }) {
                            Text("→ السابق")
                        }
                        OutlinedButton(enabled = idx < qs.size - 1, onClick = { idx++ }) {
                            Text("التالي ←")
                        }
                    }
                }
            }

            // Question Map
            item {
                SectionCard {
                    Text("خريطة الأسئلة", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    LtrContent {
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            qs.indices.forEach { k ->
                                val isCur = k == idx
                                val isAns = answers[k] != null
                                val isFlagged = flags[k] == true
                                val bg = when {
                                    isCur -> MaterialTheme.colorScheme.primary
                                    isAns -> qa.okBg
                                    else -> MaterialTheme.colorScheme.surface
                                }
                                val fg = when {
                                    isCur -> MaterialTheme.colorScheme.onPrimary
                                    isAns -> qa.ok
                                    else -> MaterialTheme.colorScheme.onSurface
                                }
                                val border = when {
                                    isFlagged -> qa.warn
                                    isCur -> MaterialTheme.colorScheme.primary
                                    isAns -> qa.ok
                                    else -> qa.border
                                }
                                Surface(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clickable { idx = k },
                                    shape = RoundedCornerShape(8.dp),
                                    color = bg,
                                    border = BorderStroke(if (isFlagged) 2.dp else 1.dp, border)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${k + 1}",
                                            color = fg,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = { showConfirmCancel = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("إلغاء المحاكاة بدون حفظ", color = qa.bad)
                }
            }
        }
    }
}
