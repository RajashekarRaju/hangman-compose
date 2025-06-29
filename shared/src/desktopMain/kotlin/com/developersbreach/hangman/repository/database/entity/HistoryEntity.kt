package com.developersbreach.hangman.repository.database.entity

import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty

actual data class HistoryEntity(
    actual val gameId: String,
    actual val gameScore: Int,
    actual val gameLevel: Int,
    actual val gameDifficulty: GameDifficulty,
    actual val gameCategory: GameCategory,
    actual val gameSummary: Boolean,
    actual val gamePlayedTime: String,
    actual val gamePlayedDate: String
)
