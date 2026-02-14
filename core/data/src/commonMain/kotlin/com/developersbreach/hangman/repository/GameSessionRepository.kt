package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.Words
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest

interface GameSessionRepository {
    fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty,
        gameCategory: GameCategory,
    ): List<Words>

    suspend fun saveCompletedGame(request: GameHistoryWriteRequest)
}
