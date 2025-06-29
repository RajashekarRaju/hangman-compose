package com.developersbreach.hangman.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

class DesktopHistoryStorage : HistoryStorage {
    private val historyList = mutableListOf<HistoryEntity>()
    private val historyFlow = MutableStateFlow<List<HistoryEntity>>(historyList)
    private val highestScore = MutableStateFlow<Int?>(null)

    override fun getCompleteGameHistory(): Flow<List<HistoryEntity>> = historyFlow

    override fun getHighestScore(): Flow<Int?> = highestScore

    override suspend fun saveNewGameToHistory(historyEntity: HistoryEntity) {
        historyList.add(historyEntity)
        historyFlow.value = historyList.toList()
        highestScore.value = historyList.maxOfOrNull { it.gameScore }
    }

    override suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity) {
        historyList.remove(historyEntity)
        historyFlow.value = historyList.toList()
        highestScore.value = historyList.maxOfOrNull { it.gameScore }
    }

    override suspend fun deleteAllGamesHistory() {
        historyList.clear()
        historyFlow.value = emptyList()
        highestScore.value = null
    }
}
