package com.developersbreach.hangman.repository

import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest

interface GameSessionRepository {
    suspend fun saveCompletedGame(request: GameHistoryWriteRequest)
}
