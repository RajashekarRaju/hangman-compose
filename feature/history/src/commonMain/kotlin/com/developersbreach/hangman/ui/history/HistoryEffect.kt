package com.developersbreach.hangman.ui.history

sealed interface HistoryEffect {
    data object NavigateUp : HistoryEffect
}
