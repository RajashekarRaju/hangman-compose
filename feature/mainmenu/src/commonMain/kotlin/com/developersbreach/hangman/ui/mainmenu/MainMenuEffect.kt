package com.developersbreach.hangman.ui.mainmenu

sealed interface MainMenuEffect {
    data object NavigateToGame : MainMenuEffect
    data object NavigateToSettings : MainMenuEffect
    data object NavigateToHistory : MainMenuEffect
    data object NavigateToAchievements : MainMenuEffect
    data object NavigateToGameGuide : MainMenuEffect
    data object FinishActivity : MainMenuEffect
    data class OpenIssueTracker(val url: String) : MainMenuEffect
}
