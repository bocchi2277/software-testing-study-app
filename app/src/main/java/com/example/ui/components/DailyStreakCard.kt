package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppState
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils

@Composable
fun DailyStreakCard(
    state: AppState,
    checkingIn: Boolean,
    onCheckInToday: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    val currentStreak = StatsUtils.streak(state)
    val bestStreak = StatsUtils.bestStreak(state)
    val studiedToday = StatsUtils.studiedToday(state)
    val today = DateUtils.todayStr()

    val recentDays = remember(state.sessions, state.profile.lastStudyDate, currentStreak) {
        val minutesByDay = StatsUtils.minutesByDay(state)
        val streakDays = buildSet {
            val anchor = state.profile.lastStudyDate?.takeIf { DateUtils.isValidDay(it) }
            if (anchor != null && currentStreak > 0) {
                val capped = currentStreak.coerceAtMost(7)
                for (offset in 0 until capped) {
                    add(DateUtils.addDays(anchor, -offset))
                }
            }
        }
        DateUtils.lastNDays(7).map { day ->
            day to ((minutesByDay[day] ?: 0) > 0 || day in streakDays)
        }
    }

    val cardBg = if (studiedToday) {
        qa.okBg.copy(alpha = 0.55f)
    } else if (currentStreak > 0) {
        qa.warnBg.copy(alpha = 0.65f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    val cardBorder = if (studiedToday) qa.ok else if (currentStreak > 0) qa.warn else qa.border

    SectionCard(
        modifier = modifier.testTag("daily_streak_card"),
        containerColor = cardBg,
        borderColor = cardBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (studiedToday) qa.ok.copy(alpha = 0.16f) else qa.warn.copy(alpha = 0.18f),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "سلسلة الدراسة اليومية",
                            tint = if (studiedToday) qa.ok else qa.warn,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AnimatedContent(targetState = currentStreak, label = "streak_count") { count ->
                            Text(
                                text = count.toString(),
                                fontFamily = JetBrainsMono,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (studiedToday) qa.ok else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("daily_streak_count")
                            )
                        }
                        Text(
                            text = if (currentStreak == 1) "يوم متواصل" else "أيام متواصلة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                    Text(
                        text = when {
                            studiedToday -> "أحسنت! تم توثيق دراسة اليوم في حسابك"
                            currentStreak > 0 -> "ادرس اليوم للحفاظ على سلسلتك المتواصلة"
                            else -> "ابدأ سلسلتك اليوم بالتعلّم أو جلسة تركيز"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, qa.border)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "أطول سلسلة",
                        tint = qa.warn,
                        modifier = Modifier.size(16.dp)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "الأفضل",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            color = qa.muted
                        )
                        Text(
                            text = "$bestStreak",
                            fontFamily = JetBrainsMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 7-day consistency tracker
        LtrContent {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                recentDays.forEach { (day, active) ->
                    val isToday = day == today
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        active -> qa.ok
                                        isToday -> MaterialTheme.colorScheme.primaryContainer
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (active) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "$day: تمّت الدراسة",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = if (isToday) "•" else "",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = DateUtils.dayLabel(day),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isToday) MaterialTheme.colorScheme.primary else qa.muted,
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        if (!studiedToday) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onCheckInToday,
                enabled = !checkingIn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("daily_streak_checkin_btn")
            ) {
                if (checkingIn) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("تسجيل دراسة اليوم (+1 سلسلة) 🔥")
            }
        }
    }
}
