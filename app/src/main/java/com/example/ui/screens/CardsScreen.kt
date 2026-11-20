package com.example.ui.screens

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CardsData
import com.example.data.SyllabusData
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.ConfirmActionDialog
import com.example.ui.components.LtrContent
import com.example.ui.components.PageHeader
import com.example.ui.components.QaFilterChip
import com.example.ui.components.QaProgressBar
import com.example.ui.components.SectionCard
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import kotlin.math.min
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CardsScreen(
    state: AppState,
    viewModel: QaViewModel
) {
    val qa = LocalQaColors.current
    val today = DateUtils.todayStr()

    var chFilter by remember { mutableStateOf("all") }
    var scope by remember { mutableStateOf("due") }
    var started by remember { mutableStateOf(false) }
    val queue = remember { mutableStateListOf<String>() }
    var idx by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var sessionKnown by remember { mutableIntStateOf(0) }
    var sessionAgain by remember { mutableIntStateOf(0) }
    var chMenuExpanded by remember { mutableStateOf(false) }
    var showResetConfirm by remember { mutableStateOf(false) }

    val pool = remember(chFilter, scope, state.cards, today) {
        CardsData.CARDS.filter { chFilter == "all" || it.ch == chFilter.toIntOrNull() }
            .filter { c ->
                if (scope == "all") true
                else {
                    val st = state.cards[c.id]
                    st == null || st.due <= today
                }
            }
    }

    val boxes = (1..5).map { b -> CardsData.CARDS.count { (state.cards[it.id]?.box ?: 0) == b } }
    val freshCount = CardsData.CARDS.count { !state.cards.containsKey(it.id) }
    val masteredPct = ((CardsData.CARDS.count { (state.cards[it.id]?.box ?: 0) >= 4 }.toDouble() / CardsData.CARDS.size) * 100.0).roundToInt()

    if (showResetConfirm) {
        ConfirmActionDialog(
            title = "تصفير تقدم البطاقات",
            message = "سيتم تصفير تقدم كل البطاقات. متأكد؟",
            confirmText = "تصفير",
            onConfirm = {
                viewModel.resetCards()
                started = false
            },
            onDismiss = { showResetConfirm = false }
        )
    }

    fun startReview() {
        queue.clear()
        queue.addAll(pool.map { it.id }.shuffled())
        idx = 0
        flipped = false
        sessionKnown = 0
        sessionAgain = 0
        started = true
    }

    val currentCard = if (started) queue.getOrNull(idx)?.let { id -> CardsData.CARDS.find { it.id == id } } else null
    val finished = started && currentCard == null

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "بطاقات المصطلحات",
                sub = "${CardsData.CARDS.size} بطاقة بنظام المراجعة المتباعدة (Leitner): ما تعرفه يتباعد ظهوره، وما تنساه يعود غداً."
            )
        }

        // Leitner Box Summary
        item {
            SectionCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text("$freshCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = qa.muted)
                        Text("جديدة", fontSize = 11.sp, color = qa.muted)
                    }
                    boxes.forEachIndexed { i, count ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                            Text("$count", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("صندوق ${i + 1}", fontSize = 11.sp, color = qa.muted)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                QaProgressBar(pct = masteredPct)
                Spacer(modifier = Modifier.height(4.dp))
                Text("الصناديق 4–5 تعني أنك تحفظها جيداً.", style = MaterialTheme.typography.labelSmall, color = qa.muted)
            }
        }

        // Filters
        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box {
                    OutlinedButton(onClick = { chMenuExpanded = true }) {
                        val label = if (chFilter == "all") "كل الفصول"
                        else chFilter.toIntOrNull()?.let { SyllabusData.findChapter(it) }?.let { SyllabusData.chapterLabel(it) } ?: "كل الفصول"
                        Text(label)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "اختر الفصل")
                    }
                    DropdownMenu(expanded = chMenuExpanded, onDismissRequest = { chMenuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("كل الفصول") },
                            onClick = { chFilter = "all"; started = false; chMenuExpanded = false }
                        )
                        SyllabusData.CHAPTERS.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(SyllabusData.chapterLabel(c)) },
                                onClick = { chFilter = c.id.toString(); started = false; chMenuExpanded = false }
                            )
                        }
                    }
                }
                QaFilterChip("المستحقة اليوم", scope == "due", { scope = "due"; started = false })
                QaFilterChip("الكل", scope == "all", { scope = "all"; started = false })
            }
        }

        if (!started) {
            item {
                SectionCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${pool.size} بطاقة ${if (scope == "due") "مستحقة" else "في هذه المجموعة"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            enabled = pool.isNotEmpty(),
                            onClick = { startReview() },
                            modifier = Modifier.testTag("cards_start_button")
                        ) {
                            Text("ابدأ المراجعة")
                        }
                        if (pool.isEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("أحسنت! لا شيء مستحق الآن 🎉", color = qa.ok, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }

        if (started && currentCard != null) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${min(idx + 1, queue.size)} / ${queue.size}", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                    Text("✅ $sessionKnown · 🔁 $sessionAgain", style = MaterialTheme.typography.bodyMedium, color = qa.muted)
                }
                Spacer(modifier = Modifier.height(8.dp))
                SectionCard(
                    onClick = { flipped = !flipped },
                    modifier = Modifier.testTag("flashcard_surface")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 210.dp)
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Crossfade(targetState = flipped, label = "card_flip") { isBack ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "الفصل ${currentCard.ch} · ${if (isBack) "التعريف" else "المصطلح"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = qa.muted
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                if (isBack) {
                                    Text(
                                        text = currentCard.def,
                                        style = MaterialTheme.typography.titleMedium,
                                        textAlign = TextAlign.Center
                                    )
                                } else {
                                    LtrContent {
                                        Text(
                                            text = currentCard.term,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.primary,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "فكّر في التعريف ثم اضغط للقلب",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = qa.muted
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        enabled = flipped,
                        onClick = {
                            viewModel.reviewCard(currentCard.id, known = false)
                            sessionAgain++
                            queue.add(currentCard.id)
                            idx++
                            flipped = false
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_again_button")
                    ) {
                        Text("🔁 لم أعرفها")
                    }
                    Button(
                        enabled = flipped,
                        onClick = {
                            viewModel.reviewCard(currentCard.id, known = true)
                            sessionKnown++
                            idx++
                            flipped = false
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("card_known_button")
                    ) {
                        Text("✅ عرفتها")
                    }
                }
            }
        }

        if (finished) {
            item {
                SectionCard {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "انتهت الجلسة: عرفت $sessionKnown وأعدت $sessionAgain.",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { started = false }) { Text("رجوع") }
                    }
                }
            }
        }

        item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                OutlinedButton(
                    onClick = { showResetConfirm = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad)
                ) {
                    Text("تصفير تقدم البطاقات")
                }
            }
        }
    }
}
