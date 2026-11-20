package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.model.TopicStatus
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlin.math.roundToInt

private enum class SyllabusChartMode(val label: String) {
    BARS("الأعمدة"),
    STATUS("الحالات")
}

private data class ModuleVisualMetric(
    val chapter: Chapter,
    val progressPct: Int,
    val accuracyPct: Int,
    val answeredCount: Int,
    val correctCount: Int,
    val doneCount: Int,
    val learningCount: Int,
    val reviewCount: Int,
    val noneCount: Int
)

@Composable
fun SyllabusProgressChartCard(
    state: AppState,
    onOpenTopic: (String) -> Unit,
    onOpenPractice: (Int?, String?) -> Unit,
    modifier: Modifier = Modifier,
    mutatingChapterIds: Set<Int> = emptySet(),
    onToggleModuleComplete: ((chapterId: Int, completed: Boolean) -> Unit)? = null
) {
    val qa = LocalQaColors.current
    var mode by remember { mutableStateOf(SyllabusChartMode.BARS) }
    var selectedChapterId by remember {
        mutableIntStateOf(StatsUtils.nextTopic(state)?.chapter ?: 1)
    }

    val metrics = remember(state.progress, state.attempts) {
        val accMap = StatsUtils.chapterAccuracy(state)
        SyllabusData.CHAPTERS.map { ch ->
            var done = 0
            var learning = 0
            var review = 0
            var none = 0
            ch.topics.forEach { t ->
                when (TopicStatus.fromKey(StatsUtils.topicStatus(state, t.id))) {
                    TopicStatus.DONE -> done++
                    TopicStatus.LEARNING -> learning++
                    TopicStatus.REVIEW -> review++
                    TopicStatus.NONE -> none++
                }
            }
            val acc = accMap[ch.id]
            ModuleVisualMetric(
                chapter = ch,
                progressPct = StatsUtils.chapterProgress(state, ch.id),
                accuracyPct = acc?.pct ?: 0,
                answeredCount = acc?.t ?: 0,
                correctCount = acc?.c ?: 0,
                doneCount = done,
                learningCount = learning,
                reviewCount = review,
                noneCount = none
            )
        }
    }

    val selectedMetric = metrics.find { it.chapter.id == selectedChapterId } ?: metrics.first()
    val overallTopicsDone = metrics.sumOf { it.doneCount }
    val totalTopics = SyllabusData.ALL_TOPICS.size
    val totalAnswered = metrics.sumOf { it.answeredCount }

    SectionCard(modifier = modifier.testTag("syllabus_progress_chart_card")) {
        // Header + Mode Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "📊 تقدم وحدات منهج ISTQB",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$overallTopicsDone/$totalTopics موضوعاً مكتملاً عبر ${SyllabusData.CHAPTERS.size} فصول",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                ChartModeTogglePill(
                    label = SyllabusChartMode.BARS.label,
                    icon = Icons.Default.BarChart,
                    selected = mode == SyllabusChartMode.BARS,
                    testTag = "chart_mode_bars",
                    onClick = { mode = SyllabusChartMode.BARS }
                )
                ChartModeTogglePill(
                    label = SyllabusChartMode.STATUS.label,
                    icon = Icons.Default.StackedBarChart,
                    selected = mode == SyllabusChartMode.STATUS,
                    testTag = "chart_mode_breakdown",
                    onClick = { mode = SyllabusChartMode.STATUS }
                )
            }
        }

        if (overallTopicsDone == 0 && totalAnswered == 0) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ابدأ بدراسة موضوع من الفصل 1 أو أجب عن بضعة أسئلة لترى أعمدة التقدم والدقة ترتفع هنا.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        AnimatedContent(targetState = mode, label = "syllabus_chart_mode") { activeMode ->
            when (activeMode) {
                SyllabusChartMode.BARS -> {
                    GroupedSyllabusCanvasChart(
                        metrics = metrics,
                        selectedChapterId = selectedMetric.chapter.id,
                        onSelectChapter = { selectedChapterId = it }
                    )
                }

                SyllabusChartMode.STATUS -> {
                    StackedModuleStatusBreakdown(
                        metrics = metrics,
                        selectedChapterId = selectedMetric.chapter.id,
                        onSelectChapter = { selectedChapterId = it }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = qa.border)
        Spacer(modifier = Modifier.height(12.dp))

        // Selected Module Drill-down Details
        SelectedModuleDetailStrip(
            metric = selectedMetric,
            isMutating = selectedMetric.chapter.id in mutatingChapterIds,
            onToggleComplete = onToggleModuleComplete?.let { callback ->
                {
                    val isComplete = selectedMetric.doneCount == selectedMetric.chapter.topics.size
                    callback(selectedMetric.chapter.id, !isComplete)
                }
            },
            onOpenTopic = {
                val targetTopic = selectedMetric.chapter.topics.firstOrNull {
                    StatsUtils.topicStatus(state, it.id) != "done"
                } ?: selectedMetric.chapter.topics.firstOrNull()
                if (targetTopic != null) {
                    onOpenTopic(targetTopic.id)
                }
            },
            onOpenPractice = {
                onOpenPractice(selectedMetric.chapter.id, null)
            }
        )
    }
}

@Composable
private fun ChartModeTogglePill(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val qa = LocalQaColors.current
    val bg = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
    val fg = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    val border = if (selected) MaterialTheme.colorScheme.primary else qa.border

    Surface(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .testTag(testTag)
            .clip(RoundedCornerShape(999.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = bg,
        border = BorderStroke(1.dp, border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = fg,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = label,
                color = fg,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun GroupedSyllabusCanvasChart(
    metrics: List<ModuleVisualMetric>,
    selectedChapterId: Int,
    onSelectChapter: (Int) -> Unit
) {
    val qa = LocalQaColors.current
    val primary = MaterialTheme.colorScheme.primary
    val primarySoft = primary.copy(alpha = 0.11f)
    val gridColor = qa.border
    val passLineColor = qa.warn
    val mutedColor = qa.muted
    val textMeasurer = rememberTextMeasurer()

    val animatedProgress = metrics.map { metric ->
        val anim by animateFloatAsState(
            targetValue = metric.progressPct.coerceIn(0, 100) / 100f,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "ch_prog_${metric.chapter.id}"
        )
        anim
    }

    val animatedAccuracy = metrics.map { metric ->
        val target = if (metric.answeredCount > 0) metric.accuracyPct.coerceIn(0, 100) / 100f else 0f
        val anim by animateFloatAsState(
            targetValue = target,
            animationSpec = tween(durationMillis = 450),
            label = "ch_acc_${metric.chapter.id}"
        )
        anim
    }

    val chartSummaryDescription = remember(metrics) {
        metrics.joinToString("، ") {
            "الفصل ${it.chapter.id}: إنجاز ${it.progressPct}% ودقة ${it.accuracyPct}%"
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LtrContent {
            Column(modifier = Modifier.fillMaxWidth()) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp)
                        .semantics { contentDescription = chartSummaryDescription }
                        .pointerInput(metrics.size) {
                            detectTapGestures { offset ->
                                val axisLeftPx = 38.dp.toPx()
                                val usableWidth = (size.width - axisLeftPx).coerceAtLeast(1f)
                                val relX = (offset.x - axisLeftPx).coerceIn(0f, usableWidth - 1f)
                                val idx = ((relX / usableWidth) * metrics.size)
                                    .toInt()
                                    .coerceIn(0, metrics.lastIndex)
                                onSelectChapter(metrics[idx].chapter.id)
                            }
                        }
                ) {
                    val axisLeftPx = 38.dp.toPx()
                    val topPadPx = 14.dp.toPx()
                    val bottomPadPx = 8.dp.toPx()
                    val chartH = (size.height - topPadPx - bottomPadPx).coerceAtLeast(1f)
                    val plotW = (size.width - axisLeftPx).coerceAtLeast(1f)

                    fun yForPct(pct: Float): Float = topPadPx + chartH * (1f - (pct / 100f))

                    val axisLabelStyle = TextStyle(
                        color = mutedColor,
                        fontSize = 10.sp,
                        fontFamily = JetBrainsMono
                    )

                    // Horizontal baseline gridlines (0%, 50%, 100%)
                    listOf(0f, 50f, 100f).forEach { pct ->
                        val y = yForPct(pct)
                        drawLine(
                            color = gridColor.copy(alpha = if (pct == 0f) 0.9f else 0.55f),
                            start = Offset(axisLeftPx, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        val label = "${pct.toInt()}%"
                        val measured = textMeasurer.measure(label, style = axisLabelStyle)
                        drawText(
                            textLayoutResult = measured,
                            topLeft = Offset(
                                x = (axisLeftPx - measured.size.width - 6.dp.toPx()).coerceAtLeast(0f),
                                y = (y - measured.size.height / 2f).coerceIn(0f, size.height - measured.size.height)
                            )
                        )
                    }

                    // 65% ISTQB pass threshold dashed line
                    val passY = yForPct(65f)
                    drawLine(
                        color = passLineColor.copy(alpha = 0.85f),
                        start = Offset(axisLeftPx, passY),
                        end = Offset(size.width, passY),
                        strokeWidth = 1.3.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 7f), 0f)
                    )
                    val passLabel = textMeasurer.measure(
                        "65%",
                        style = axisLabelStyle.copy(color = passLineColor, fontWeight = FontWeight.Bold)
                    )
                    drawText(
                        textLayoutResult = passLabel,
                        topLeft = Offset(
                            x = (axisLeftPx - passLabel.size.width - 6.dp.toPx()).coerceAtLeast(0f),
                            y = (passY - passLabel.size.height / 2f).coerceIn(0f, size.height - passLabel.size.height)
                        )
                    )

                    val slotW = plotW / metrics.size
                    val barPairGap = 4.dp.toPx()
                    val singleBarW = ((slotW * 0.62f - barPairGap) / 2f).coerceIn(8.dp.toPx(), 18.dp.toPx())
                    val cornerRadius = CornerRadius(5.dp.toPx(), 5.dp.toPx())
                    val baseY = yForPct(0f)

                    metrics.forEachIndexed { idx, metric ->
                        val slotLeft = axisLeftPx + idx * slotW
                        val slotCenter = slotLeft + slotW / 2f
                        val isSelected = metric.chapter.id == selectedChapterId

                        if (isSelected) {
                            drawRoundRect(
                                color = primarySoft,
                                topLeft = Offset(slotLeft + 3.dp.toPx(), topPadPx - 4.dp.toPx()),
                                size = Size(slotW - 6.dp.toPx(), chartH + 8.dp.toPx()),
                                cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                            )
                        }

                        val progFrac = animatedProgress[idx]
                        val progH = maxOf(if (metric.progressPct > 0) 6.dp.toPx() else 3.dp.toPx(), chartH * progFrac)
                        val progLeft = slotCenter - barPairGap / 2f - singleBarW
                        drawRoundRect(
                            color = if (metric.progressPct > 0) primary else gridColor.copy(alpha = 0.55f),
                            topLeft = Offset(progLeft, baseY - progH),
                            size = Size(singleBarW, progH),
                            cornerRadius = cornerRadius
                        )

                        val accFrac = animatedAccuracy[idx]
                        val accH = maxOf(if (metric.answeredCount > 0) 6.dp.toPx() else 3.dp.toPx(), chartH * accFrac)
                        val accLeft = slotCenter + barPairGap / 2f
                        val accColor = when {
                            metric.answeredCount == 0 -> gridColor.copy(alpha = 0.45f)
                            metric.accuracyPct >= 65 -> qa.ok
                            metric.accuracyPct >= 45 -> qa.warn
                            else -> qa.bad
                        }
                        drawRoundRect(
                            color = accColor,
                            topLeft = Offset(accLeft, baseY - accH),
                            size = Size(singleBarW, accH),
                            cornerRadius = cornerRadius
                        )

                        if (isSelected) {
                            drawCircle(
                                color = primary,
                                radius = 3.dp.toPx(),
                                center = Offset(slotCenter, topPadPx - 8.dp.toPx())
                            )
                        }
                    }
                }

                // Tappable Chapter Pills aligned under each slot
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 38.dp, top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    metrics.forEach { metric ->
                        val selected = metric.chapter.id == selectedChapterId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .testTag("chart_chapter_tab_${metric.chapter.id}")
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selected) MaterialTheme.colorScheme.primaryContainer
                                    else Color.Transparent
                                )
                                .clickable { onSelectChapter(metric.chapter.id) }
                                .padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Ch${metric.chapter.id}",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 11.sp,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else qa.muted
                                )
                                Text(
                                    text = "${metric.progressPct}%",
                                    fontFamily = JetBrainsMono,
                                    fontSize = 10.sp,
                                    color = if (selected) MaterialTheme.colorScheme.primary else qa.muted
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Compact Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendChip(color = MaterialTheme.colorScheme.primary, label = "إتمام المنهج")
            LegendChip(color = qa.ok, label = "دقة ≥ 65%")
            LegendChip(color = qa.warn, label = "65% حد النجاح", isDashed = true)
        }
    }
}

@Composable
private fun StackedModuleStatusBreakdown(
    metrics: List<ModuleVisualMetric>,
    selectedChapterId: Int,
    onSelectChapter: (Int) -> Unit
) {
    val qa = LocalQaColors.current
    val fallbackBg = qa.border.copy(alpha = 0.45f)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        metrics.forEach { metric ->
            val selected = metric.chapter.id == selectedChapterId
            val total = metric.chapter.topics.size.coerceAtLeast(1).toFloat()

            val doneFrac by animateFloatAsState(
                targetValue = metric.doneCount / total,
                animationSpec = tween(350),
                label = "done_frac_${metric.chapter.id}"
            )
            val learningFrac by animateFloatAsState(
                targetValue = metric.learningCount / total,
                animationSpec = tween(350),
                label = "learn_frac_${metric.chapter.id}"
            )
            val reviewFrac by animateFloatAsState(
                targetValue = metric.reviewCount / total,
                animationSpec = tween(350),
                label = "rev_frac_${metric.chapter.id}"
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("chart_chapter_tab_${metric.chapter.id}")
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onSelectChapter(metric.chapter.id) },
                shape = RoundedCornerShape(10.dp),
                color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                else MaterialTheme.colorScheme.background,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selected) MaterialTheme.colorScheme.primary else qa.border
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "ف${metric.chapter.id}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                            Text(
                                text = metric.chapter.ar,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${metric.doneCount}/${metric.chapter.topics.size} (${metric.progressPct}%)",
                            fontFamily = JetBrainsMono,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (metric.progressPct >= 100) qa.ok else MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(7.dp))

                    LtrContent {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(9.dp)
                                .clip(CircleShape)
                        ) {
                            drawRect(color = fallbackBg)
                            var curX = 0f
                            val segments = listOf(
                                doneFrac to qa.ok,
                                learningFrac to qa.warn,
                                reviewFrac to qa.bad
                            )
                            segments.forEach { (frac, color) ->
                                val w = (size.width * frac).coerceIn(0f, size.width - curX)
                                if (w > 0.5f) {
                                    drawRect(
                                        color = color,
                                        topLeft = Offset(curX, 0f),
                                        size = Size(w, size.height)
                                    )
                                    curX += w
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            LegendChip(color = qa.ok, label = "أتقنته")
            LegendChip(color = qa.warn, label = "أتعلم")
            LegendChip(color = qa.bad, label = "مراجعة")
            LegendChip(color = qa.border, label = "لم أبدأ")
        }
    }
}

@Composable
private fun SelectedModuleDetailStrip(
    metric: ModuleVisualMetric,
    isMutating: Boolean,
    onToggleComplete: (() -> Unit)?,
    onOpenTopic: () -> Unit,
    onOpenPractice: () -> Unit
) {
    val qa = LocalQaColors.current
    val ch = metric.chapter
    val isModuleComplete = ch.topics.isNotEmpty() && metric.doneCount == ch.topics.size
    val hoursText = (((ch.minutes / 60.0) * 10.0).roundToInt() / 10.0).toString()
    val accDisplay = if (metric.answeredCount > 0) {
        "${metric.accuracyPct}% (${metric.correctCount}/${metric.answeredCount})"
    } else {
        "لم تبدأ الأسئلة"
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "الفصل ${ch.id}: ${ch.ar}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "وزن الامتحان: ${ch.examQ} من ${SyllabusData.EXAM_TOTAL_Q} سؤال · ≈ $hoursText ساعة دراسة",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            if (onToggleComplete != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("chart_mark_module_complete_btn")
                        .clip(RoundedCornerShape(999.dp))
                        .clickable(enabled = !isMutating, onClick = onToggleComplete),
                    shape = RoundedCornerShape(999.dp),
                    color = if (isModuleComplete) qa.okBg else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isModuleComplete) qa.ok else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (isMutating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = if (isModuleComplete) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = if (isModuleComplete) "الفصل مكتمل" else "تحديد الفصل كمكتمل",
                                tint = if (isModuleComplete) qa.ok else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = if (isModuleComplete) "مكتمل" else "إكمال الفصل",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isModuleComplete) qa.ok else MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MiniMetricPill(
                label = "إتمام الفصل",
                value = "${metric.doneCount}/${ch.topics.size} (${metric.progressPct}%)",
                valueColor = if (metric.progressPct >= 100) qa.ok else MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            MiniMetricPill(
                label = "دقة الأسئلة",
                value = accDisplay,
                valueColor = when {
                    metric.answeredCount == 0 -> qa.muted
                    metric.accuracyPct >= 65 -> qa.ok
                    else -> qa.bad
                },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onOpenTopic,
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
                    .testTag("chart_open_chapter_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "فتح الفصل",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("دراسة الفصل ${ch.id}")
            }
            Button(
                onClick = onOpenPractice,
                modifier = Modifier
                    .weight(1f)
                    .minimumInteractiveComponentSize()
                    .testTag("chart_practice_chapter_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Quiz,
                    contentDescription = "اختبار الفصل",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("اختبر الفصل ${ch.id}")
            }
        }
    }
}

@Composable
private fun MiniMetricPill(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, qa.border)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LegendChip(
    color: Color,
    label: String,
    isDashed: Boolean = false
) {
    val qa = LocalQaColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (isDashed) {
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(2.dp)
                    .background(color)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = qa.muted
        )
    }
}
