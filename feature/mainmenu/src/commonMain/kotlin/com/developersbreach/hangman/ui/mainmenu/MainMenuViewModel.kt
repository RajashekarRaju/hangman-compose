package com.developersbreach.hangman.ui.mainmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.logging.Log
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainMenuViewModel(
    private val historyRepository: HistoryRepository,
    private val achievementsRepository: AchievementsRepository,
    private val settingsRepository: GameSettingsRepository,
    private val audioController: BackgroundAudioController
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainMenuUiState())
    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<MainMenuEffect>()
    val effects: SharedFlow<MainMenuEffect> = _effects.asSharedFlow()

    init {
        hydrateFromPreferences()
        observeHighestScore()
        observeUnreadAchievementsCount()
    }

    fun onEvent(event: MainMenuEvent) {
        event.auditSpec()?.let(Log::audit)
        when (event) {
            MainMenuEvent.NavigateToGameClicked -> {
                stopBackgroundMusic()
                emitEffect(MainMenuEffect.NavigateToGame)
            }

            MainMenuEvent.NavigateToSettingsClicked -> {
                emitEffect(MainMenuEffect.NavigateToSettings)
            }

            MainMenuEvent.NavigateToHistoryClicked -> {
                emitEffect(MainMenuEffect.NavigateToHistory)
            }

            MainMenuEvent.NavigateToAchievementsClicked -> {
                emitEffect(MainMenuEffect.NavigateToAchievements)
            }

            MainMenuEvent.NavigateToGameGuideClicked -> {
                emitEffect(MainMenuEffect.NavigateToGameGuide)
            }

            MainMenuEvent.ExitClicked -> {
                stopBackgroundMusic()
                emitEffect(MainMenuEffect.FinishActivity)
            }

            MainMenuEvent.ReportIssueClicked -> {
                emitEffect(MainMenuEffect.OpenIssueTracker(ISSUES_URL))
            }
        }
    }

    private fun emitEffect(effect: MainMenuEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private fun observeHighestScore() {
        viewModelScope.launch {
            historyRepository.observeHistory().collect { history ->
                val highScore = history.maxOfOrNull { it.gameScore } ?: 0
                _uiState.update { current ->
                    current.copy(highScore = highScore)
                }
            }
        }
    }

    private fun observeUnreadAchievementsCount() {
        viewModelScope.launch {
            achievementsRepository.observeAchievementProgress().collect { progress ->
                val hasUnreadAchievements = progress.any { value ->
                    value.isUnlocked && value.isUnread
                }
                _uiState.update { current ->
                    current.copy(hasUnreadAchievements = hasUnreadAchievements)
                }
            }
        }
    }

    private fun hydrateFromPreferences() {
        viewModelScope.launch {
            val isBackgroundMusicEnabled = settingsRepository.isBackgroundMusicEnabled()
            when {
                isBackgroundMusicEnabled -> audioController.playLoop()
                else -> audioController.stop()
            }
        }
    }

    private fun stopBackgroundMusic() {
        audioController.stop()
    }

    override fun onCleared() {
        stopBackgroundMusic()
    }

    private companion object {
        private const val ISSUES_URL = "https://github.com/RajashekarRaju/hangman-compose/issues"
    }
}
