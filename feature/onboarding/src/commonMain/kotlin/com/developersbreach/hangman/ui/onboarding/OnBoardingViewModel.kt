package com.developersbreach.hangman.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.audio.BackgroundAudioController
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

class OnBoardingViewModel(
    private val historyRepository: HistoryRepository,
    private val achievementsRepository: AchievementsRepository,
    private val settingsRepository: GameSettingsRepository,
    private val audioController: BackgroundAudioController
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnBoardingUiState())
    val uiState: StateFlow<OnBoardingUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<OnBoardingEffect>()
    val effects: SharedFlow<OnBoardingEffect> = _effects.asSharedFlow()

    init {
        hydrateFromPreferences()
        observeHighestScore()
        observeUnreadAchievementsCount()
    }

    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            OnBoardingEvent.NavigateToGameClicked -> {
                stopBackgroundMusic()
                emitEffect(OnBoardingEffect.NavigateToGame)
            }

            OnBoardingEvent.NavigateToSettingsClicked -> {
                emitEffect(OnBoardingEffect.NavigateToSettings)
            }

            OnBoardingEvent.NavigateToHistoryClicked -> {
                emitEffect(OnBoardingEffect.NavigateToHistory)
            }

            OnBoardingEvent.NavigateToAchievementsClicked -> {
                emitEffect(OnBoardingEffect.NavigateToAchievements)
            }

            OnBoardingEvent.NavigateToGameGuideClicked -> {
                emitEffect(OnBoardingEffect.NavigateToGameGuide)
            }

            OnBoardingEvent.ExitClicked -> {
                stopBackgroundMusic()
                emitEffect(OnBoardingEffect.FinishActivity)
            }

            OnBoardingEvent.ReportIssueClicked -> {
                emitEffect(OnBoardingEffect.OpenIssueTracker(ISSUES_URL))
            }
        }
    }

    private fun emitEffect(effect: OnBoardingEffect) {
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
