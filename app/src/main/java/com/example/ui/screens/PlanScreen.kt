package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CareerData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.CardsRoute
import com.example.ui.NotesRoute
import com.example.ui.PracticeRoute
import com.example.ui.QaViewModel
import com.example.ui.SettingsRoute
import com.example.ui.SyllabusRoute
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaDatePickerDialog
import com.example.ui.components.SectionCard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private data class StudyBlockDef(val title: String, val ratio: Double, val route: Any)

private val BLOCK_DEFS = listOf(
    StudyBlockDef("📖 قراءة موضوع جديد من المنهج", 0.45, SyllabusRoute),
    StudyBlockDef("📝 أسئلة تطبيقية", 0.25, PracticeRoute()),
    StudyBlockDef("🃏 مراجعة البطاقات والمصطلحات", 0.15, CardsRoute),
    StudyBlockDef("✍️ تلخيص وملاحظات", 0.15, NotesRoute)
)

private fun fmtClock(totalMin: Int): String =
    "%02d:%02d".format(Locale.US, (totalMin / 60) % 24, totalMin % 60)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlanScreen(
    state: AppState,
    viewModel: QaViewModel,
    onOpenTopic: (String) -> Unit,
    onNavigateSection: (Any) -> Unit
) {
    val qa = LocalQaColors.current
    val today = DateUtils.todayStr()
    var newTitle by remember { mutableStateOf("") }
    var taskDay by remember { mutableStateOf(today) }
    var showTaskDatePicker by remember { mutableStateOf(false) }
    var hoursMenuOpen by remember { mutableStateOf(false) }

    val totalMin = (state.profile.dailyHours * 60.0).roundToInt()
    val parts = state.profile.startTime.split(":")
    var curMin = (parts.getOrNull(0)?.toIntOrNull() ?: 18) * 60 + (parts.getOrNull(1)?.toIntOrNull() ?: 0)

    val blocks = BLOCK_DEFS.mapIndexed { idx, def ->
        val mins = (((totalMin * def.ratio) / 5.0).roundToInt() * 5)
        val s = curMin
        curMin += mins
        Triple(idx, def, Triple(mins, s, curMin))
    }
    val doneMin = blocks.filter { (i, _, _) -> state.progress["blk:$today:$i"] == "1" }.sumOf { it.third.first }

    val weekNum = (DateUtils.daysBetween(state.profile.startDate, today) / 7) + 1
    val daysLeft = state.profile.examDate?.let { DateUtils.daysBetween(today, it) }

    val pending = state.tasks.filter { !it.done && it.day <= today }
    val upcoming = state.tasks.filter { it.day > today }
    val doneRecent = state.tasks.filter { it.done && it.day <= today }.takeLast(5)
    val recentSessions = state.sessions.take(8)

    if (showTaskDatePicker) {
        QaDatePickerDialog(
            initialDateStr = taskDay,
            onDateSelected = { d -> if (d != null) taskDay = d },
            onDismiss = { showTaskDatePicker = false }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(title = "الخطة والجدول", sub = "جدول يومي ذكي، خطة 8 أسابيع، ومهامك.")
        }

        if (daysLeft != null) {
            item {
                val isTight = daysLeft < 56
                SectionCard(
                    borderColor = if (isTight) qa.warn else qa.ok,
                    containerColor = if (isTight) qa.warnBg else qa.okBg
                ) {
                    val recHours = min(4.0, ceil((56.0 / max(daysLeft, 7)) * state.profile.dailyHours * 2.0) / 2.0)
                    val msg = when {
                        daysLeft < 0 -> "موعد الامتحان مضى. حدّث الموعد من الرئيسية."
                        daysLeft >= 56 -> "متبقي $daysLeft يوماً: وقتك مريح. التزم بساعتين يومياً وستنهي الخطة مبكراً."
                        else -> "متبقي $daysLeft يوماً فقط (أقل من 8 أسابيع): ارفع ساعات المذاكرة اليومية إلى $recHours ساعة تقريباً."
                    }
                    Text(msg, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Daily Schedule
        item {
            SectionCard {
                Text("🗓️ جدول اليوم", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("ساعات المذاكرة:", style = MaterialTheme.typography.bodyMedium)
                    Box {
                        OutlinedButton(onClick = { hoursMenuOpen = true }) {
                            Text("${state.profile.dailyHours} س")
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "اختر ساعات المذاكرة")
                        }
                        DropdownMenu(expanded = hoursMenuOpen, onDismissRequest = { hoursMenuOpen = false }) {
                            listOf(0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0).forEach { h ->
                                DropdownMenuItem(
                                    text = { Text("$h") },
                                    onClick = {
                                        viewModel.updateProfile { it.copy(dailyHours = h) }
                                        hoursMenuOpen = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                blocks.forEach { (i, def, timing) ->
                    val (mins, s, e) = timing
                    val done = state.progress["blk:$today:$i"] == "1"
                    HorizontalDivider(color = qa.border)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = done,
                            onCheckedChange = { viewModel.setProgress("blk:$today:$i", if (done) null else "1") }
                        )
                        LtrContent {
                            Text(
                                text = "${fmtClock(s)}–${fmtClock(e)}",
                                fontFamily = JetBrainsMono,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = def.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = if (done) qa.muted else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onNavigateSection(def.route) }
                                .padding(horizontal = 8.dp)
                        )
                        Text("$mins د", style = MaterialTheme.typography.labelSmall, color = qa.muted)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "أنجزت ${DateUtils.fmtMinutes(doneMin)} من ${DateUtils.fmtMinutes(totalMin)}. استخدم Pomodoro: 25 دقيقة تركيز + 5 راحة.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }

        // 8-Week Plan
        item {
            SectionCard {
                Text("🧭 خطة 8 أسابيع", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                val weekDesc = when {
                    weekNum in 1..8 -> "— أنت الآن في الأسبوع $weekNum"
                    weekNum > 8 -> "— تجاوزت 8 أسابيع، راجع وحاكِ الامتحان"
                    else -> "— لم تبدأ بعد"
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "تبدأ الخطة من ${state.profile.startDate} $weekDesc.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { onNavigateSection(SettingsRoute) }) {
                        Text("الإعدادات")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CareerData.WEEKS.forEach { w ->
                        val isNow = weekNum == w.n
                        val doneTopics = w.topics.count { StatsUtils.topicStatus(state, it) == "done" }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isNow) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(if (isNow) 2.dp else 1.dp, if (isNow) MaterialTheme.colorScheme.primary else qa.border)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("الأسبوع ${w.n}: ${w.title}", fontWeight = FontWeight.Bold)
                                    Text(
                                        text = buildString {
                                            append(DateUtils.addDays(state.profile.startDate, (w.n - 1) * 7))
                                            if (w.topics.isNotEmpty()) append(" · $doneTopics/${w.topics.size}")
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = qa.muted
                                    )
                                }
                                if (w.topics.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LtrContent {
                                        FlowRow(
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            w.topics.forEach { id ->
                                                val st = StatsUtils.topicStatus(state, id)
                                                val (bg, fg, br) = when (st) {
                                                    "done" -> Triple(qa.okBg, qa.ok, qa.ok)
                                                    "learning" -> Triple(qa.warnBg, qa.warn, qa.warn)
                                                    else -> Triple(MaterialTheme.colorScheme.surface, qa.muted, qa.border)
                                                }
                                                Surface(
                                                    modifier = Modifier.clickable { onOpenTopic(id) },
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = bg,
                                                    border = BorderStroke(1.dp, br)
                                                ) {
                                                    Text(
                                                        text = id,
                                                        fontFamily = JetBrainsMono,
                                                        fontSize = 11.sp,
                                                        color = fg,
                                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("🎯 ${w.goal}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }

        // Tasks CRUD
        item {
            SectionCard {
                Text("✅ المهام", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    placeholder = { Text("مهمة جديدة…") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { showTaskDatePicker = true }) {
                        Text(taskDay)
                    }
                    Button(onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.addTask(newTitle.trim(), taskDay)
                            newTitle = ""
                        }
                    }) {
                        Text("إضافة")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                val groups = listOf(
                    "مستحقة / اليوم" to pending,
                    "قادمة" to upcoming,
                    "أُنجزت مؤخراً" to doneRecent
                )
                if (state.tasks.isEmpty()) {
                    Text("لا مهام. أضف أول مهمة.", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                } else {
                    groups.forEach { (label, list) ->
                        if (list.isNotEmpty()) {
                            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = qa.muted)
                            list.forEach { t ->
                                HorizontalDivider(color = qa.border)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(checked = t.done, onCheckedChange = { viewModel.toggleTask(t.id) })
                                    Text(
                                        text = t.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDecoration = if (t.done) TextDecoration.LineThrough else TextDecoration.None
                                        ),
                                        color = if (t.done) qa.muted else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(1f)
                                    )
                                    LtrContent {
                                        Text(t.day, fontSize = 11.sp, color = qa.muted)
                                    }
                                    IconButton(onClick = { viewModel.deleteTask(t.id) }) {
                                        Icon(Icons.Default.Close, contentDescription = "حذف المهمة", tint = qa.bad)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }

        // Recent Sessions
        item {
            SectionCard {
                Text("🕒 آخر جلسات الدراسة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                if (recentSessions.isEmpty()) {
                    Text("لم تسجّل جلسات بعد.", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                } else {
                    recentSessions.forEachIndexed { idx, s ->
                        if (idx > 0) HorizontalDivider(color = qa.border)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LtrContent { Text(s.day, fontSize = 11.sp, color = qa.muted) }
                            Text(
                                text = buildString {
                                    append(if (s.kind == "manual") "تسجيل يدوي" else "جلسة تركيز")
                                    if (!s.topic.isNullOrBlank()) append(" · ${s.topic}")
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                            )
                            Text("${s.minutes} د", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.deleteSession(s.id) }) {
                                Icon(Icons.Default.Close, contentDescription = "حذف الجلسة", tint = qa.bad)
                            }
                        }
                    }
                }
            }
        }
    }
}
