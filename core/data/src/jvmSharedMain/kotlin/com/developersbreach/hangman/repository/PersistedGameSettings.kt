package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.ui.theme.ThemePaletteId

data class PersistedGameSettings(
    val gameDifficulty: GameDifficulty,
    val gameCategory: GameCategory,
    val themePaletteId: ThemePaletteId,
)
