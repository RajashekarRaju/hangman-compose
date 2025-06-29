package com.developersbreach.hangman.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: GameRepository
) : BaseViewModel() {

    val gameHistoryList: LiveData<List<HistoryEntity>> = repository.getCompleteGameHistory()

    fun deleteSelectedGameHistory(history: HistoryEntity) {
        viewModelScope.launch { repository.deleteSelectedSingleGameHistory(history) }
    }

    fun deleteAllGameHistoryData() {
        viewModelScope.launch { repository.deleteCompleteGamesHistory() }
    }
}
