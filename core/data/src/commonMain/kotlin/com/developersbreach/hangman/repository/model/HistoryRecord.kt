package com.developersbreach.hangman.repository.model

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class HistoryRecord(
    val gameId: String,
    val gameScore: Int,
    val gameLevel: Int,
    val gameDifficulty: GameDifficulty,
    val gameCategory: GameCategory,
    val gameSummary: Boolean,
    val gamePlayedTime: String,
    val gamePlayedDate: String
)
