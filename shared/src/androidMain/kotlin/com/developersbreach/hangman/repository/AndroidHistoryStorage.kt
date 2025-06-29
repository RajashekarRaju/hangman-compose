package com.developersbreach.hangman.repository

import kotlinx.coroutines.flow.Flow
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

class AndroidHistoryStorage(private val database: GameDatabase) : HistoryStorage {
    override fun getCompleteGameHistory(): Flow<List<HistoryEntity>> =
        database.historyDao.getCompleteGameHistory()

    override fun getHighestScore(): Flow<Int?> =
        database.historyDao.getHighestScoreFromHistory()

    override suspend fun saveNewGameToHistory(historyEntity: HistoryEntity) {
        database.historyDao.saveNewGameToHistory(historyEntity)
    }

    override suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity) {
        database.historyDao.deleteSingleGameHistory(historyEntity)
    }

    override suspend fun deleteAllGamesHistory() {
        database.historyDao.deleteAllGamesHistory()
    }
}
