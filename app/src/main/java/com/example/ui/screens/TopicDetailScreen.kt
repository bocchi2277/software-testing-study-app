package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResourcesData
import com.example.data.SyllabusData
import com.example.data.ZatonaData
import com.example.model.AppState
import com.example.model.TopicStatus
import com.example.ui.QaViewModel
import com.example.ui.components.DynamicThemeToggleButton
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.components.openExternalUrl
import com.example.ui.theme.LocalQaColors
import com.example.util.StatsUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicDetailScreen(
    topicId: String,
    state: AppState,
    viewModel: QaViewModel,
    onBack: () -> Unit,
    onOpenTopic: (String) -> Unit,
    onOpenPractice: (Int) -> Unit,
    onOpenCards: () -> Unit,
    onOpenLab: (String?) -> Unit,
    onOpenSkills: (String?) -> Unit,
    onOpenResources: () -> Unit
) {
    BackHandler { onBack() }

    val qa = LocalQaColors.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val topic = SyllabusData.findTopic(topicId)

    if (topic == null) {
        SectionCard(modifier = Modifier.padding(16.dp)) {
            Text("الموضوع غير موجود.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onBack) { Text("العودة للمنهج") }
        }
        return
    }

    val idx = SyllabusData.ALL_TOPICS.indexOfFirst { it.id == topicId }
    val prev = SyllabusData.ALL_TOPICS.getOrNull(idx - 1)
    val nxt = SyllabusData.ALL_TOPICS.getOrNull(idx + 1)
    val chapter = SyllabusData.findChapter(topic.chapter)
    val currentStatus = StatsUtils.topicStatus(state, topicId)
    val existingNote = state.notes.firstOrNull { it.topic == topicId }
    // مصادر الفصل والموضوع فقط؛ المصادر العامة للمنهج كله في شاشة «المصادر»
    val relatedResources = remember(topic.chapter, topic.id) {
        ResourcesData.forTopic(topic.chapter, topic.id)
    }

    var noteBody by remember(topicId, existingNote?.id) {
        mutableStateOf(existingNote?.body ?: "")
    }
    var savedBanner by remember { mutableStateOf(false) }

    LaunchedEffect(existingNote?.id) {
        if (existingNote != null && noteBody.isEmpty()) {
            noteBody = existingNote.body
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("topic_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "العودة للمنهج")
                }
                Text(
                    text = "المنهج / الفصل ${chapter?.id ?: topic.chapter}: ${chapter?.ar ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted,
                    modifier = Modifier.weight(1f)
                )
                DynamicThemeToggleButton(
                    onToggle = { isDark -> viewModel.toggleLightDarkTheme(isDark) },
                    compact = true,
                    testTag = "topic_theme_toggle"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            PageHeader(
                title = "${topic.id} — ${topic.ar}",
                sub = "≈ ${topic.minutes} دقيقة"
            )
            if (chapter != null && chapter.topics.size > 1) {
                Spacer(modifier = Modifier.height(6.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(chapter.topics, key = { it.id }) { sibling ->
                        QaFilterChip(
                            text = sibling.id,
                            selected = sibling.id == topicId,
                            onClick = {
                                if (sibling.id != topicId) {
                                    onOpenTopic(sibling.id)
                                }
                            },
                            testTag = "chapter_topic_chip_${sibling.id}"
                        )
                    }
                }
            }
        }

        // Status & Focus timer trigger
        item {
            SectionCard {
                Text("حالتك في هذا الموضوع", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TopicStatus.entries.forEach { st ->
                        QaFilterChip(
                            text = st.label,
                            selected = currentStatus == st.key,
                            onClick = { viewModel.setProgress("t:$topicId", st.key.ifEmpty { null }) },
                            testTag = "detail_status_${st.name.lowercase()}"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = { viewModel.startFocusOnTopic(topicId) },
                    modifier = Modifier.testTag("topic_start_focus")
                ) {
                    Text("🎯 ابدأ جلسة تركيز على هذا الموضوع")
                }
            }
        }

        // Summary points
        item {
            SectionCard {
                Text("الملخص", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                topic.points.forEach { point ->
                    Row(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(point, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }

        // Learning Objectives
        item {
            SectionCard {
                Text(
                    text = "الأهداف التعليمية (Learning objectives)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                topic.objectives.forEach { obj ->
                    Row(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(obj, style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "K1 تذكّر · K2 فهم · K3 تطبيق. أسئلة K3 غالباً حسابات وتقنيات.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }

        // Tip callout
        if (!topic.tip.isNullOrBlank()) {
            item {
                SectionCard(
                    borderColor = qa.warn,
                    containerColor = qa.warnBg
                ) {
                    Text("⚠️ نقطة امتحان:", fontWeight = FontWeight.Bold, color = qa.warn)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(topic.tip, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Chapter Zatona (Quick Cheat Sheet & Exam Traps)
        val zatona = ZatonaData.forChapter(topic.chapter)
        if (zatona != null) {
            item {
                var showFullZatona by rememberSaveable(topic.chapter) { mutableStateOf(false) }
                SectionCard(
                    borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.55f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🥥 ${zatona.title}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(
                            onClick = { showFullZatona = !showFullZatona },
                            modifier = Modifier.testTag("topic_zatona_toggle")
                        ) {
                            Text(if (showFullZatona) "إخفاء التفاصيل ▲" else "عرض الفروقات والفخاخ ▼")
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    zatona.coreSummary.forEach { line ->
                        Row(modifier = Modifier.padding(bottom = 5.dp)) {
                            Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text(line, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    if (showFullZatona) {
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = qa.border)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "قوانين وفروقات سريعة:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        zatona.keyFormulasAndDiffs.forEach { (label, detail) ->
                            Column(modifier = Modifier.padding(bottom = 6.dp)) {
                                Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                Text(detail, style = MaterialTheme.typography.bodySmall, color = qa.muted)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "فخاخ شائعة في الامتحان:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = qa.warn
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        zatona.examTraps.forEach { trap ->
                            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                                Text("⚠️ ", fontSize = 12.sp)
                                Text(trap, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }

        // Test yourself
        item {
            val labLink: Pair<String?, String>? = when (topic.id) {
                "4.2.1", "4.2.2" -> "ep" to "🧪 مولّد EP و BVA"
                "4.2.3" -> "dt" to "🧪 مولّد Decision Table"
                "4.3" -> "cov" to "🧪 محاكي Statement/Branch"
                "5.1" -> "est" to "🧪 حاسبة Three-point"
                "5.2" -> "risk" to "🧪 حاسبة المخاطر"
                "4.1" -> null to "🧪 جرّب المختبر التفاعلي"
                else -> null
            }
            val skillLink: Pair<String, String>? = when (topic.id) {
                "1.5" -> "comm" to "🛠️ التواصل مع المطورين"
                "3.2" -> "req" to "🛠️ مراجعة المتطلبات"
                "4.4" -> "charter" to "🛠️ قالب Exploratory Charter"
                "4.5" -> "story" to "🛠️ User Story وAcceptance"
                "5.1" -> "plan" to "🛠️ قالب Test Plan"
                "5.3" -> "summary" to "🛠️ Test Summary Report"
                "5.5" -> "bug" to "🛠️ مدقّق BugChecker وقالب العيوب"
                else -> null
            }

            SectionCard {
                Text("اختبر نفسك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onOpenPractice(topic.chapter) },
                        modifier = Modifier.testTag("topic_practice_button")
                    ) {
                        Text("أسئلة الفصل ${topic.chapter}")
                    }
                    OutlinedButton(
                        onClick = onOpenCards,
                        modifier = Modifier.testTag("topic_cards_button")
                    ) {
                        Text("بطاقات المصطلحات")
                    }
                    if (labLink != null) {
                        OutlinedButton(
                            onClick = { onOpenLab(labLink.first) },
                            modifier = Modifier.testTag("topic_lab_button")
                        ) {
                            Text(labLink.second)
                        }
                    }
                    if (skillLink != null) {
                        OutlinedButton(
                            onClick = { onOpenSkills(skillLink.first) },
                            modifier = Modifier.testTag("topic_skills_button")
                        ) {
                            Text(skillLink.second)
                        }
                    }
                }
            }
        }

        // Topic Note
        item {
            SectionCard {
                Text("ملاحظاتي على هذا الموضوع", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = noteBody,
                    onValueChange = { noteBody = it },
                    placeholder = { Text("اكتب بكلماتك: ما الذي فهمته؟ ما الذي ما زال غامضاً؟") },
                    minLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("topic_note_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        enabled = noteBody.isNotBlank() || existingNote != null,
                        onClick = {
                            scope.launch {
                                viewModel.saveNote(
                                    id = existingNote?.id,
                                    title = "ملاحظات ${topic.id} — ${topic.ar}",
                                    body = noteBody,
                                    topic = topicId
                                )
                                savedBanner = true
                                delay(1500)
                                savedBanner = false
                            }
                        },
                        modifier = Modifier.testTag("topic_note_save")
                    ) {
                        Text("حفظ")
                    }
                    if (savedBanner) {
                        Text("تم الحفظ ✓", color = qa.ok, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Related Resources
        if (relatedResources.isNotEmpty()) {
            item {
                SectionCard {
                    Text(
                        text = "مصادر خاصة بالفصل ${topic.chapter}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "المصادر المرتبطة بهذا الموضوع والفصل؛ المصادر العامة للمنهج بالكامل في صفحة المصادر.",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    relatedResources.forEachIndexed { rIdx, r ->
                        if (rIdx > 0) HorizontalDivider(color = qa.border)
                        val isTopicDirect = topic.id in r.topics
                        val isArabic = r.lang == "ar"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openExternalUrl(context, r.url) }
                                .padding(vertical = 8.dp)
                        ) {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                val tags = buildList {
                                    if (isTopicDirect) add("خاص بالموضوع")
                                    add(if (isArabic) "عربي" else "EN")
                                    add(r.cat)
                                }
                                tags.forEach { tag ->
                                    val (bg, fg, bold) = when (tag) {
                                        "خاص بالموضوع" -> Triple(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.onPrimaryContainer,
                                            true
                                        )
                                        "عربي" -> Triple(qa.okBg, qa.ok, true)
                                        else -> Triple(MaterialTheme.colorScheme.background, qa.muted, false)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = bg
                                    ) {
                                        Text(
                                            text = tag,
                                            fontSize = 11.sp,
                                            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
                                            color = fg,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${r.title} ↗", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(r.desc, style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        }
                    }
                    TextButton(
                        onClick = onOpenResources,
                        modifier = Modifier.testTag("topic_resources_all_button")
                    ) {
                        Text("المصادر العامة وباقي المصادر ←")
                    }
                }
            }
        }

        // Prev / Next navigation
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (prev != null) {
                    OutlinedButton(onClick = { onOpenTopic(prev.id) }) {
                        Text("→ ${prev.id}")
                    }
                } else {
                    Spacer(modifier = Modifier)
                }
                if (nxt != null) {
                    OutlinedButton(onClick = { onOpenTopic(nxt.id) }) {
                        Text("${nxt.id} ←")
                    }
                }
            }
        }
    }
}
