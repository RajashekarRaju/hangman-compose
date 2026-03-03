package com.developersbreach.hangman.ui.mainmenu

sealed interface MainMenuEvent {
    data object NavigateToGameClicked : MainMenuEvent
    data object NavigateToSettingsClicked : MainMenuEvent
    data object NavigateToHistoryClicked : MainMenuEvent
    data object NavigateToAchievementsClicked : MainMenuEvent
    data object NavigateToGameGuideClicked : MainMenuEvent
    data object ExitClicked : MainMenuEvent
    data object ReportIssueClicked : MainMenuEvent
}