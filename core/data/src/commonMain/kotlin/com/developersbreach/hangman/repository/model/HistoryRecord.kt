package com.developersbreach.hangman.repository.model

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintType

data class HistoryRecord(
    val gameId: String,
    val gameScore: Int,
    val gameLevel: Int,
    val gameDifficulty: GameDifficulty,
    val gameCategory: GameCategory,
    val gameSummary: Boolean,
    val gamePlayedTime: String,
    val gamePlayedDate: String,
    val hintsUsed: Int = 0,
    val hintTypesUsed: List<HintType> = emptyList(),
)
