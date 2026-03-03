package com.developersbreach.hangman.ui.onboarding

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.ui.settings.labelRes
import org.jetbrains.compose.resources.StringResource

data class OnBoardingUiState(
    val highScore: Int = 0,
    val hasUnreadAchievements: Boolean = false,
    val gameDifficultyLabelRes: StringResource = GameDifficulty.EASY.labelRes(),
    val gameCategoryLabelRes: StringResource = GameCategory.COUNTRIES.labelRes(),
    val isBackgroundMusicPlaying: Boolean = false,
    val isInstructionsDialogOpen: Boolean = false,
)