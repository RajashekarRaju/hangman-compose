package com.developersbreach.hangman.ui.onboarding

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import kotlin.math.abs

data class OnBoardingUiState(
    val highScore: Int = 0,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val isBackgroundMusicPlaying: Boolean = false,
    val isDifficultyDialogOpen: Boolean = false,
    val isCategoryDialogOpen: Boolean = false,
    val isInstructionsDialogOpen: Boolean = false,
    val pendingDifficulty: GameDifficulty = GameDifficulty.EASY,
    val pendingDifficultySliderPosition: Float = 1f,
)

internal fun GameDifficulty.toSliderPosition(): Float {
    return DIFFICULTY_TO_SLIDER_VALUE[this]
        ?: DIFFICULTY_TO_SLIDER_VALUE.getValue(GameDifficulty.EASY)
}

internal fun Float.toGameDifficulty(): GameDifficulty {
    return DIFFICULTY_TO_SLIDER_VALUE
        .minByOrNull { (_, sliderValue) -> abs(sliderValue - this) }
        ?.key
        ?: GameDifficulty.EASY
}

private val DIFFICULTY_TO_SLIDER_VALUE = mapOf(
    GameDifficulty.EASY to 1f,
    GameDifficulty.MEDIUM to 2f,
    GameDifficulty.HARD to 3f,
    GameDifficulty.VERY_HARD to 4f,
)