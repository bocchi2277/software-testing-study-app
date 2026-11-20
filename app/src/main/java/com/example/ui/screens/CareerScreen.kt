package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.data.CareerData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaProgressBar
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors
import kotlin.math.roundToInt

@Composable
fun CareerScreen(
    state: AppState,
    viewModel: QaViewModel
) {
    val qa = LocalQaColors.current
    val allItems = remember { CareerData.STAGES.flatMap { it.items } }
    val doneCount = allItems.count { state.progress["career:${it.id}"] == "1" }
    val pct = if (allItems.isNotEmpty()) ((doneCount.toDouble() / allItems.size) * 100.0).roundToInt() else 0
    val expandedInterview = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "المسار المهني 🚀",
                sub = "من شهادة ISTQB إلى وظيفة مختبر/مهندس جودة."
            )
        }

        // Overall Career Progress
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("تقدمك في المسار", fontWeight = FontWeight.Bold)
                    Text("$doneCount/${allItems.size}", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                QaProgressBar(pct = pct)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "الشهادة تفتح الباب، لكن أصحاب العمل يوظفون من يُثبت مهارته. اجمع بين الشهادة وأعمال حقيقية.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
            }
        }

        // Stages
        items(CareerData.STAGES, key = { it.id }) { stage ->
            SectionCard {
                Text(stage.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(stage.why, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                Spacer(modifier = Modifier.height(8.dp))
                stage.items.forEachIndexed { idx, item ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    val checked = state.progress["career:${item.id}"] == "1"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("career_item_${item.id}")
                            .clickable {
                                viewModel.setProgress("career:${item.id}", if (checked) null else "1")
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                viewModel.setProgress("career:${item.id}", if (checked) null else "1")
                            }
                        )
                        Text(
                            text = item.text,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = if (checked) qa.muted else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Portfolio Ideas
        item {
            SectionCard {
                Text(
                    text = "💼 أفكار لمحفظة الأعمال (Portfolio)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.PORTFOLIO.forEachIndexed { idx, p ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(p.title, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(p.desc, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                    }
                }
            }
        }

        // Interview Q&A
        item {
            SectionCard {
                Text(
                    text = "🎤 أسئلة مقابلات شائعة (${CareerData.INTERVIEW.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.INTERVIEW.forEachIndexed { idx, qaItem ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    val isOpen = expandedInterview[idx] == true
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("interview_q_$idx")
                            .clickable { expandedInterview[idx] = !isOpen }
                            .padding(vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${idx + 1}. ${qaItem.q}",
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isOpen) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                contentDescription = if (isOpen) "إخفاء الإجابة" else "عرض الإجابة",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        AnimatedVisibility(visible = isOpen) {
                            Text(
                                text = qaItem.a,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "حاول الإجابة بصوت عالٍ قبل فتح الجواب.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }

        // CV & LinkedIn Tips
        item {
            SectionCard {
                Text(
                    text = "📄 نصائح CV وLinkedIn",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.CV_TIPS.forEach { tip ->
                    Row(modifier = Modifier.padding(bottom = 6.dp)) {
                        Text("• ", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(tip, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Next ISTQB Certs
        item {
            SectionCard {
                Text(
                    text = "🏅 شهادات ISTQB التالية",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                CareerData.CERTS.forEachIndexed { idx, cert ->
                    if (idx > 0) HorizontalDivider(color = qa.border)
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        LtrContent {
                            Text(
                                text = cert.name,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(cert.desc, style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "تحقق من توفر كل شهادة ومتطلباتها على istqb.org.",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
        }
    }
}
