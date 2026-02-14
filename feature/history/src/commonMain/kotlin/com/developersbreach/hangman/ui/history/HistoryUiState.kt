package com.developersbreach.hangman.ui.history

import com.developersbreach.hangman.repository.model.HistoryRecord

data class HistoryUiState(
    val gameHistoryList: List<HistoryRecord> = emptyList()
) {
    val showDeleteIconInAppBar: Boolean
        get() = gameHistoryList.isNotEmpty()
}
