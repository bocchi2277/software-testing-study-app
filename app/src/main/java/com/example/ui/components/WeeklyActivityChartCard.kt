package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.model.AppState
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils
import com.example.util.WeeklyDayActivity
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

private enum class WeeklyChartType(val label: String, val icon: ImageVector) {
    BARS("أعمدة", Icons.Default.BarChart),
    TREND("منحنى", Icons.Default.ShowChart)
}

@Composable
fun WeeklyActivityChartCard(
    state: AppState,
    modifier: Modifier = Modifier,
    onStartFocusClick: (() -> Unit)? = null
) {
    val qa = LocalQaColors.current
    val activity = remember(state.sessions) { StatsUtils.weeklyActivity(state, daysCount = 7) }
    val dailyGoalMin = (state.profile.dailyHours * 60.0).roundToInt().coerceAtLeast(30)

    var chartType by remember { mutableStateOf(WeeklyChartType.BARS) }
    var selectedIdx by remember(activity) {
        val defaultIdx = (activity.indexOfLast { it.totalMin > 0 })
            .takeIf { it >= 0 } ?: (activity.size - 1).coerceAtLeast(0)
        mutableIntStateOf(defaultIdx)
    }

    val safeSelectedIdx = selectedIdx.coerceIn(0, (activity.size - 1).coerceAtLeast(0))
    val selectedDay = activity.getOrNull(safeSelectedIdx)

    val weekTotalMin = activity.sumOf { it.totalMin }
    val weekFocusMin = activity.sumOf { it.focusMin }
    val weekManualMin = activity.sumOf { it.manualMin }
    val weekFocusSessions = activity.sumOf { it.focusSessions }
    val dailyAvgMin = (weekTotalMin / 7.0).roundToInt()
    val goalDaysCount = activity.count { it.totalMin >= dailyGoalMin }

    val peakDayMin = activity.maxOfOrNull { it.totalMin } ?: 0
    val rawMax = max(max(60, peakDayMin), (dailyGoalMin * 1.1).roundToInt())
    val yAxisMax = ((ceil(rawMax / 30.0)) * 30.0).toInt().coerceAtLeast(60)

    SectionCard(modifier = modifier.testTag("weekly_activity_chart_card")) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "📊 النشاط الأسبوعي (Pomodoro)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "توزيع دقائق التركيز والدراسة خلال آخر 7 أيام",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                WeeklyChartType.entries.forEach { mode ->
                    WeeklyModeTogglePill(
                        label = mode.label,
                        icon = mode.icon,
                        selected = chartType == mode,
                        onClick = { chartType = mode },
                        testTag = "weekly_chart_mode_${mode.name.lowercase()}"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Summary KPI Strip
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WeeklySummaryPill(
                value = DateUtils.fmtMinutes(weekTotalMin),
                sub = if (weekFocusSessions > 0) "🍅 $weekFocusSessions جلسة" else "هذا الأسبوع",
                modifier = Modifier.weight(1f)
            )
            WeeklySummaryPill(
                value = DateUtils.fmtMinutes(dailyAvgMin),
                sub = "المعدل اليومي",
                modifier = Modifier.weight(1f)
            )
            WeeklySummaryPill(
                value = "$goalDaysCount / 7",
                sub = "أيام حققت الهدف",
                highlight = goalDaysCount > 0,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Chart Canvas
        AnimatedContent(targetState = chartType, label = "weeklyChartType") { mode ->
            WeeklyActivityCanvas(
                activity = activity,
                selectedIdx = safeSelectedIdx,
                yAxisMax = yAxisMax,
                dailyGoalMin = dailyGoalMin,
                isTrendMode = mode == WeeklyChartType.TREND,
                onSelectDay = { idx -> selectedIdx = idx }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Legend Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            WeeklyLegendDot(color = MaterialTheme.colorScheme.primary, label = "تركيز Pomodoro (${DateUtils.fmtMinutes(weekFocusMin)})")
            WeeklyLegendDot(color = qa.ok, label = "دراسة خارجية (${DateUtils.fmtMinutes(weekManualMin)})")
            WeeklyLegendDot(color = qa.warn, label = "الهدف: ${DateUtils.fmtMinutes(dailyGoalMin)}")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Day Interactive Tooltip Panel
        if (selectedDay != null) {
            val metGoal = selectedDay.totalMin >= dailyGoalMin
            val remainingToGoal = (dailyGoalMin - selectedDay.totalMin).coerceAtLeast(0)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, if (metGoal) qa.ok.copy(alpha = 0.6f) else qa.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("weekly_selected_day_panel")
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = DateUtils.dayLabel(selectedDay.day),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = selectedDay.day,
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = JetBrainsMono,
                                color = qa.muted
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = if (metGoal) qa.okBg else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (metGoal) qa.ok else qa.border)
                        ) {
                            Text(
                                text = if (metGoal) "✅ تحقق الهدف" else "متبقي ${DateUtils.fmtMinutes(remainingToGoal)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (metGoal) qa.ok else qa.muted,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedDay.totalMin > 0) {
                                "الإجمالي: ${DateUtils.fmtMinutes(selectedDay.totalMin)} (🍅 ${selectedDay.focusMin}د · 📘 ${selectedDay.manualMin}د)"
                            } else {
                                "لم تُسجَّل جلسات دراسة في هذا اليوم"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = if (selectedDay.totalMin > 0) MaterialTheme.colorScheme.onSurface else qa.muted
                        )
                        if (selectedDay.earnedPoints > 0) {
                            Text(
                                text = "+${selectedDay.earnedPoints} XP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (weekTotalMin == 0 && onStartFocusClick != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ابدأ أول جلسة بومودورو ليظهر نشاطك هنا فوراً.",
                                style = MaterialTheme.typography.labelSmall,
                                color = qa.muted,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = onStartFocusClick,
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .testTag("weekly_empty_start_focus")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "بدء جلسة تركيز",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ابدأ الآن")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeeklyActivityCanvas(
    activity: List<WeeklyDayActivity>,
    selectedIdx: Int,
    yAxisMax: Int,
    dailyGoalMin: Int,
    isTrendMode: Boolean,
    onSelectDay: (Int) -> Unit
) {
    val qa = LocalQaColors.current
    val primary = MaterialTheme.colorScheme.primary
    val manualColor = qa.ok
    val goalColor = qa.warn
    val borderColor = qa.border
    val mutedColor = qa.muted
    val onSurface = MaterialTheme.colorScheme.onSurface
    val surfaceColor = MaterialTheme.colorScheme.surface
    val textMeasurer = rememberTextMeasurer()

    val animatedFractions = activity.mapIndexed { idx, day ->
        val frac by animateFloatAsState(
            targetValue = (day.totalMin.toFloat() / yAxisMax.toFloat()).coerceIn(0f, 1f),
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label = "weekDayBar_$idx"
        )
        frac
    }

    LtrContent {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .semantics {
                    contentDescription = "مخطط النشاط الأسبوعي لوقت الدراسة وجلسات بومودورو"
                }
                .testTag("weekly_activity_canvas")
                .pointerInput(activity.size) {
                    detectTapGestures { offset ->
                        if (activity.isEmpty()) return@detectTapGestures
                        val leftPad = 38.dp.toPx()
                        val rightPad = 8.dp.toPx()
                        val plotWidth = (size.width - leftPad - rightPad).coerceAtLeast(1f)
                        val relX = (offset.x - leftPad).coerceIn(0f, plotWidth - 0.01f)
                        val slot = ((relX / plotWidth) * activity.size).toInt().coerceIn(0, activity.size - 1)
                        onSelectDay(slot)
                    }
                }
        ) {
            val leftPad = 38.dp.toPx()
            val rightPad = 8.dp.toPx()
            val topPad = 14.dp.toPx()
            val bottomPad = 26.dp.toPx()
            val plotWidth = (size.width - leftPad - rightPad).coerceAtLeast(1f)
            val plotHeight = (size.height - topPad - bottomPad).coerceAtLeast(1f)
            val slotWidth = plotWidth / activity.size.coerceAtLeast(1)

            val axisLabelStyle = TextStyle(
                color = mutedColor,
                fontSize = 10.sp,
                fontFamily = JetBrainsMono
            )
            val dayLabelStyle = TextStyle(
                color = mutedColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            val selectedDayStyle = TextStyle(
                color = primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            // Y-axis gridlines & labels (0, mid, max)
            val ticks = listOf(0, yAxisMax / 2, yAxisMax)
            ticks.forEach { tick ->
                val y = topPad + plotHeight * (1f - tick.toFloat() / yAxisMax.toFloat())
                drawLine(
                    color = borderColor.copy(alpha = 0.65f),
                    start = Offset(leftPad, y),
                    end = Offset(size.width - rightPad, y),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = if (tick == 0) null else PathEffect.dashPathEffect(floatArrayOf(6f, 6f))
                )
                val tickText = textMeasurer.measure("${tick}m", style = axisLabelStyle)
                drawText(
                    textLayoutResult = tickText,
                    topLeft = Offset(
                        x = (leftPad - tickText.size.width - 6.dp.toPx()).coerceAtLeast(0f),
                        y = (y - tickText.size.height / 2f).coerceIn(0f, size.height - tickText.size.height)
                    )
                )
            }

            // Daily Goal Reference Line
            val goalY = topPad + plotHeight * (1f - (dailyGoalMin.toFloat() / yAxisMax.toFloat()).coerceIn(0f, 1f))
            drawLine(
                color = goalColor.copy(alpha = 0.85f),
                start = Offset(leftPad, goalY),
                end = Offset(size.width - rightPad, goalY),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f))
            )

            if (!isTrendMode) {
                // Stacked Bar Chart Mode
                activity.forEachIndexed { index, item ->
                    val centerX = leftPad + slotWidth * (index + 0.5f)
                    val barWidth = (slotWidth * 0.54f).coerceIn(14.dp.toPx(), 34.dp.toPx())
                    val totalFrac = animatedFractions[index]
                    val barHeight = if (item.totalMin > 0) max(6.dp.toPx(), plotHeight * totalFrac) else 3.dp.toPx()
                    val barTop = topPad + plotHeight - barHeight
                    val barLeft = centerX - barWidth / 2f

                    if (index == selectedIdx) {
                        drawRoundRect(
                            color = primary.copy(alpha = 0.10f),
                            topLeft = Offset(centerX - slotWidth * 0.44f, topPad - 4.dp.toPx()),
                            size = Size(slotWidth * 0.88f, plotHeight + 8.dp.toPx()),
                            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                        )
                    }

                    if (item.totalMin == 0) {
                        drawRoundRect(
                            color = borderColor.copy(alpha = 0.5f),
                            topLeft = Offset(barLeft, barTop),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    } else {
                        val focusRatio = item.focusMin.toFloat() / item.totalMin.toFloat()
                        val focusH = barHeight * focusRatio
                        val manualH = barHeight - focusH

                        // Top segment (manual) if > 0, else full rounded bar for focus
                        if (manualH > 1f) {
                            drawRoundRect(
                                color = manualColor,
                                topLeft = Offset(barLeft, barTop),
                                size = Size(barWidth, manualH + if (focusH > 1f) 3.dp.toPx() else 0f),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )
                        }
                        if (focusH > 1f) {
                            val focusTop = barTop + manualH
                            drawRoundRect(
                                color = primary,
                                topLeft = Offset(barLeft, focusTop),
                                size = Size(barWidth, focusH),
                                cornerRadius = if (manualH <= 1f) CornerRadius(6.dp.toPx(), 6.dp.toPx()) else CornerRadius(3.dp.toPx(), 3.dp.toPx())
                            )
                        }

                        if (index == selectedIdx || item.totalMin > 0) {
                            val valText = textMeasurer.measure(
                                "${item.totalMin}",
                                style = TextStyle(
                                    color = if (index == selectedIdx) onSurface else mutedColor,
                                    fontSize = 10.sp,
                                    fontWeight = if (index == selectedIdx) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = JetBrainsMono
                                )
                            )
                            drawText(
                                textLayoutResult = valText,
                                topLeft = Offset(
                                    x = centerX - valText.size.width / 2f,
                                    y = (barTop - valText.size.height - 2.dp.toPx()).coerceAtLeast(0f)
                                )
                            )
                        }
                    }

                    // Day Label
                    val dayText = textMeasurer.measure(
                        text = DateUtils.dayLabel(item.day),
                        style = if (index == selectedIdx) selectedDayStyle else dayLabelStyle
                    )
                    drawText(
                        textLayoutResult = dayText,
                        topLeft = Offset(
                            x = centerX - dayText.size.width / 2f,
                            y = topPad + plotHeight + 6.dp.toPx()
                        )
                    )
                }
            } else {
                // Smooth Area & Line Chart Mode (Recharts AreaChart style)
                val points = activity.mapIndexed { index, _ ->
                    val cx = leftPad + slotWidth * (index + 0.5f)
                    val cy = topPad + plotHeight * (1f - animatedFractions[index])
                    Offset(cx, cy)
                }

                if (points.isNotEmpty()) {
                    val linePath = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 1 until points.size) {
                            val prev = points[i - 1]
                            val cur = points[i]
                            val midX = (prev.x + cur.x) / 2f
                            cubicTo(midX, prev.y, midX, cur.y, cur.x, cur.y)
                        }
                    }
                    val areaPath = Path().apply {
                        addPath(linePath)
                        lineTo(points.last().x, topPad + plotHeight)
                        lineTo(points.first().x, topPad + plotHeight)
                        close()
                    }

                    drawPath(
                        path = areaPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(primary.copy(alpha = 0.28f), Color.Transparent),
                            startY = topPad,
                            endY = topPad + plotHeight
                        )
                    )
                    drawPath(
                        path = linePath,
                        color = primary,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                activity.forEachIndexed { index, item ->
                    val pt = points[index]
                    if (index == selectedIdx) {
                        drawLine(
                            color = primary.copy(alpha = 0.35f),
                            start = Offset(pt.x, topPad),
                            end = Offset(pt.x, topPad + plotHeight),
                            strokeWidth = 1.5.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
                        )
                        drawCircle(
                            color = primary.copy(alpha = 0.22f),
                            radius = 10.dp.toPx(),
                            center = pt
                        )
                    }
                    drawCircle(
                        color = if (index == selectedIdx) primary else surfaceColor,
                        radius = if (index == selectedIdx) 5.5.dp.toPx() else 4.dp.toPx(),
                        center = pt
                    )
                    drawCircle(
                        color = primary,
                        radius = if (index == selectedIdx) 5.5.dp.toPx() else 4.dp.toPx(),
                        center = pt,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    if (index == selectedIdx || item.totalMin > 0) {
                        val valText = textMeasurer.measure(
                            "${item.totalMin}",
                            style = TextStyle(
                                color = if (index == selectedIdx) onSurface else mutedColor,
                                fontSize = 10.sp,
                                fontWeight = if (index == selectedIdx) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = JetBrainsMono
                            )
                        )
                        drawText(
                            textLayoutResult = valText,
                            topLeft = Offset(
                                x = pt.x - valText.size.width / 2f,
                                y = (pt.y - valText.size.height - 6.dp.toPx()).coerceAtLeast(0f)
                            )
                        )
                    }

                    val dayText = textMeasurer.measure(
                        text = DateUtils.dayLabel(item.day),
                        style = if (index == selectedIdx) selectedDayStyle else dayLabelStyle
                    )
                    drawText(
                        textLayoutResult = dayText,
                        topLeft = Offset(
                            x = pt.x - dayText.size.width / 2f,
                            y = topPad + plotHeight + 6.dp.toPx()
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun WeeklyModeTogglePill(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val qa = LocalQaColors.current
    Surface(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(999.dp))
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (selected) MaterialTheme.colorScheme.primary else qa.border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.onPrimary else qa.muted,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun WeeklySummaryPill(
    value: String,
    sub: String,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (highlight) qa.okBg else MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, if (highlight) qa.ok.copy(alpha = 0.5f) else qa.border)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (highlight) qa.ok else MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun WeeklyLegendDot(
    color: Color,
    label: String
) {
    val qa = LocalQaColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = qa.muted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
