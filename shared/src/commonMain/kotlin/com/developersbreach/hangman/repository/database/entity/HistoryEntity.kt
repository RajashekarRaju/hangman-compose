package com.developersbreach.hangman.repository.database.entity

import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty

expect class HistoryEntity(
    gameId: String,
    gameScore: Int,
    gameLevel: Int,
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory,
    gameSummary: Boolean,
    gamePlayedTime: String,
    gamePlayedDate: String
) {
    val gameId: String
    val gameScore: Int
    val gameLevel: Int
    val gameDifficulty: GameDifficulty
    val gameCategory: GameCategory
    val gameSummary: Boolean
    val gamePlayedTime: String
    val gamePlayedDate: String
}
