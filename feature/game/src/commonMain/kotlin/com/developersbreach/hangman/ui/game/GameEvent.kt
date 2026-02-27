package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.HintType

sealed interface GameEvent {
    data object BackPressed : GameEvent
    data object ExitConfirmed : GameEvent
    data object ExitDismissed : GameEvent
    data object ToggleInstructionsDialog : GameEvent
    data class HintSelected(val hintType: HintType) : GameEvent
    data object DismissHintFeedbackDialog : GameEvent
    data class AlphabetClicked(val alphabetId: Int) : GameEvent
    data object WinDialogDismissed : GameEvent
    data object LostDialogDismissed : GameEvent
    data object AchievementBannerConsumed : GameEvent
}
