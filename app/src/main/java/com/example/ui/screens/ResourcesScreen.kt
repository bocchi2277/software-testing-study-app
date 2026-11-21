package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ResourcesData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.components.openExternalUrl
import com.example.ui.theme.LocalQaColors

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ResourcesScreen(
    state: AppState,
    viewModel: QaViewModel,
    initialChapter: Int? = null
) {
    val qa = LocalQaColors.current
    val context = LocalContext.current

    var cat by rememberSaveable { mutableStateOf("الكل") }
    var query by rememberSaveable { mutableStateOf("") }
    var level by rememberSaveable { mutableStateOf("الكل") }
    var lang by rememberSaveable { mutableStateOf("الكل") }
    var only by rememberSaveable { mutableStateOf("") } // "", "top", "saved", "done"
    var chapterFilter by rememberSaveable(initialChapter) {
        mutableStateOf(
            if (initialChapter != null && initialChapter in 1..6) initialChapter.toString() else "الكل"
        )
    }

    val selectedChapter = chapterFilter.toIntOrNull()

    val list = remember(cat, query, level, lang, only, chapterFilter, state.progress) {
        val s = query.trim().lowercase()
        val filtered = ResourcesData.RESOURCES.filter { r ->
            val chapterMatch = when {
                chapterFilter == "الكل" -> true
                chapterFilter == "general" -> r.isGeneral || r.ch.isEmpty()
                selectedChapter != null -> selectedChapter in r.ch
                else -> true
            }
            chapterMatch &&
                (cat == "الكل" || r.cat == cat) &&
                (level == "الكل" || r.level == level) &&
                (lang == "الكل" || r.lang == (if (lang == "عربي") "ar" else "en")) &&
                (s.isEmpty() || r.title.lowercase().contains(s) || r.desc.lowercase().contains(s)) &&
                (only.isEmpty() || (if (only == "top") r.top else state.progress["r:${r.id}"] == only))
        }
        if (selectedChapter != null) {
            filtered.sortedBy { if (it.isGeneral) 1 else 0 }
        } else {
            filtered
        }
    }

    val arCount = remember { ResourcesData.RESOURCES.count { it.lang == "ar" } }
    val enCount = remember { ResourcesData.RESOURCES.count { it.lang == "en" } }
    val doneCount = ResourcesData.RESOURCES.count { state.progress["r:${it.id}"] == "done" }

    val sortedList = remember(list, selectedChapter) {
        if (selectedChapter != null) {
            list.sortedWith(
                compareBy<com.example.data.StudyResource> { if (it.isGeneral) 1 else 0 }
                    .thenBy { if (it.lang == "ar") 0 else 1 }
            )
        } else {
            list
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            PageHeader(
                title = "مصادر مجانية (${ResourcesData.RESOURCES.size})",
                sub = "رسمية وتعليمية وتدريب عملي ومجتمعات. أنجزت منها $doneCount."
            )
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("ابحث في المصادر…") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("resources_search_input")
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QaFilterChip(
                    text = "كل الفصول",
                    selected = chapterFilter == "الكل",
                    onClick = { chapterFilter = "الكل" },
                    testTag = "res_ch_all"
                )
                (1..6).forEach { c ->
                    QaFilterChip(
                        text = "فصل $c",
                        selected = chapterFilter == c.toString(),
                        onClick = { chapterFilter = if (chapterFilter == c.toString()) "الكل" else c.toString() },
                        testTag = "res_ch_$c"
                    )
                }
                QaFilterChip(
                    text = "عام للمنهج",
                    selected = chapterFilter == "general",
                    onClick = { chapterFilter = if (chapterFilter == "general") "الكل" else "general" },
                    testTag = "res_ch_general"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("الكل", "مبتدئ", "متوسط", "متقدم").forEach { lv ->
                    QaFilterChip(lv, level == lv, { level = lv })
                }
                QaFilterChip(
                    text = "عربي ($arCount)",
                    selected = lang == "عربي",
                    onClick = { lang = if (lang == "عربي") "الكل" else "عربي" },
                    testTag = "res_lang_ar"
                )
                QaFilterChip(
                    text = "English ($enCount)",
                    selected = lang == "English",
                    onClick = { lang = if (lang == "English") "الكل" else "English" },
                    testTag = "res_lang_en"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                (listOf("الكل") + ResourcesData.RES_CATS).forEach { c ->
                    QaFilterChip(c, cat == c, { cat = c })
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                QaFilterChip("⭐ الأفضل للبداية", only == "top", { only = if (only == "top") "" else "top" })
                QaFilterChip("🔖 المحفوظة", only == "saved", { only = if (only == "saved") "" else "saved" })
                QaFilterChip("✅ المنجزة", only == "done", { only = if (only == "done") "" else "done" })
            }
        }

        if (sortedList.isEmpty()) {
            item {
                Text("لا نتائج مطابقة.", color = qa.muted, modifier = Modifier.padding(16.dp))
            }
        }

        items(sortedList, key = { it.id }) { r ->
            val mark = state.progress["r:${r.id}"] ?: ""
            SectionCard(modifier = Modifier.alpha(if (mark == "done") 0.7f else 1f)) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val scopeLabel = when {
                        r.isGeneral -> "عام للمنهج"
                        r.ch.isNotEmpty() -> "فصل ${r.ch.joinToString("،")}"
                        else -> "عام للمهارات"
                    }
                    val langLabel = if (r.lang == "ar") "عربي" else "EN"
                    listOf(langLabel, r.cat, r.level, scopeLabel).forEach { tag ->
                        val (bg, fg, bold) = when (tag) {
                            "عربي" -> Triple(qa.okBg, qa.ok, true)
                            "ملخصات ومستودعات" -> Triple(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.onPrimaryContainer,
                                true
                            )
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
                    if (r.top) Text("⭐", fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${r.title} ↗",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { openExternalUrl(context, r.url) }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(r.desc, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    QaFilterChip(
                        text = "🔖 حفظ",
                        selected = mark == "saved",
                        onClick = { viewModel.setProgress("r:${r.id}", if (mark == "saved") null else "saved") }
                    )
                    QaFilterChip(
                        text = "✅ أنجزته",
                        selected = mark == "done",
                        onClick = { viewModel.setProgress("r:${r.id}", if (mark == "done") null else "done") }
                    )
                }
            }
        }

        item {
            Text(
                text = "روابط الفيديو تفتح بحث يوتيوب لأن القنوات تتغير، اختر الأحدث والأعلى مشاهدة. تحقق دائماً من أن المصدر يتبع منهج v4.0.",
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted
            )
        }
    }
}
