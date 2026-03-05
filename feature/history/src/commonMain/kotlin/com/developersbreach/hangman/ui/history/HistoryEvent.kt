package com.developersbreach.hangman.ui.history

import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.logging.AuditSpec

sealed interface HistoryEvent {
    data object NavigateUpClicked : HistoryEvent
    data object DeleteAllClicked : HistoryEvent
    data class DeleteHistoryItemClicked(val history: HistoryRecord) : HistoryEvent
}

internal fun HistoryEvent.auditSpec(current: HistoryUiState): AuditSpec? = when (this) {
    HistoryEvent.NavigateUpClicked,
    HistoryEvent.DeleteAllClicked,
    is HistoryEvent.DeleteHistoryItemClicked -> null
}
