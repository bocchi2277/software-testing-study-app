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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
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
    viewModel: QaViewModel
) {
    val qa = LocalQaColors.current
    val context = LocalContext.current

    var cat by remember { mutableStateOf("الكل") }
    var query by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("الكل") }
    var lang by remember { mutableStateOf("الكل") }
    var only by remember { mutableStateOf("") } // "", "top", "saved", "done"

    val list = remember(cat, query, level, lang, only, state.progress) {
        val s = query.trim().lowercase()
        ResourcesData.RESOURCES.filter { r ->
            (cat == "الكل" || r.cat == cat) &&
                (level == "الكل" || r.level == level) &&
                (lang == "الكل" || r.lang == (if (lang == "عربي") "ar" else "en")) &&
                (s.isEmpty() || r.title.lowercase().contains(s) || r.desc.lowercase().contains(s)) &&
                (only.isEmpty() || (if (only == "top") r.top else state.progress["r:${r.id}"] == only))
        }
    }

    val doneCount = ResourcesData.RESOURCES.count { state.progress["r:${it.id}"] == "done" }

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
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("الكل", "مبتدئ", "متوسط", "متقدم").forEach { lv ->
                    QaFilterChip(lv, level == lv, { level = lv })
                }
                listOf("الكل", "عربي", "English").forEach { lg ->
                    val selected = lang == lg && !(lg == "الكل")
                    if (lg != "الكل") {
                        QaFilterChip(lg, lang == lg, { lang = if (lang == lg) "الكل" else lg })
                    } else if (false) {
                        Unit
                    }
                    if (selected) Unit
                }
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

        if (list.isEmpty()) {
            item {
                Text("لا نتائج مطابقة.", color = qa.muted, modifier = Modifier.padding(16.dp))
            }
        }

        items(list, key = { it.id }) { r ->
            val mark = state.progress["r:${r.id}"] ?: ""
            SectionCard(modifier = Modifier.alpha(if (mark == "done") 0.7f else 1f)) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(r.cat, r.level, if (r.lang == "ar") "عربي" else "EN").forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = tag,
                                fontSize = 11.sp,
                                color = qa.muted,
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
