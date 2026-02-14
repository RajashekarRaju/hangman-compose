package com.developersbreach.hangman.repository

import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun observeHistory(): Flow<List<HistoryRecord>>

    suspend fun deleteHistoryItem(history: HistoryRecord)

    suspend fun deleteAllHistory()
}
