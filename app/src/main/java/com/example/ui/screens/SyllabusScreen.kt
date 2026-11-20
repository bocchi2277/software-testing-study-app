package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.model.TopicStatus
import com.example.ui.QaViewModel
import com.example.ui.components.PageHeader
import com.example.ui.components.QaProgressBar
import com.example.ui.components.SectionCard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlin.math.roundToInt

@Composable
fun SyllabusScreen(
    state: AppState,
    viewModel: QaViewModel,
    onOpenTopic: (String) -> Unit
) {
    val qa = LocalQaColors.current
    val mutatingChapterIds by viewModel.mutatingChapterIds.collectAsStateWithLifecycle()
    val acc = StatsUtils.chapterAccuracy(state)
    val totalPct = StatsUtils.syllabusPct(state)

    fun cycleStatus(topicId: String) {
        val current = StatsUtils.topicStatus(state, topicId)
        val next = when (current) {
            "" -> "learning"
            "learning" -> "done"
            "done" -> "review"
            else -> null
        }
        viewModel.setProgress("t:$topicId", next)
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "منهج ISTQB CTFL v4.0",
                sub = "${SyllabusData.CHAPTERS.size} فصول، ${SyllabusData.EXAM_TOTAL_Q} سؤالاً في الامتحان. تقدمك $totalPct%"
            )
            SectionCard {
                QaProgressBar(pct = totalPct)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ادخل كل موضوع لقراءة الملخص والأهداف التعليمية (Learning objectives) والمصادر المرتبطة. اضغط على الحالة لتغييرها بسرعة: لم أبدأ ← أتعلم ← أتقنته ← يحتاج مراجعة.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
            }
        }

        items(SyllabusData.CHAPTERS, key = { it.id }) { chapter ->
            val chAcc = acc[chapter.id]
            val chPct = StatsUtils.chapterProgress(state, chapter.id)
            val isModuleComplete = chapter.topics.isNotEmpty() &&
                chapter.topics.all { StatsUtils.topicStatus(state, it.id) == "done" }
            val isMutating = chapter.id in mutatingChapterIds
            val hours = ((chapter.minutes / 60.0) * 10.0).roundToInt() / 10.0

            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "الفصل ${chapter.id}: ${chapter.ar}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = buildString {
                                append("≈ ${chapter.examQ} أسئلة · $hours ساعة دراسة")
                                if (chAcc != null && chAcc.t > 0) {
                                    append(" · دقتك ${chAcc.pct}%")
                                }
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("mark_module_complete_${chapter.id}")
                            .clip(RoundedCornerShape(999.dp))
                            .clickable(enabled = !isMutating) {
                                viewModel.setChapterCompleted(chapter.id, !isModuleComplete)
                            },
                        shape = RoundedCornerShape(999.dp),
                        color = if (isModuleComplete) qa.okBg else MaterialTheme.colorScheme.background,
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (isModuleComplete) qa.ok else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
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
                                    modifier = Modifier.size(15.dp)
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
                Spacer(modifier = Modifier.height(8.dp))
                QaProgressBar(pct = chPct)
                Spacer(modifier = Modifier.height(8.dp))

                chapter.topics.forEachIndexed { idx, topic ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    val st = TopicStatus.fromKey(StatsUtils.topicStatus(state, topic.id))
                    val (badgeTextColor, badgeBgColor, badgeBorderColor) = when (st) {
                        TopicStatus.NONE -> Triple(qa.muted, MaterialTheme.colorScheme.surface, qa.border)
                        TopicStatus.LEARNING -> Triple(qa.warn, qa.warnBg, qa.warn)
                        TopicStatus.REVIEW -> Triple(qa.bad, qa.badBg, qa.bad)
                        TopicStatus.DONE -> Triple(qa.ok, qa.okBg, qa.ok)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenTopic(topic.id) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("topic_status_${topic.id}")
                                .clickable { cycleStatus(topic.id) },
                            shape = RoundedCornerShape(8.dp),
                            color = badgeBgColor,
                            border = BorderStroke(1.dp, badgeBorderColor)
                        ) {
                            Text(
                                text = st.label,
                                color = badgeTextColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = topic.id,
                                    fontFamily = JetBrainsMono,
                                    fontSize = 12.sp,
                                    color = qa.muted
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = topic.ar,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Text(
                            text = "${topic.minutes} د",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                }
            }
        }
    }
}
