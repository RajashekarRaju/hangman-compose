package com.developersbreach.hangman.ui.onboarding

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

sealed interface OnBoardingEvent {
    data object NavigateToGameClicked : OnBoardingEvent
    data object NavigateToHistoryClicked : OnBoardingEvent
    data object ExitClicked : OnBoardingEvent
    data object OpenDifficultyDialog : OnBoardingEvent
    data object DismissDifficultyDialog : OnBoardingEvent
    data class DifficultySliderPositionChanged(val sliderPosition: Float) : OnBoardingEvent
    data class DifficultyChanged(val difficulty: GameDifficulty) : OnBoardingEvent
    data object OpenCategoryDialog : OnBoardingEvent
    data object DismissCategoryDialog : OnBoardingEvent
    data class CategoryChanged(val category: GameCategory) : OnBoardingEvent
    data object OpenInstructionsDialog : OnBoardingEvent
    data object DismissInstructionsDialog : OnBoardingEvent
    data object ToggleBackgroundMusic : OnBoardingEvent
}