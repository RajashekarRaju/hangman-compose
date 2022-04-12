package com.hangman.hangman.ui.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.launch


class HistoryViewModel(
    private val repository: GameRepository
) : ViewModel() {

    var gameHistoryList by mutableStateOf(emptyList<HistoryEntity>())

    init {
        viewModelScope.launch {
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }

    fun deleteSelectedGameHistory(
        history: HistoryEntity
    ) {
        viewModelScope.launch {
            repository.deleteSelectedSingleGameHistory(history)
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }

    fun deleteAllGameHistoryData() {
        viewModelScope.launch {
            repository.deleteCompleteGamesHistory()
            gameHistoryList = repository.getCompleteGameHistory()
        }
    }
}