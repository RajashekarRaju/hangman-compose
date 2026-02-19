package com.developersbreach.hangman.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
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
        playBackgroundMusic()
    }

    fun onEvent(event: OnBoardingEvent) {
        when (event) {
            OnBoardingEvent.NavigateToGameClicked -> {
                stopBackgroundMusic()
                emitEffect(OnBoardingEffect.NavigateToGame)
            }

            OnBoardingEvent.NavigateToHistoryClicked -> {
                emitEffect(OnBoardingEffect.NavigateToHistory)
            }

            OnBoardingEvent.ExitClicked -> {
                stopBackgroundMusic()
                emitEffect(OnBoardingEffect.FinishActivity)
            }

            is OnBoardingEvent.DifficultyChanged -> {
                updatePlayerChosenDifficulty(event.sliderPosition)
            }

            is OnBoardingEvent.CategoryChanged -> {
                updatePlayerChosenCategory(event.categoryId)
            }

            OnBoardingEvent.ToggleBackgroundMusic -> {
                if (_uiState.value.isBackgroundMusicPlaying) {
                    stopBackgroundMusic()
                } else {
                    playBackgroundMusic()
                }
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

    private fun hydrateFromPreferences() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    gameDifficulty = settingsRepository.getGameDifficulty(),
                    gameCategory = settingsRepository.getGameCategory(),
                    isBackgroundMusicPlaying = audioController.isPlaying()
                )
            }
        }
    }

    private fun playBackgroundMusic() {
        audioController.playLoop()
        _uiState.update { current ->
            current.copy(isBackgroundMusicPlaying = true)
        }
    }

    private fun stopBackgroundMusic() {
        audioController.stop()
        _uiState.update { current ->
            current.copy(isBackgroundMusicPlaying = false)
        }
    }

    private fun updatePlayerChosenDifficulty(sliderPosition: Float) {
        val gameDifficulty = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }

        _uiState.update { current ->
            current.copy(gameDifficulty = gameDifficulty)
        }
        viewModelScope.launch {
            settingsRepository.setGameDifficulty(gameDifficulty)
        }
    }

    private fun updatePlayerChosenCategory(category: Int) {
        val gameCategory = when (category) {
            0 -> GameCategory.COUNTRIES
            1 -> GameCategory.LANGUAGES
            2 -> GameCategory.COMPANIES
            else -> GameCategory.COUNTRIES
        }

        _uiState.update { current ->
            current.copy(gameCategory = gameCategory)
        }
        viewModelScope.launch {
            settingsRepository.setGameCategory(gameCategory)
        }
    }

    override fun onCleared() {
        stopBackgroundMusic()
    }
}
