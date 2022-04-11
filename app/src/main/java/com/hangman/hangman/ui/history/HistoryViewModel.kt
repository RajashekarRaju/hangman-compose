package com.hangman.hangman.ui.history

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(
    application: Application,
    private val repository: GameRepository
) : AndroidViewModel(application) {

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

    companion object {

        fun provideFactory(
            application: Application,
            repository: GameRepository,
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
                        return HistoryViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Cannot create Instance for HistoryViewModel class")
                }
            }
        }
    }
}