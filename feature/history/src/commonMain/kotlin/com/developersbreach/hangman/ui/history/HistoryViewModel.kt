package com.developersbreach.hangman.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for screen [HistoryScreen].
 * Initialized with koin.
 */
class HistoryViewModel(
    private val repository: HistoryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<HistoryEffect>()
    val effects: SharedFlow<HistoryEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.observeHistory().collect { gameHistoryList ->
                _uiState.update { currentState ->
                    currentState.copy(gameHistoryList = gameHistoryList)
                }
            }
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            HistoryEvent.NavigateUpClicked -> {
                viewModelScope.launch {
                    _effects.emit(HistoryEffect.NavigateUp)
                }
            }

            HistoryEvent.DeleteAllClicked -> {
                deleteAllGameHistoryData()
            }

            is HistoryEvent.DeleteHistoryItemClicked -> {
                deleteSelectedGameHistory(event.history)
            }
        }
    }

    private fun deleteSelectedGameHistory(history: HistoryRecord) {
        viewModelScope.launch {
            repository.deleteHistoryItem(history)
        }
    }

    private fun deleteAllGameHistoryData() {
        viewModelScope.launch {
            repository.deleteAllHistory()
        }
    }
}
