package com.developersbreach.hangman.ui.history

import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.game.core.LEVELS_PER_GAME

data class HistoryUiState(
    val gameHistoryList: List<HistoryListItemUiState> = emptyList()
) {
    val showDeleteIconInAppBar: Boolean
        get() = gameHistoryList.isNotEmpty()
}

data class HistoryListItemUiState(
    val history: HistoryRecord,
    val levelProgress: Float,
)

fun HistoryRecord.toHistoryListItemUiState(): HistoryListItemUiState {
    return HistoryListItemUiState(
        history = this,
        levelProgress = historyLevelProgress(gameLevel),
    )
}

private fun historyLevelProgress(level: Int): Float {
    if (level <= 0) return 0f
    return (level.coerceAtMost(LEVELS_PER_GAME)) / LEVELS_PER_GAME.toFloat()
}