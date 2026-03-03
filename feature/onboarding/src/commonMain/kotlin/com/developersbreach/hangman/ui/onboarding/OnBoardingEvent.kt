package com.developersbreach.hangman.ui.onboarding

sealed interface OnBoardingEvent {
    data object NavigateToGameClicked : OnBoardingEvent
    data object NavigateToSettingsClicked : OnBoardingEvent
    data object NavigateToHistoryClicked : OnBoardingEvent
    data object NavigateToAchievementsClicked : OnBoardingEvent
    data object NavigateToGameGuideClicked : OnBoardingEvent
    data object ExitClicked : OnBoardingEvent
    data object ReportIssueClicked : OnBoardingEvent
}