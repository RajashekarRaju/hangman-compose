package com.hangman.hangman.ui.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.launch


/**
 * ViewModel for screen [HistoryScreen].
 * Initialized with koin.
 */
class HistoryViewModel(
    private val repository: GameRepository
) : ViewModel() {

    // Contains latest game history list.
    var gameHistoryList by mutableStateOf(emptyList<HistoryEntity>())

    init {
        viewModelScope.launch {
            // Fetch the game history from database.
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }

    /**
     * Deletes the single game history item from database.
     * Once deleted, immediately update the gameHistoryList state with new data.
     */
    fun deleteSelectedGameHistory(
        history: HistoryEntity
    ) {
        viewModelScope.launch {
            repository.deleteSelectedSingleGameHistory(history)
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }

    /**
     * Deletes complete game history from database.
     * Once deleted, immediately update the gameHistoryList state.
     */
    fun deleteAllGameHistoryData() {
        viewModelScope.launch {
            repository.deleteCompleteGamesHistory()
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }
}