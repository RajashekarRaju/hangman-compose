package com.developersbreach.hangman.ui.game

sealed interface GameEvent {
    data object BackPressed : GameEvent
    data object ExitConfirmed : GameEvent
    data object ExitDismissed : GameEvent
    data object ToggleInstructionsDialog : GameEvent
    data class AlphabetClicked(val alphabetId: Int) : GameEvent
    data object WinDialogDismissed : GameEvent
    data object LostDialogDismissed : GameEvent
}
