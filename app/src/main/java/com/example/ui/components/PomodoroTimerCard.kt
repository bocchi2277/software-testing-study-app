package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.TimerMode
import com.example.ui.TimerUiState
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils

private data class PomodoroPreset(
    val focusMin: Int,
    val breakMin: Int,
    val label: String
)

private val POMODORO_PRESETS = listOf(
    PomodoroPreset(15, 3, "15 / 3 د"),
    PomodoroPreset(25, 5, "25 / 5 د"),
    PomodoroPreset(45, 10, "45 / 10 د"),
    PomodoroPreset(50, 10, "50 / 10 د")
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PomodoroTimerCard(
    state: AppState,
    timer: TimerUiState,
    isGuest: Boolean,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    onFinishAndLog: () -> Unit,
    onSelectTopic: (String) -> Unit,
    onSelectPreset: (Int, Int) -> Unit,
    onLogManualMinutes: (Int) -> Unit,
    onDismissReward: () -> Unit,
    onOpenProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    var topicMenuExpanded by remember { mutableStateOf(false) }
    var manualMinutesText by remember { mutableStateOf("30") }

    val totalFocusMinutes = remember(state) { StatsUtils.totalFocusMinutes(state) }
    val focusPoints = remember(state) { StatsUtils.focusPoints(state) }
    val focusSessions = remember(state) { StatsUtils.focusSessionCount(state) }
    val level = remember(focusPoints) { StatsUtils.focusLevel(focusPoints) }
    val focusBadges = remember(state) { StatsUtils.focusBadges(state) }
    val allBadges = remember(state) { StatsUtils.allBadges(state) }
    val unlockedAllCount = remember(allBadges) { allBadges.count { it.unlocked } }

    val isFocusMode = timer.mode == TimerMode.FOCUS
    val accentColor = if (isFocusMode) MaterialTheme.colorScheme.primary else qa.ok
    val animatedSweep by animateFloatAsState(
        targetValue = timer.progressFraction * 360f,
        animationSpec = tween(durationMillis = 300),
        label = "pomodoroSweep"
    )
    val selectedTopic = SyllabusData.findTopic(timer.topicId)

    SectionCard(modifier = modifier.testTag("pomodoro_timer_card")) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = "مؤقت بومودورو",
                    tint = accentColor
                )
                Column {
                    Text(
                        text = if (isFocusMode) "جلسة تركيز (Pomodoro)" else "وقت راحة وتجديد",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isGuest) "يُسجَّل محلياً (+2 XP/دقيقة)" else "محفوظ في Firestore (+2 XP/دقيقة)",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(999.dp),
                color = if (isFocusMode) MaterialTheme.colorScheme.primaryContainer else qa.okBg,
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (!isGuest) {
                        Icon(
                            imageVector = Icons.Default.CloudDone,
                            contentDescription = "مزامنة سحابية",
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Text(
                        text = if (isFocusMode) "تركيز ${state.profile.focusMin}د" else "راحة ${state.profile.breakMin}د",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Animated Reward Banner
        AnimatedVisibility(visible = timer.lastRewardMessage != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = qa.okBg,
                border = BorderStroke(1.dp, qa.ok),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("pomodoro_reward_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "مكافأة التركيز",
                            tint = qa.ok
                        )
                        Text(
                            text = timer.lastRewardMessage.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = qa.ok
                        )
                    }
                    IconButton(
                        onClick = onDismissReward,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "إغلاق إشعار المكافأة",
                            tint = qa.ok
                        )
                    }
                }
            }
        }

        // Circular Countdown Dial
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(196.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(196.dp)) {
                    val strokePx = 12.dp.toPx()
                    drawArc(
                        color = qa.border.copy(alpha = 0.65f),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokePx, cap = StrokeCap.Round)
                    )
                    if (animatedSweep > 0.5f) {
                        drawArc(
                            color = accentColor,
                            startAngle = -90f,
                            sweepAngle = animatedSweep,
                            useCenter = false,
                            style = Stroke(width = strokePx, cap = StrokeCap.Round)
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.padding(horizontal = 18.dp)
                ) {
                    Text(
                        text = if (timer.running) {
                            if (isFocusMode) "جلسة نشطة 🎯" else "استراحة نشطة ☕"
                        } else if (timer.elapsedSeconds > 0) {
                            "متوقف مؤقتاً"
                        } else {
                            if (isFocusMode) "جاهز للتركيز" else "وقت الراحة"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                    LtrContent {
                        Text(
                            text = DateUtils.fmtTime(timer.leftSeconds),
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 38.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.testTag("pomodoro_countdown_text")
                        )
                    }
                    if (isFocusMode && timer.elapsedFocusMinutes >= 1) {
                        Text(
                            text = "منجز: ${timer.elapsedFocusMinutes} د (+${StatsUtils.pointsForSession(timer.elapsedFocusMinutes, "focus")} XP)",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.ok,
                            fontWeight = FontWeight.Medium
                        )
                    } else if (selectedTopic != null && isFocusMode) {
                        Text(
                            text = selectedTopic.id,
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                POMODORO_PRESETS.forEach { preset ->
                    val isSelected =
                        state.profile.focusMin == preset.focusMin && state.profile.breakMin == preset.breakMin
                    QaFilterChip(
                        text = preset.label,
                        selected = isSelected,
                        onClick = { if (!timer.running) onSelectPreset(preset.focusMin, preset.breakMin) },
                        testTag = "pomodoro_preset_${preset.focusMin}"
                    )
                }
            }

            // Topic Selector (Focus Mode)
            if (isFocusMode) {
                Spacer(modifier = Modifier.height(10.dp))
                Box {
                    OutlinedButton(
                        onClick = { topicMenuExpanded = true },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("timer_topic_selector")
                    ) {
                        Text(
                            text = selectedTopic?.let { "${it.id} — ${it.ar}" } ?: "بدون موضوع محدد",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "اختر الموضوع")
                    }
                    DropdownMenu(
                        expanded = topicMenuExpanded,
                        onDismissRequest = { topicMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("بدون موضوع محدد") },
                            onClick = {
                                onSelectTopic("")
                                topicMenuExpanded = false
                            }
                        )
                        SyllabusData.ALL_TOPICS.forEach { t ->
                            DropdownMenuItem(
                                text = { Text("${t.id} — ${t.ar}", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                onClick = {
                                    onSelectTopic(t.id)
                                    topicMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Timer Action Controls
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { if (timer.running) onPause() else onStart() },
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("timer_start_pause")
                ) {
                    Icon(
                        imageVector = if (timer.running) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (timer.running) "إيقاف مؤقت" else "ابدأ المؤقت"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when {
                            timer.running -> "إيقاف مؤقت"
                            timer.elapsedSeconds > 0 -> "متابعة"
                            isFocusMode -> "ابدأ التركيز"
                            else -> "ابدأ الراحة"
                        }
                    )
                }

                if (isFocusMode && timer.elapsedFocusMinutes >= 1) {
                    FilledTonalButton(
                        onClick = onFinishAndLog,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("timer_finish_log")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "إنهاء وحفظ الجلسة"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("إنهاء وحفظ (${timer.elapsedFocusMinutes} د)")
                    }
                }

                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("timer_reset")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة تعيين المؤقت",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إعادة")
                }

                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("timer_skip")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "تخطي المرحلة",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isFocusMode) "تخطي للراحة" else "تخطي للتركيز")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Focus Stats, Level & Rewards Strip
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.background,
            border = BorderStroke(1.dp, qa.border),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FocusMetricPill(
                        value = DateUtils.fmtMinutes(totalFocusMinutes),
                        label = "إجمالي التركيز",
                        modifier = Modifier.weight(1f)
                    )
                    FocusMetricPill(
                        value = "$focusPoints XP",
                        label = "نقاط التقدم",
                        highlight = true,
                        modifier = Modifier.weight(1f)
                    )
                    FocusMetricPill(
                        value = "$focusSessions",
                        label = if (timer.completedToday > 0) "جلسات (اليوم ${timer.completedToday})" else "جلسات بومودورو",
                        modifier = Modifier.weight(1f)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${level.emoji} المستوى ${level.level}: ${level.title}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${level.currentPoints} / ${level.nextLevelPoints} XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    QaProgressBar(pct = level.progressPct)
                }

                // Pomodoro Focus Badges
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🏅 شارات التركيز",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            onClick = onOpenProgress,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("pomodoro_open_progress")
                        ) {
                            Text("كل الإنجازات ($unlockedAllCount/${allBadges.size})")
                        }
                    }

                    focusBadges.chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { badge ->
                                val bg = if (badge.unlocked) qa.okBg else MaterialTheme.colorScheme.surface
                                val border = if (badge.unlocked) qa.ok else qa.border
                                Row(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(bg)
                                        .border(1.dp, border, RoundedCornerShape(10.dp))
                                        .alpha(if (badge.unlocked) 1f else 0.65f)
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (badge.unlocked) badge.icon else "🔒",
                                        fontSize = 18.sp
                                    )
                                    Column {
                                        Text(
                                            text = badge.name,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = badge.desc,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = qa.muted,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = qa.border)

        // Manual External Session Logger
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "درست خارج التطبيق؟",
                style = MaterialTheme.typography.bodyMedium,
                color = qa.muted,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = manualMinutesText,
                onValueChange = { manualMinutesText = it.filter { ch -> ch.isDigit() }.take(3) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier
                    .width(84.dp)
                    .testTag("manual_minutes_input"),
                suffix = { Text("د", fontSize = 12.sp) }
            )
            Button(
                onClick = {
                    val mins = manualMinutesText.toIntOrNull() ?: 0
                    if (mins > 0) onLogManualMinutes(mins)
                },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .testTag("manual_session_add")
            ) {
                Text("تسجيل")
            }
        }
    }
}

@Composable
private fun FocusMetricPill(
    value: String,
    label: String,
    highlight: Boolean = false,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = if (highlight) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, if (highlight) MaterialTheme.colorScheme.primary.copy(alpha = 0.35f) else qa.border)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
