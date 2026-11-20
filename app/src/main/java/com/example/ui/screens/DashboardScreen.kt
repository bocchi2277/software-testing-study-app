package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.CareerData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.TimerMode
import com.example.ui.TimerUiState
import com.example.ui.components.DailyStreakCard
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaDatePickerDialog
import com.example.ui.components.QaProgressBar
import com.example.ui.components.ReadinessRing
import com.example.ui.components.SectionCard
import com.example.ui.components.StatCard
import com.example.ui.components.SyllabusProgressChartCard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    state: AppState,
    timer: TimerUiState,
    viewModel: QaViewModel,
    onOpenTopic: (String) -> Unit,
    onOpenCards: () -> Unit,
    onOpenPractice: (Int?, String?) -> Unit
) {
    val qa = LocalQaColors.current
    val mutatingChapterIds by viewModel.mutatingChapterIds.collectAsStateWithLifecycle()
    val checkingInStreak by viewModel.checkingInStreak.collectAsStateWithLifecycle()
    val today = DateUtils.todayStr()
    var showExamDatePicker by remember { mutableStateOf(false) }
    var manualMinutesText by remember { mutableStateOf("30") }
    var newTaskTitle by remember { mutableStateOf("") }
    var topicMenuExpanded by remember { mutableStateOf(false) }

    val minsToday = StatsUtils.minutesByDay(state)[today] ?: 0
    val goalMin = (state.profile.dailyHours * 60.0).roundToInt().coerceAtLeast(30)
    val daysLeft = state.profile.examDate?.let { DateUtils.daysBetween(today, it) }
    val week = StatsUtils.weekMinutes(state)
    val maxW = max(60, week.maxOfOrNull { it.min } ?: 60)
    val weekTotal = week.sumOf { it.min }
    val next = StatsUtils.nextTopic(state)
    val dueCount = StatsUtils.dueCards(state).size
    val mistakeCount = StatsUtils.mistakeIds(state).size
    val weakCh = StatsUtils.weakestChapter(state)
    val todayTasks = state.tasks.filter { it.day == today }
    val tip = CareerData.DAILY_TIPS[(DateUtils.epochDayIndex() % CareerData.DAILY_TIPS.size).toInt().coerceAtLeast(0)]

    if (showExamDatePicker) {
        QaDatePickerDialog(
            initialDateStr = state.profile.examDate,
            allowClear = true,
            onDateSelected = { picked -> viewModel.updateProfile { it.copy(examDate = picked) } },
            onDismiss = { showExamDatePicker = false }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "مرحباً بك في مسارك نحو ISTQB 👋",
                sub = "خطوة صغيرة كل يوم، والشهادة في النهاية."
            )
        }

        // Visual Hero Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, qa.border),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_qa_hero),
                        contentDescription = "مسار QA - التحضير لشهادة ISTQB",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(116.dp),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(
                                text = "💡 نصيحة اليوم",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = tip,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Countdown & Readiness
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (daysLeft == null) "—" else max(daysLeft, 0).toString(),
                            style = MaterialTheme.typography.displayLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (daysLeft != null && daysLeft < 0) "انتهى الموعد" else "يوم حتى الامتحان",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                    OutlinedButton(
                        onClick = { showExamDatePicker = true },
                        modifier = Modifier.testTag("exam_date_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "موعد الامتحان",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(state.profile.examDate ?: "تحديد موعد الامتحان")
                    }
                }
            }
        }

        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val readyPct = StatsUtils.readiness(state)
                    val sylPct = StatsUtils.syllabusPct(state)
                    ReadinessRing(pct = readyPct, size = 104.dp)
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("تقدم المنهج", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text("$sylPct%", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        QaProgressBar(pct = sylPct)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("هدف اليوم", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text("${DateUtils.fmtMinutes(minsToday)} / ${DateUtils.fmtMinutes(goalMin)}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        QaProgressBar(pct = ((minsToday.toDouble() / goalMin) * 100.0).roundToInt())
                    }
                }
            }
        }

        // Daily Study Streak Counter (Firestore-persisted)
        item {
            DailyStreakCard(
                state = state,
                checkingIn = checkingInStreak,
                onCheckInToday = { viewModel.checkInDailyStudy() }
            )
        }

        // ISTQB Syllabus Modules Progress Visualization
        item {
            SyllabusProgressChartCard(
                state = state,
                onOpenTopic = onOpenTopic,
                onOpenPractice = onOpenPractice,
                mutatingChapterIds = mutatingChapterIds,
                onToggleModuleComplete = { chapterId, completed ->
                    viewModel.setChapterCompleted(chapterId, completed)
                }
            )
        }

        // 3 Stat Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    value = "🔥 ${StatsUtils.streak(state)}",
                    label = "أيام متتالية",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = DateUtils.fmtMinutes(weekTotal),
                    label = "هذا الأسبوع",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = state.exams.size.toString(),
                    label = "محاكاة امتحان",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Smart Plan Today
        item {
            SectionCard {
                Text(
                    text = "🎯 خطتك الذكية اليوم",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (next != null) {
                        SmartPlanItem(
                            eyebrow = "1) تعلّم موضوعاً",
                            title = "${next.id} — ${next.ar}",
                            meta = "≈ ${next.minutes} دقيقة",
                            testTag = "smart_next_topic",
                            onClick = { onOpenTopic(next.id) }
                        )
                    }
                    SmartPlanItem(
                        eyebrow = "2) راجع البطاقات",
                        title = if (dueCount > 0) "$dueCount بطاقة مستحقة" else "لا بطاقات مستحقة 🎉",
                        meta = "مراجعة متباعدة (Leitner)",
                        testTag = "smart_due_cards",
                        onClick = onOpenCards
                    )
                    SmartPlanItem(
                        eyebrow = "3) تدرب على نقطة ضعفك",
                        title = "الفصل $weakCh: ${SyllabusData.findChapter(weakCh)?.ar ?: ""}",
                        meta = "10 أسئلة",
                        testTag = "smart_weak_chapter",
                        onClick = { onOpenPractice(weakCh, null) }
                    )
                    SmartPlanItem(
                        eyebrow = "4) راجع أخطاءك",
                        title = if (mistakeCount > 0) "$mistakeCount سؤال أخطأت فيه" else "لا أخطاء مسجلة",
                        meta = "تدريب مركز",
                        testTag = "smart_mistakes",
                        onClick = { onOpenPractice(null, "mistakes") }
                    )
                }
            }
        }

        // Pomodoro Timer Card
        item {
            SectionCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (timer.mode == TimerMode.FOCUS) "🎯 جلسة تركيز (Pomodoro)" else "☕ وقت راحة",
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LtrContent {
                        Text(
                            text = DateUtils.fmtTime(timer.leftSeconds),
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (timer.mode == TimerMode.FOCUS) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val selectedTopic = SyllabusData.findTopic(timer.topicId)
                        Box {
                            OutlinedButton(
                                onClick = { topicMenuExpanded = true },
                                modifier = Modifier.testTag("timer_topic_selector")
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
                                        viewModel.setTimerTopic("")
                                        topicMenuExpanded = false
                                    }
                                )
                                SyllabusData.ALL_TOPICS.forEach { t ->
                                    DropdownMenuItem(
                                        text = { Text("${t.id} — ${t.ar}", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                                        onClick = {
                                            viewModel.setTimerTopic(t.id)
                                            topicMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (timer.running) viewModel.pauseTimer() else viewModel.startTimer() },
                            modifier = Modifier.testTag("timer_start_pause")
                        ) {
                            Text(if (timer.running) "إيقاف مؤقت" else "ابدأ")
                        }
                        OutlinedButton(
                            onClick = { viewModel.resetTimer() },
                            modifier = Modifier.testTag("timer_reset")
                        ) {
                            Text("إعادة")
                        }
                        OutlinedButton(
                            onClick = { viewModel.skipTimer() },
                            modifier = Modifier.testTag("timer_skip")
                        ) {
                            Text(if (timer.mode == TimerMode.FOCUS) "تخطي للراحة" else "تخطي للتركيز")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "جلسات مكتملة اليوم عبر المؤقت: ${timer.completedToday}. تُسجَّل الدقائق تلقائياً عند اكتمال جلسة التركيز (${state.profile.focusMin} د).",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = qa.border)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("درست خارج التطبيق؟", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                        OutlinedTextField(
                            value = manualMinutesText,
                            onValueChange = { manualMinutesText = it.filter { ch -> ch.isDigit() }.take(3) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(74.dp)
                                .testTag("manual_minutes_input"),
                            suffix = { Text("د", fontSize = 12.sp) }
                        )
                        Button(
                            onClick = {
                                val mins = manualMinutesText.toIntOrNull() ?: 0
                                if (mins > 0) viewModel.addSession(mins, "manual")
                            },
                            modifier = Modifier.testTag("manual_session_add")
                        ) {
                            Text("تسجيل")
                        }
                    }
                }
            }
        }

        // Last 7 days bar chart
        item {
            SectionCard {
                Text(
                    text = "آخر 7 أيام",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                LtrContent {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        week.forEach { w ->
                            val frac = (w.min.toFloat() / maxW.toFloat()).coerceIn(0f, 1f)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = if (w.min > 0) "${w.min}" else "",
                                    fontSize = 10.sp,
                                    color = qa.muted
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(if (w.min > 0) maxOf(0.08f, frac) else 0.04f)
                                            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = if (w.min > 0) 1f else 0.2f
                                                )
                                            )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = DateUtils.dayLabel(w.day),
                                    fontSize = 11.sp,
                                    color = qa.muted,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }

        // Today's Tasks
        item {
            SectionCard {
                Text(
                    text = "✅ مهام اليوم",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                if (todayTasks.isEmpty()) {
                    Text(
                        text = "لا مهام بعد. أضف مهمة صغيرة قابلة للإنجاز.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted
                    )
                } else {
                    todayTasks.forEachIndexed { idx, task ->
                        if (idx > 0) HorizontalDivider(color = qa.border)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleTask(task.id) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = task.done,
                                onCheckedChange = { viewModel.toggleTask(task.id) }
                            )
                            Text(
                                text = task.title,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None
                                ),
                                color = if (task.done) qa.muted else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        placeholder = { Text("مثال: اكتب 5 حالات اختبار لصفحة التسجيل") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("dashboard_task_input")
                    )
                    Button(
                        onClick = {
                            if (newTaskTitle.isNotBlank()) {
                                viewModel.addTask(newTaskTitle.trim(), today)
                                newTaskTitle = ""
                            }
                        },
                        modifier = Modifier.testTag("dashboard_task_add")
                    ) {
                        Text("إضافة")
                    }
                }
            }
        }
    }
}

@Composable
private fun SmartPlanItem(
    eyebrow: String,
    title: String,
    meta: String,
    testTag: String,
    onClick: () -> Unit
) {
    val qa = LocalQaColors.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.dp, qa.border)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(eyebrow, style = MaterialTheme.typography.labelSmall, color = qa.muted)
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(meta, style = MaterialTheme.typography.labelSmall, color = qa.muted)
        }
    }
}
