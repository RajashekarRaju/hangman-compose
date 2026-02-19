package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class PersistedGameSettings(
    val gameDifficulty: GameDifficulty,
    val gameCategory: GameCategory,
)
