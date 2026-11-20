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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.ResourcesData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.model.TopicStatus
import com.example.ui.QaViewModel
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
    onOpenLab: () -> Unit,
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
    val relatedResources = remember(topic.chapter) {
        ResourcesData.RESOURCES.filter { topic.chapter in it.ch }.take(5)
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("topic_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "العودة للمنهج")
                }
                Text(
                    text = "المنهج / الفصل ${chapter?.id ?: topic.chapter}: ${chapter?.ar ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            PageHeader(
                title = "${topic.id} — ${topic.ar}",
                sub = "≈ ${topic.minutes} دقيقة"
            )
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

        // Test yourself
        item {
            SectionCard {
                Text("اختبر نفسك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = { onOpenPractice(topic.chapter) }) {
                        Text("أسئلة الفصل ${topic.chapter}")
                    }
                    OutlinedButton(onClick = onOpenCards) {
                        Text("بطاقات المصطلحات")
                    }
                    if (topic.chapter == 4 || topic.id == "5.1") {
                        OutlinedButton(onClick = onOpenLab) {
                            Text("🧪 جرّب المختبر التفاعلي")
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
                    Text("مصادر مجانية مرتبطة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    relatedResources.forEachIndexed { rIdx, r ->
                        if (rIdx > 0) HorizontalDivider(color = qa.border)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openExternalUrl(context, r.url) }
                                .padding(vertical = 8.dp)
                        ) {
                            Text("${r.title} ↗", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            Text(r.desc, style = MaterialTheme.typography.labelSmall, color = qa.muted)
                        }
                    }
                    TextButton(onClick = onOpenResources) {
                        Text("كل المصادر ←")
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
