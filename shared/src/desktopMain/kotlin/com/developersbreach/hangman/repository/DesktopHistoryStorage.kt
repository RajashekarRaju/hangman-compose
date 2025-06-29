package com.developersbreach.hangman.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

class DesktopHistoryStorage : HistoryStorage {
    private val historyList = mutableListOf<HistoryEntity>()
    private val historyLiveData = MutableLiveData<List<HistoryEntity>>(historyList)
    private val highestScore = MutableLiveData<Int?>(null)

    override fun getCompleteGameHistory(): LiveData<List<HistoryEntity>> = historyLiveData

    override fun getHighestScore(): LiveData<Int?> = highestScore

    override suspend fun saveNewGameToHistory(historyEntity: HistoryEntity) {
        historyList.add(historyEntity)
        historyLiveData.value = historyList.toList()
        highestScore.value = historyList.maxOfOrNull { it.gameScore }
    }

    override suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity) {
        historyList.remove(historyEntity)
        historyLiveData.value = historyList.toList()
        highestScore.value = historyList.maxOfOrNull { it.gameScore }
    }

    override suspend fun deleteAllGamesHistory() {
        historyList.clear()
        historyLiveData.value = emptyList()
        highestScore.value = null
    }
}
