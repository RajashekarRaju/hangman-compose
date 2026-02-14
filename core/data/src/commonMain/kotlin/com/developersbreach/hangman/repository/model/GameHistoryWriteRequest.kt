package com.developersbreach.hangman.repository.model

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class GameHistoryWriteRequest(
    val gameScore: Int,
    val gameLevel: Int,
    val gameSummary: Boolean,
    val gameDifficulty: GameDifficulty,
    val gameCategory: GameCategory,
)
