package com.developersbreach.hangman.repository

import kotlinx.coroutines.flow.Flow
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

interface HistoryStorage {
    fun getCompleteGameHistory(): Flow<List<HistoryEntity>>
    fun getHighestScore(): Flow<Int?>
    suspend fun saveNewGameToHistory(historyEntity: HistoryEntity)
    suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity)
    suspend fun deleteAllGamesHistory()
}
