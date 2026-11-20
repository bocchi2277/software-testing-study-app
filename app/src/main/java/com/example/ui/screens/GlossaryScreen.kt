package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CardsData
import com.example.data.SyllabusData
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.SectionCard
import com.example.ui.components.openExternalUrl
import com.example.ui.theme.LocalQaColors

@Composable
fun GlossaryScreen() {
    val qa = LocalQaColors.current
    val context = LocalContext.current
    var query by remember { mutableStateOf("") }
    var chFilter by remember { mutableIntStateOf(0) }

    val list = remember(query, chFilter) {
        val s = query.trim().lowercase()
        CardsData.CARDS
            .filter { c ->
                (chFilter == 0 || c.ch == chFilter) &&
                    (s.isEmpty() || c.term.lowercase().contains(s) || c.def.contains(s))
            }
            .sortedBy { it.term.lowercase() }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "قاموس المصطلحات",
                sub = "${CardsData.CARDS.size} مصطلحاً. المصدر الرسمي الكامل: glossary.istqb.org"
            )
        }

        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("ابحث بالإنجليزية أو العربية…") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "بحث") },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Filled.Close, contentDescription = "مسح البحث")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("glossary_search_input")
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QaFilterChip(
                    text = "كل الفصول",
                    selected = chFilter == 0,
                    onClick = { chFilter = 0 },
                    testTag = "glossary_ch_all"
                )
                SyllabusData.CHAPTERS.forEach { c ->
                    QaFilterChip(
                        text = "الفصل ${c.id}",
                        selected = chFilter == c.id,
                        onClick = { chFilter = c.id },
                        testTag = "glossary_ch_${c.id}"
                    )
                }
            }
        }

        item {
            SectionCard {
                if (list.isEmpty()) {
                    Text(
                        text = "لا نتائج.",
                        color = qa.muted
                    )
                } else {
                    list.forEachIndexed { idx, card ->
                        if (idx > 0) HorizontalDivider(color = qa.border)
                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                            LtrContent {
                                Text(
                                    text = card.term,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = card.def,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "(فصل ${card.ch})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = qa.muted,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            TextButton(
                onClick = { openExternalUrl(context, "https://glossary.istqb.org") },
                modifier = Modifier.testTag("glossary_external_link")
            ) {
                Text("القاموس الرسمي الكامل ↗")
            }
        }
    }
}
