package com.example.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.model.AppState

@Composable
fun SyllabusRoadmapCard(
    state: AppState,
    onOpenTopic: (String) -> Unit,
    onToggleModuleComplete: (chapterId: Int, completed: Boolean) -> Unit,
    onToggleTopicComplete: (topicId: String, completed: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    mutatingChapterIds: Set<Int> = emptySet(),
    initiallyExpandedChapterId: Int? = null
) {
    Roadmap(
        state = state,
        onOpenTopic = onOpenTopic,
        onToggleModuleComplete = onToggleModuleComplete,
        onToggleTopicComplete = onToggleTopicComplete,
        modifier = modifier,
        mutatingChapterIds = mutatingChapterIds,
        initiallyExpandedChapterId = initiallyExpandedChapterId
    )
}
