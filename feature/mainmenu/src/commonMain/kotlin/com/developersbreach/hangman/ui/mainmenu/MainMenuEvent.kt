package com.developersbreach.hangman.ui.mainmenu

import com.developersbreach.hangman.logging.AuditSpec

sealed interface MainMenuEvent {
    data object NavigateToGameClicked : MainMenuEvent
    data object NavigateToSettingsClicked : MainMenuEvent
    data object NavigateToHistoryClicked : MainMenuEvent
    data object NavigateToAchievementsClicked : MainMenuEvent
    data object NavigateToGameGuideClicked : MainMenuEvent
    data object ExitClicked : MainMenuEvent
    data object ReportIssueClicked : MainMenuEvent
}

internal fun MainMenuEvent.auditSpec(): AuditSpec? = when (this) {
    MainMenuEvent.NavigateToGameClicked,
    MainMenuEvent.NavigateToSettingsClicked,
    MainMenuEvent.NavigateToHistoryClicked,
    MainMenuEvent.ExitClicked,
    MainMenuEvent.NavigateToAchievementsClicked,
    MainMenuEvent.NavigateToGameGuideClicked -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "main_menu",
            "destination" to "game_guide",
        ),
    )
    MainMenuEvent.ReportIssueClicked -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "main_menu",
            "destination" to "issue_tracker",
        ),
    )
}
