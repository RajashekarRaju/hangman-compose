package com.developersbreach.hangman.ui.history

import com.developersbreach.hangman.repository.model.HistoryRecord

sealed interface HistoryEvent {
    data object NavigateUpClicked : HistoryEvent
    data object DeleteAllClicked : HistoryEvent
    data class DeleteHistoryItemClicked(val history: HistoryRecord) : HistoryEvent
}
