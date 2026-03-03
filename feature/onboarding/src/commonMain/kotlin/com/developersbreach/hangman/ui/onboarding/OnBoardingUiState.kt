package com.developersbreach.hangman.ui.onboarding

data class OnBoardingUiState(
    val highScore: Int = 0,
    val hasUnreadAchievements: Boolean = false,
)