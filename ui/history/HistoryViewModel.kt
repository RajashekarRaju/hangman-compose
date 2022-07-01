package com.hangman.hangman.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


/**
 * ViewModel for screen [HistoryScreen].
 * Initialized with koin.
 */
class HistoryViewModel(
    private val repository: GameRepository
) : ViewModel() {

    // Get all the game history from the database.
    val gameHistoryList: Flow<List<HistoryEntity>> = repository.getCompleteGameHistory()

    /**
     * Deletes the single game history item from database.
     */
    fun deleteSelectedGameHistory(
        history: HistoryEntity
    ) {
        viewModelScope.launch {
            repository.deleteSelectedSingleGameHistory(history)
        }
    }

    /**
     * Deletes complete game history from database.
     */
    fun deleteAllGameHistoryData() {
        viewModelScope.launch {
            repository.deleteCompleteGamesHistory()
        }
    }
}