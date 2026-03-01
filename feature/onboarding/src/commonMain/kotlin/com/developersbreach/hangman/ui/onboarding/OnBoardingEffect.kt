package com.developersbreach.hangman.ui.onboarding

sealed interface OnBoardingEffect {
    data object NavigateToGame : OnBoardingEffect
    data object NavigateToHistory : OnBoardingEffect
    data object NavigateToAchievements : OnBoardingEffect
    data object FinishActivity : OnBoardingEffect
    data class OpenIssueTracker(val url: String) : OnBoardingEffect
}
