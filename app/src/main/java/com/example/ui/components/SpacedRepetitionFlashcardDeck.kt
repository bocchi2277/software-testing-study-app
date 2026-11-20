package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SyllabusData
import com.example.db.QaRepository
import com.example.model.AppState
import com.example.model.CardState
import com.example.model.IstqbFlashcard
import com.example.ui.theme.JetBrainsMono
import com.example.ui.theme.LocalQaColors
import com.example.util.DateUtils
import com.example.util.StatsUtils
import kotlin.math.min
import kotlin.math.roundToInt

private val BOX_INTERVAL_LABELS = listOf("", "1ي", "2ي", "4ي", "8ي", "16ي")

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpacedRepetitionFlashcardDeck(
    state: AppState,
    isGuest: Boolean,
    isSyncing: Boolean,
    onReviewCard: (cardId: String, known: Boolean) -> Unit,
    onRefreshFromFirestore: () -> Unit,
    onAddCustomCard: (term: String, question: String, answer: String, chapter: Int, hint: String?) -> Unit,
    onDeleteCustomCard: (cardId: String) -> Unit,
    onRequestResetProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    val today = DateUtils.todayStr()
    val deck = remember(state.flashcards) { StatsUtils.effectiveFlashcards(state) }

    var chFilter by remember { mutableStateOf("all") }
    var scope by remember { mutableStateOf("due") }
    var started by remember { mutableStateOf(false) }
    val queue = remember { mutableStateListOf<String>() }
    var idx by remember { mutableIntStateOf(0) }
    var flipped by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    var sessionKnown by remember { mutableIntStateOf(0) }
    var sessionAgain by remember { mutableIntStateOf(0) }
    var chMenuExpanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }

    val pool = remember(deck, chFilter, scope, state.cards, today) {
        deck.filter { chFilter == "all" || it.chapter == chFilter.toIntOrNull() }
            .filter { c ->
                when (scope) {
                    "all" -> true
                    "custom" -> c.source == "custom"
                    else -> {
                        val st = state.cards[c.id]
                        st == null || st.due <= today
                    }
                }
            }
    }

    val boxes = (1..5).map { b -> deck.count { (state.cards[it.id]?.box ?: 0) == b } }
    val freshCount = deck.count { !state.cards.containsKey(it.id) }
    val masteredCount = deck.count { (state.cards[it.id]?.box ?: 0) >= 4 }
    val masteredPct = if (deck.isEmpty()) 0 else ((masteredCount.toDouble() / deck.size) * 100.0).roundToInt()

    fun startReview() {
        queue.clear()
        queue.addAll(pool.map { it.id }.shuffled())
        idx = 0
        flipped = false
        showHint = false
        sessionKnown = 0
        sessionAgain = 0
        started = true
    }

    val currentCard = if (started) queue.getOrNull(idx)?.let { id -> deck.find { it.id == id } } else null
    val finished = started && currentCard == null

    if (showAddDialog) {
        AddIstqbFlashcardDialog(
            onSave = { term, question, answer, chapter, hint ->
                onAddCustomCard(term, question, answer, chapter, hint)
                showAddDialog = false
            },
            onDismiss = { showAddDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("spaced_repetition_flashcard_deck"),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Firestore Sync & Deck Summary Card
        SectionCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (isGuest) Icons.Default.CloudOff else Icons.Default.CloudDone,
                        contentDescription = null,
                        tint = if (isGuest) qa.muted else qa.ok,
                        modifier = Modifier.size(18.dp)
                    )
                    Column {
                        Text(
                            text = if (isGuest) "مصطلحات ISTQB (وضع محلي)" else "متزامن مع Firestore",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${deck.size} بطاقة سؤال وجواب · ${StatsUtils.dueCards(state).size} مستحقة اليوم",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                }
                IconButton(
                    onClick = onRefreshFromFirestore,
                    enabled = !isSyncing,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("flashcards_refresh_button")
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "تحديث البطاقات من Firestore",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                OutlinedButton(
                    onClick = { showAddDialog = true },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("flashcards_add_custom_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("بطاقة", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Leitner Box Breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    Text("$freshCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = qa.muted)
                    Text("جديدة", fontSize = 11.sp, color = qa.muted)
                }
                boxes.forEachIndexed { i, count ->
                    val boxNum = i + 1
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$count",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (boxNum >= 4) qa.ok else MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "ص$boxNum (${BOX_INTERVAL_LABELS[boxNum]})",
                            fontSize = 10.sp,
                            color = qa.muted,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            QaProgressBar(pct = masteredPct, color = if (masteredPct >= 80) qa.ok else MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "الإتقان: $masteredCount/${deck.size} بطاقة في الصناديق 4–5 (تباعد 8–16 يوماً).",
                style = MaterialTheme.typography.labelSmall,
                color = qa.muted
            )
        }

        // Chapter & Scope Filters
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box {
                OutlinedButton(
                    onClick = { chMenuExpanded = true },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
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
            QaFilterChip("المستحقة اليوم", scope == "due", { scope = "due"; started = false }, testTag = "scope_due")
            QaFilterChip("الكل", scope == "all", { scope = "all"; started = false }, testTag = "scope_all")
            if (deck.any { it.source == "custom" }) {
                QaFilterChip("المضافة يدوياً", scope == "custom", { scope = "custom"; started = false }, testTag = "scope_custom")
            }
        }

        // Start / Idle State
        if (!started) {
            SectionCard {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Style,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${pool.size} بطاقة ${when (scope) {
                            "due" -> "مستحقة للمراجعة"
                            "custom" -> "مضافة يدوياً"
                            else -> "في هذه المجموعة"
                        }}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "نظام التكرار المتباعد يعرض أسئلة المصطلحات الأقل إتقاناً بشكل متكرر حتى تثبت في ذاكرتك.",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        enabled = pool.isNotEmpty(),
                        onClick = { startReview() },
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("cards_start_button")
                    ) {
                        Text("ابدأ المراجعة")
                    }
                    if (pool.isEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("أحسنت! لا توجد بطاقات مستحقة في هذا القسم 🎉", color = qa.ok, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Active Flashcard Review Stage
        if (started && currentCard != null) {
            val cardState = state.cards[currentCard.id]
            val currentBox = (cardState?.box ?: 1).coerceIn(1, 5)
            val nextBoxIfKnown = minOf(5, currentBox + 1)
            val nextDaysIfKnown = QaRepository.BOX_DAYS[nextBoxIfKnown]

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${min(idx + 1, queue.size)} / ${queue.size}",
                    fontFamily = JetBrainsMono,
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
                Text(
                    text = "✅ $sessionKnown · 🔁 $sessionAgain",
                    style = MaterialTheme.typography.bodyMedium,
                    color = qa.muted
                )
            }

            FlashcardStageCard(
                card = currentCard,
                cardState = cardState,
                flipped = flipped,
                showHint = showHint,
                onToggleFlip = { flipped = !flipped },
                onToggleHint = { showHint = !showHint },
                onDeleteCustom = if (currentCard.source == "custom") {
                    {
                        onDeleteCustomCard(currentCard.id)
                        idx++
                        flipped = false
                        showHint = false
                    }
                } else null
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    enabled = flipped,
                    onClick = {
                        onReviewCard(currentCard.id, false)
                        sessionAgain++
                        queue.add(currentCard.id)
                        idx++
                        flipped = false
                        showHint = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("card_again_button")
                ) {
                    Text("🔁 أعد لاحقاً (ص1)", maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Button(
                    enabled = flipped,
                    onClick = {
                        onReviewCard(currentCard.id, true)
                        sessionKnown++
                        idx++
                        flipped = false
                        showHint = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                        .testTag("card_known_button")
                ) {
                    Text(
                        text = "✅ عرفتها (+${nextDaysIfKnown}ي)",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Session Finished Summary
        if (finished) {
            SectionCard(borderColor = qa.ok.copy(alpha = 0.6f)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🎉", fontSize = 44.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "انتهت الجلسة: عرفت $sessionKnown وأعدت $sessionAgain.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { started = false },
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Text("رجوع")
                    }
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            OutlinedButton(
                onClick = onRequestResetProgress,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad),
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text("تصفير تقدم البطاقات")
            }
        }
    }
}

@Composable
private fun FlashcardStageCard(
    card: IstqbFlashcard,
    cardState: CardState?,
    flipped: Boolean,
    showHint: Boolean,
    onToggleFlip: () -> Unit,
    onToggleHint: () -> Unit,
    onDeleteCustom: (() -> Unit)?
) {
    val qa = LocalQaColors.current
    val borderColor = if (flipped) MaterialTheme.colorScheme.primary else qa.border
    val bgColor = if (flipped) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f) else MaterialTheme.colorScheme.surface

    SectionCard(
        onClick = onToggleFlip,
        borderColor = borderColor,
        containerColor = bgColor,
        modifier = Modifier.testTag("flashcard_surface")
    ) {
        // Card metadata header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, qa.border)
                ) {
                    Text(
                        text = "الفصل ${card.chapter}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = if ((cardState?.box ?: 0) >= 4) qa.okBg else MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Text(
                        text = if (cardState == null) "جديدة" else "صندوق ${cardState.box} · ${cardState.reps} مراجعة",
                        style = MaterialTheme.typography.labelSmall,
                        color = if ((cardState?.box ?: 0) >= 4) qa.ok else MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!card.hint.isNullOrBlank()) {
                    IconButton(
                        onClick = onToggleHint,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "عرض تلميح",
                            tint = if (showHint) qa.warn else qa.muted,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                if (onDeleteCustom != null) {
                    IconButton(
                        onClick = onDeleteCustom,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "حذف البطاقة",
                            tint = qa.bad,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = showHint && !card.hint.isNullOrBlank()) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = qa.warnBg,
                border = BorderStroke(1.dp, qa.warn.copy(alpha = 0.45f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "💡 ${card.hint}",
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.warn,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp)
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(targetState = flipped, label = "card_flip") { isBack ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (isBack) "الإجابة والتعريف" else "سؤال المراجعة المتباعدة",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (isBack) {
                        LtrContent {
                            Text(
                                text = card.term,
                                style = MaterialTheme.typography.titleSmall,
                                fontFamily = JetBrainsMono,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = card.answer,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = card.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        LtrContent {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    text = card.term,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "اختبر تذكّرك ثم اضغط على البطاقة لإظهار الإجابة",
                            style = MaterialTheme.typography.labelSmall,
                            color = qa.muted
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickFlashcardPracticeCard(
    state: AppState,
    isGuest: Boolean,
    isSyncing: Boolean,
    onReviewCard: (cardId: String, known: Boolean) -> Unit,
    onRefreshFromFirestore: () -> Unit,
    onOpenAllCards: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qa = LocalQaColors.current
    val today = DateUtils.todayStr()
    val deck = remember(state.flashcards) { StatsUtils.effectiveFlashcards(state) }
    val dueCards = remember(deck, state.cards, today) {
        deck.filter { c ->
            val st = state.cards[c.id]
            st == null || st.due <= today
        }
    }

    val activeCard = dueCards.firstOrNull() ?: deck.firstOrNull()
    var flipped by remember(activeCard?.id) { mutableStateOf(false) }

    SectionCard(modifier = modifier.testTag("dashboard_quick_flashcard")) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "🃏 بطاقة مصطلحات ISTQB السريعة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isGuest) {
                        "${dueCards.size} بطاقة مستحقة اليوم (التصفح الحر)"
                    } else {
                        "${dueCards.size} مستحقة اليوم · مجلوبة من Firestore"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = qa.muted
                )
            }
            IconButton(
                onClick = onRefreshFromFirestore,
                enabled = !isSyncing,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                if (isSyncing) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "تحديث الأسئلة من Firestore",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (activeCard != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { flipped = !flipped }
                    .testTag("dashboard_flashcard_flip"),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(1.dp, if (flipped) MaterialTheme.colorScheme.primary else qa.border)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (flipped) "التعريف:" else activeCard.question,
                        style = MaterialTheme.typography.bodySmall,
                        color = qa.muted,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Crossfade(targetState = flipped, label = "quick_card_flip") { isBack ->
                        if (isBack) {
                            Text(
                                text = activeCard.answer,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            LtrContent {
                                Text(
                                    text = activeCard.term,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (flipped) "قيّم تذكرك بالأسفل:" else "اضغط لعرض الإجابة",
                        style = MaterialTheme.typography.labelSmall,
                        color = qa.muted
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    enabled = flipped,
                    onClick = {
                        onReviewCard(activeCard.id, false)
                        flipped = false
                    },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = qa.bad),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                ) {
                    Text("🔁 أعد", maxLines = 1)
                }
                Button(
                    enabled = flipped,
                    onClick = {
                        onReviewCard(activeCard.id, true)
                        flipped = false
                    },
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                ) {
                    Text("✅ عرفتها", maxLines = 1)
                }
                TextButton(
                    onClick = onOpenAllCards,
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("كل البطاقات")
                }
            }
        }
    }
}

@Composable
private fun AddIstqbFlashcardDialog(
    onSave: (term: String, question: String, answer: String, chapter: Int, hint: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var term by remember { mutableStateOf("") }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf("") }
    var chapter by remember { mutableIntStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("إضافة بطاقة مصطلح ISTQB", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = term,
                    onValueChange = { term = it.take(120) },
                    label = { Text("المصطلح (Term)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = question,
                    onValueChange = { question = it.take(400) },
                    label = { Text("السؤال (اختياري)") },
                    placeholder = { Text("ما المقصود بهذا المصطلح؟") },
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it.take(600) },
                    label = { Text("الإجابة والتعريف") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hint,
                    onValueChange = { hint = it.take(200) },
                    label = { Text("تلميح سريع (اختياري)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                HorizontalDivider()
                Text("الفصل المرتبط:", style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    (1..6).forEach { ch ->
                        val selected = chapter == ch
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .minimumInteractiveComponentSize()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { chapter = ch },
                            shape = RoundedCornerShape(8.dp),
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$ch",
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = term.isNotBlank() && answer.isNotBlank(),
                onClick = {
                    onSave(
                        term.trim(),
                        question.trim(),
                        answer.trim(),
                        chapter,
                        hint.trim().ifEmpty { null }
                    )
                }
            ) {
                Text("حفظ في Firestore", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
