package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Chapter
import com.example.data.SyllabusData
import com.example.data.Topic
import com.example.model.AppState
import com.example.model.TopicStatus
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

private enum class ChronologicalModuleState {
    COMPLETED,
    IN_PROGRESS,
    NOT_STARTED
}

private data class ChronologicalRoadmapEntry(
    val chapter: Chapter,
    val moduleState: ChronologicalModuleState,
    val progressPct: Int,
    val doneTopicsCount: Int,
    val startStudyDay: Int,
    val endStudyDay: Int,
    val isCurrentMilestone: Boolean,
    val displayedTopics: List<Topic>,
    val autoExpandedByTopicMatch: Boolean
)

@Composable
fun Roadmap(
    state: AppState,
    onOpenTopic: (String) -> Unit,
    onToggleModuleComplete: (chapterId: Int, completed: Boolean) -> Unit,
    onToggleTopicComplete: (topicId: String, completed: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mutatingChapterIds: Set<Int> = emptySet(),
    initiallyExpandedChapterId: Int? = null
) {
    val qa = LocalQaColors.current
    var expandedChapterId by rememberSaveable(initiallyExpandedChapterId) {
        mutableStateOf(initiallyExpandedChapterId)
    }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val trimmedQuery = searchQuery.trim()
    var collapsedInSearch by remember(trimmedQuery) { mutableStateOf(emptySet<Int>()) }

    val orderedChapters = remember { SyllabusData.CHAPTERS.sortedBy { it.id } }
    val dailyStudyMinutes = max(30, (state.profile.dailyHours * 60.0).roundToInt())

    val chronologicalTimeline = remember(state.progress, dailyStudyMinutes) {
        var cumulativeMin = 0
        val states = orderedChapters.map { ch ->
            val statuses = ch.topics.map { TopicStatus.fromKey(StatsUtils.topicStatus(state, it.id)) }
            val doneCount = statuses.count { it == TopicStatus.DONE }
            val startedCount = statuses.count { it != TopicStatus.NONE }
            val pct = StatsUtils.chapterProgress(state, ch.id)
            val modState = when {
                ch.topics.isNotEmpty() && doneCount == ch.topics.size -> ChronologicalModuleState.COMPLETED
                startedCount > 0 || pct > 0 -> ChronologicalModuleState.IN_PROGRESS
                else -> ChronologicalModuleState.NOT_STARTED
            }
            val startDay = (cumulativeMin / dailyStudyMinutes) + 1
            cumulativeMin += ch.minutes
            val endDay = max(startDay, ceil(cumulativeMin.toDouble() / dailyStudyMinutes.toDouble()).toInt())
            Triple(ch, modState, Triple(pct, doneCount, startDay to endDay))
        }
        val firstIncompleteChapterId = states.firstOrNull { it.second != ChronologicalModuleState.COMPLETED }?.first?.id
        states.map { (ch, modState, meta) ->
            val (pct, doneCount, daysSpan) = meta
            ChronologicalRoadmapEntry(
                chapter = ch,
                moduleState = modState,
                progressPct = pct,
                doneTopicsCount = doneCount,
                startStudyDay = daysSpan.first,
                endStudyDay = daysSpan.second,
                isCurrentMilestone = ch.id == firstIncompleteChapterId,
                displayedTopics = ch.topics,
                autoExpandedByTopicMatch = false
            )
        }
    }

    val filteredEntries = remember(trimmedQuery, chronologicalTimeline) {
        if (trimmedQuery.isEmpty()) {
            chronologicalTimeline
        } else {
            chronologicalTimeline.mapNotNull { entry ->
                val ch = entry.chapter
                val chapterMatches = ch.ar.contains(trimmedQuery, ignoreCase = true) ||
                    ch.en.contains(trimmedQuery, ignoreCase = true) ||
                    "الفصل ${ch.id}".contains(trimmedQuery, ignoreCase = true) ||
                    "المرحلة ${ch.id}".contains(trimmedQuery, ignoreCase = true) ||
                    "${ch.id}" == trimmedQuery
                val matchingTopics = ch.topics.filter { topic ->
                    topic.id.contains(trimmedQuery, ignoreCase = true) ||
                        topic.ar.contains(trimmedQuery, ignoreCase = true) ||
                        topic.en.contains(trimmedQuery, ignoreCase = true)
                }
                when {
                    matchingTopics.isNotEmpty() -> entry.copy(
                        displayedTopics = matchingTopics,
                        autoExpandedByTopicMatch = true
                    )
                    chapterMatches -> entry
                    else -> null
                }
            }
        }
    }

    val completedModules = chronologicalTimeline.count { it.moduleState == ChronologicalModuleState.COMPLETED }
    val totalTopicsDone = SyllabusData.ALL_TOPICS.count { StatsUtils.topicStatus(state, it.id) == "done" }
    val overallPct = StatsUtils.syllabusPct(state)
    val accuracyMap = remember(state.attempts) { StatsUtils.chapterAccuracy(state) }

    SectionCard(modifier = modifier.testTag("syllabus_roadmap_card")) {
        // Roadmap Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🗺️ خارطة طريق منهج ISTQB",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "تسلسل زمني من الفصل 1 إلى ${orderedChapters.size} · $completedModules/${orderedChapters.size} مكتملة · $totalTopicsDone/${SyllabusData.ALL_TOPICS.size} موضوع",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                shape = RoundedCornerShape(999.dp),
                color = if (overallPct >= 100) qa.okBg else MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (overallPct >= 100) qa.ok else MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    text = "$overallPct%",
                    fontFamily = JetBrainsMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (overallPct >= 100) qa.ok else MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        QaProgressBar(
            pct = overallPct,
            color = if (overallPct >= 100) qa.ok else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar to filter modules chronologically by name or topic
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = {
                Text(
                    text = "ابحث باسم الفصل أو الموضوع (مثال: 4.2، الحدود)...",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "بحث في خارطة الطريق",
                    tint = qa.muted
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("roadmap_search_clear")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "مسح البحث",
                            tint = qa.muted
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("roadmap_search_input")
        )

        if (trimmedQuery.isNotEmpty() && filteredEntries.isNotEmpty()) {
            val matchedTopicTotal = filteredEntries.sumOf { it.displayedTopics.size }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "النتائج: ${filteredEntries.size}/${orderedChapters.size} فصول · $matchedTopicTotal موضوع",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.testTag("roadmap_search_count")
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (filteredEntries.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, qa.border),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("roadmap_search_empty")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "لا توجد فصول أو مواضيع مطابقة لـ «$trimmedQuery»",
                        style = MaterialTheme.typography.bodyMedium,
                        color = qa.muted
                    )
                    TextButton(
                        onClick = { searchQuery = "" },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("roadmap_search_reset")
                    ) {
                        Text("إعادة ضبط البحث")
                    }
                }
            }
        } else {
            // Chronological Vertical Timeline of Syllabus Modules
            filteredEntries.forEachIndexed { index, entry ->
                val chapter = entry.chapter
                val prevState = if (index > 0) filteredEntries[index - 1].moduleState else null
                val isFirst = index == 0
                val isLast = index == filteredEntries.lastIndex
                val isExpanded = if (entry.autoExpandedByTopicMatch) {
                    chapter.id !in collapsedInSearch
                } else {
                    expandedChapterId == chapter.id
                }
                val isMutating = chapter.id in mutatingChapterIds
                val chAcc = accuracyMap[chapter.id]

                ChronologicalRoadmapRow(
                    entry = entry,
                    quizAccuracyPct = if (chAcc != null && chAcc.t > 0) chAcc.pct else null,
                    state = state,
                    prevModuleState = prevState,
                    isFirst = isFirst,
                    isLast = isLast,
                    isExpanded = isExpanded,
                    isMutating = isMutating,
                    onToggleExpand = {
                        if (entry.autoExpandedByTopicMatch) {
                            collapsedInSearch = if (chapter.id in collapsedInSearch) {
                                collapsedInSearch - chapter.id
                            } else {
                                collapsedInSearch + chapter.id
                            }
                        } else {
                            expandedChapterId = if (isExpanded) null else chapter.id
                        }
                    },
                    onToggleModuleComplete = {
                        onToggleModuleComplete(
                            chapter.id,
                            entry.moduleState != ChronologicalModuleState.COMPLETED
                        )
                    },
                    onOpenTopic = onOpenTopic,
                    onToggleTopicComplete = onToggleTopicComplete
                )
            }
        }
    }
}

@Composable
private fun ChronologicalRoadmapRow(
    entry: ChronologicalRoadmapEntry,
    quizAccuracyPct: Int?,
    state: AppState,
    prevModuleState: ChronologicalModuleState?,
    isFirst: Boolean,
    isLast: Boolean,
    isExpanded: Boolean,
    isMutating: Boolean,
    onToggleExpand: () -> Unit,
    onToggleModuleComplete: () -> Unit,
    onOpenTopic: (String) -> Unit,
    onToggleTopicComplete: (topicId: String, completed: Boolean) -> Unit
) {
    val qa = LocalQaColors.current
    val chapter = entry.chapter
    val moduleState = entry.moduleState
    val chPct = entry.progressPct
    val hours = ((chapter.minutes / 60.0) * 10.0).roundToInt() / 10.0

    val nodeColor = when {
        moduleState == ChronologicalModuleState.COMPLETED -> qa.ok
        moduleState == ChronologicalModuleState.IN_PROGRESS || entry.isCurrentMilestone -> MaterialTheme.colorScheme.primary
        else -> qa.border
    }

    val topLineColor = when (prevModuleState) {
        ChronologicalModuleState.COMPLETED -> qa.ok
        ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
        else -> qa.border.copy(alpha = 0.7f)
    }

    val bottomLineColor = when (moduleState) {
        ChronologicalModuleState.COMPLETED -> qa.ok
        ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
        ChronologicalModuleState.NOT_STARTED -> qa.border.copy(alpha = 0.7f)
    }

    val cardBorder = when {
        moduleState == ChronologicalModuleState.COMPLETED -> qa.ok.copy(alpha = 0.75f)
        isExpanded || entry.isCurrentMilestone -> MaterialTheme.colorScheme.primary
        moduleState == ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
        else -> qa.border
    }

    val cardBg = when {
        moduleState == ChronologicalModuleState.COMPLETED -> qa.okBg.copy(alpha = 0.35f)
        entry.isCurrentMilestone || moduleState == ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.surface
        else -> MaterialTheme.colorScheme.background
    }

    val daySpanLabel = if (entry.startStudyDay == entry.endStudyDay) {
        "اليوم ${entry.startStudyDay}"
    } else {
        "الأيام ${entry.startStudyDay}–${entry.endStudyDay}"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag("roadmap_module_${chapter.id}")
    ) {
        // Chronological Timeline Rail Column
        Column(
            modifier = Modifier
                .width(34.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .height(14.dp)
                    .background(if (isFirst) Color.Transparent else topLineColor)
            )

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            moduleState == ChronologicalModuleState.COMPLETED -> qa.ok
                            moduleState == ChronologicalModuleState.IN_PROGRESS || entry.isCurrentMilestone ->
                                MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                    .border(width = 2.dp, color = nodeColor, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (moduleState == ChronologicalModuleState.COMPLETED) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "الفصل ${chapter.id} مكتمل",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Text(
                        text = "${chapter.id}",
                        fontFamily = JetBrainsMono,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (moduleState == ChronologicalModuleState.IN_PROGRESS || entry.isCurrentMilestone) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            qa.muted
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(2.5.dp)
                    .weight(1f)
                    .background(if (isLast) Color.Transparent else bottomLineColor)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Module Content Card
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 10.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(onClick = onToggleExpand),
                shape = RoundedCornerShape(12.dp),
                color = cardBg,
                border = BorderStroke(1.dp, cardBorder)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                    // Chronological Stage & Progress Percentage Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "المرحلة ${chapter.id} · $daySpanLabel",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (entry.isCurrentMilestone) MaterialTheme.colorScheme.primary else qa.muted
                            )
                            if (entry.isCurrentMilestone && moduleState != ChronologicalModuleState.COMPLETED) {
                                Surface(
                                    shape = RoundedCornerShape(999.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "المحطة الحالية",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = when (moduleState) {
                                ChronologicalModuleState.COMPLETED -> qa.okBg
                                ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
                                ChronologicalModuleState.NOT_STARTED -> MaterialTheme.colorScheme.surface
                            },
                            border = BorderStroke(
                                width = 1.dp,
                                color = when (moduleState) {
                                    ChronologicalModuleState.COMPLETED -> qa.ok
                                    ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                                    ChronologicalModuleState.NOT_STARTED -> qa.border
                                }
                            )
                        ) {
                            Text(
                                text = "$chPct%",
                                fontFamily = JetBrainsMono,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (moduleState) {
                                    ChronologicalModuleState.COMPLETED -> qa.ok
                                    ChronologicalModuleState.IN_PROGRESS -> MaterialTheme.colorScheme.onPrimaryContainer
                                    ChronologicalModuleState.NOT_STARTED -> qa.muted
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .testTag("roadmap_module_pct_${chapter.id}")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = chapter.ar,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = buildString {
                                    append("${entry.doneTopicsCount}/${chapter.topics.size} مواضيع · ${chapter.examQ} أسئلة · $hours س")
                                    if (quizAccuracyPct != null) {
                                        append(" · الدقة $quizAccuracyPct%")
                                    }
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = qa.muted
                            )
                        }

                        // Module Completion Checkmark Toggle
                        IconButton(
                            onClick = onToggleModuleComplete,
                            enabled = !isMutating,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .testTag("roadmap_toggle_module_${chapter.id}")
                        ) {
                            if (isMutating) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = if (moduleState == ChronologicalModuleState.COMPLETED) {
                                        Icons.Default.CheckCircle
                                    } else {
                                        Icons.Default.RadioButtonUnchecked
                                    },
                                    contentDescription = if (moduleState == ChronologicalModuleState.COMPLETED) {
                                        "إلغاء إكمال الفصل ${chapter.id}"
                                    } else {
                                        "تحديد الفصل ${chapter.id} كمكتمل"
                                    },
                                    tint = if (moduleState == ChronologicalModuleState.COMPLETED) {
                                        qa.ok
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "طي المواضيع" else "عرض مواضيع الفصل",
                            tint = qa.muted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Per-Module Progress Bar Indicator (always visible 0%..100%)
                    Spacer(modifier = Modifier.height(8.dp))
                    val animFrac by animateFloatAsState(
                        targetValue = (chPct.coerceIn(0, 100) / 100f),
                        animationSpec = tween(320),
                        label = "roadmap_ch_${chapter.id}"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(CircleShape)
                            .background(qa.border.copy(alpha = 0.55f))
                            .testTag("roadmap_module_bar_${chapter.id}")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animFrac)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (moduleState == ChronologicalModuleState.COMPLETED) qa.ok
                                    else MaterialTheme.colorScheme.primary
                                )
                        )
                    }

                    val nextChapterTopic = entry.displayedTopics.firstOrNull {
                        TopicStatus.fromKey(StatsUtils.topicStatus(state, it.id)) != TopicStatus.DONE
                    } ?: entry.displayedTopics.firstOrNull() ?: chapter.topics.firstOrNull()

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isExpanded) {
                                "اضغط على أي موضوع لقراءته"
                            } else {
                                "عرض الدروس (${entry.displayedTopics.size})"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted,
                            modifier = Modifier.weight(1f)
                        )
                        if (nextChapterTopic != null) {
                            val enterLabel = when (moduleState) {
                                ChronologicalModuleState.COMPLETED -> "مراجعة الفصل ←"
                                ChronologicalModuleState.IN_PROGRESS -> "متابعة (${nextChapterTopic.id}) ←"
                                ChronologicalModuleState.NOT_STARTED -> "دخول الفصل ←"
                            }
                            Surface(
                                modifier = Modifier
                                    .minimumInteractiveComponentSize()
                                    .clip(RoundedCornerShape(999.dp))
                                    .clickable { onOpenTopic(nextChapterTopic.id) }
                                    .testTag("roadmap_enter_chapter_${chapter.id}"),
                                shape = RoundedCornerShape(999.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.45f))
                            ) {
                                Text(
                                    text = enterLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // Expandable Topic Checklist
                    AnimatedVisibility(visible = isExpanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            HorizontalDivider(color = qa.border)
                            Spacer(modifier = Modifier.height(6.dp))
                            entry.displayedTopics.forEachIndexed { tIdx, topic ->
                                if (tIdx > 0) {
                                    HorizontalDivider(color = qa.border.copy(alpha = 0.5f))
                                }
                                val topicStatus = TopicStatus.fromKey(StatsUtils.topicStatus(state, topic.id))
                                val isTopicDone = topicStatus == TopicStatus.DONE
                                val (icon, iconTint, statusDesc) = when (topicStatus) {
                                    TopicStatus.DONE -> Triple(Icons.Default.CheckCircle, qa.ok, "مكتمل")
                                    TopicStatus.LEARNING -> Triple(Icons.Default.Schedule, qa.warn, "أتعلم")
                                    TopicStatus.REVIEW -> Triple(Icons.Default.Replay, qa.bad, "يحتاج مراجعة")
                                    TopicStatus.NONE -> Triple(Icons.Default.RadioButtonUnchecked, qa.muted, "لم يبدأ")
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .minimumInteractiveComponentSize()
                                        .testTag("roadmap_topic_open_${topic.id}")
                                        .clickable { onOpenTopic(topic.id) }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    IconButton(
                                        onClick = { onToggleTopicComplete(topic.id, !isTopicDone) },
                                        modifier = Modifier
                                            .minimumInteractiveComponentSize()
                                            .testTag("roadmap_topic_check_${topic.id}")
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = "${topic.id}: $statusDesc",
                                            tint = iconTint,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Text(
                                        text = topic.id,
                                        fontFamily = JetBrainsMono,
                                        fontSize = 11.sp,
                                        color = qa.muted
                                    )
                                    Text(
                                        text = topic.ar,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = if (isTopicDone) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isTopicDone) qa.ok else MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${topic.minutes} د",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = qa.muted
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                        contentDescription = "فتح درس ${topic.id}",
                                        tint = qa.muted,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
