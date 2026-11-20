package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.AppState
import com.example.ui.QaViewModel
import com.example.ui.components.ConfirmActionDialog
import com.example.ui.components.DynamicThemeToggleButton
import com.example.ui.components.PageHeader
import com.example.ui.components.SpacedRepetitionFlashcardDeck
import com.example.util.StatsUtils

@Composable
fun CardsScreen(
    state: AppState,
    viewModel: QaViewModel
) {
    val syncingFlashcards by viewModel.syncingFlashcards.collectAsStateWithLifecycle()
    var showResetConfirm by remember { mutableStateOf(false) }
    val deckCount = StatsUtils.effectiveFlashcards(state).size

    if (showResetConfirm) {
        ConfirmActionDialog(
            title = "تصفير تقدم البطاقات",
            message = "سيتم تصفير تقدم كل البطاقات. متأكد؟",
            confirmText = "تصفير",
            onConfirm = { viewModel.resetCards() },
            onDismiss = { showResetConfirm = false }
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PageHeader(
                title = "بطاقات المصطلحات",
                sub = "$deckCount بطاقة بنظام المراجعة المتباعدة (Leitner): ما تعرفه يتباعد ظهوره، وما تنساه يعود غداً.",
                trailing = {
                    DynamicThemeToggleButton(
                        onToggle = { isDark -> viewModel.toggleLightDarkTheme(isDark) },
                        compact = true,
                        testTag = "cards_theme_toggle"
                    )
                }
            )
        }

        item {
            SpacedRepetitionFlashcardDeck(
                state = state,
                isGuest = viewModel.isGuest,
                isSyncing = syncingFlashcards,
                onReviewCard = { cardId, known -> viewModel.reviewCard(cardId, known) },
                onRefreshFromFirestore = { viewModel.refreshFlashcardsFromFirestore() },
                onAddCustomCard = { term, question, answer, chapter, hint ->
                    viewModel.saveCustomFlashcard(term, question, answer, chapter, hint)
                },
                onDeleteCustomCard = { cardId -> viewModel.deleteCustomFlashcard(cardId) },
                onRequestResetProgress = { showResetConfirm = true }
            )
        }
    }
}

