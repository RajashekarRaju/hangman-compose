package com.developersbreach.hangman.repository

import androidx.lifecycle.LiveData
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

interface HistoryStorage {
    fun getCompleteGameHistory(): LiveData<List<HistoryEntity>>
    fun getHighestScore(): LiveData<Int?>
    suspend fun saveNewGameToHistory(historyEntity: HistoryEntity)
    suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity)
    suspend fun deleteAllGamesHistory()
}
