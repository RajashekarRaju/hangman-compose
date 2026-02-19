package com.developersbreach.hangman.ui.onboarding

sealed interface OnBoardingEvent {
    data object NavigateToGameClicked : OnBoardingEvent
    data object NavigateToHistoryClicked : OnBoardingEvent
    data object ExitClicked : OnBoardingEvent
    data class DifficultyChanged(val sliderPosition: Float) : OnBoardingEvent
    data class CategoryChanged(val categoryId: Int) : OnBoardingEvent
    data object ToggleBackgroundMusic : OnBoardingEvent
}