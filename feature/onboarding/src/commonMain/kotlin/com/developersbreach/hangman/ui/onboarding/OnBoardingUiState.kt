package com.developersbreach.hangman.ui.onboarding

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class OnBoardingUiState(
    val highScore: Int = 0,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val isBackgroundMusicPlaying: Boolean = false
)
