package com.developersbreach.hangman.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.ui.theme.ThemePaletteId
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
        observeThemePalette()
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

            OnBoardingEvent.OpenDifficultyDialog -> {
                _uiState.update { current ->
                    current.copy(
                        isDifficultyDialogOpen = true,
                        pendingDifficulty = current.gameDifficulty,
                        pendingDifficultySliderPosition = current.gameDifficulty.toSliderPosition(),
                    )
                }
            }

            OnBoardingEvent.DismissDifficultyDialog -> {
                _uiState.update { current ->
                    current.copy(
                        isDifficultyDialogOpen = false,
                        pendingDifficulty = current.gameDifficulty,
                        pendingDifficultySliderPosition = current.gameDifficulty.toSliderPosition(),
                    )
                }
            }

            is OnBoardingEvent.DifficultySliderPositionChanged -> {
                val difficulty = event.sliderPosition.toGameDifficulty()
                _uiState.update { current ->
                    current.copy(
                        pendingDifficulty = difficulty,
                        pendingDifficultySliderPosition = event.sliderPosition,
                    )
                }
            }

            is OnBoardingEvent.DifficultyChanged -> {
                updatePlayerChosenDifficulty(event.difficulty)
            }

            OnBoardingEvent.OpenCategoryDialog -> {
                _uiState.update { current -> current.copy(isCategoryDialogOpen = true) }
            }

            OnBoardingEvent.DismissCategoryDialog -> {
                _uiState.update { current -> current.copy(isCategoryDialogOpen = false) }
            }

            is OnBoardingEvent.CategoryChanged -> {
                updatePlayerChosenCategory(event.category)
            }

            OnBoardingEvent.OpenThemePaletteMenu -> {
                _uiState.update { current -> current.copy(isPaletteMenuExpanded = true) }
            }

            OnBoardingEvent.DismissThemePaletteMenu -> {
                _uiState.update { current -> current.copy(isPaletteMenuExpanded = false) }
            }

            is OnBoardingEvent.ThemePaletteChanged -> {
                updateThemePalette(event.paletteId)
            }

            OnBoardingEvent.OpenInstructionsDialog -> {
                _uiState.update { current -> current.copy(isInstructionsDialogOpen = true) }
            }

            OnBoardingEvent.DismissInstructionsDialog -> {
                _uiState.update { current -> current.copy(isInstructionsDialogOpen = false) }
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
            val difficulty = settingsRepository.getGameDifficulty()
            val themePaletteId = settingsRepository.getThemePaletteId()
            _uiState.update { current ->
                current.copy(
                    gameDifficulty = difficulty,
                    gameCategory = settingsRepository.getGameCategory(),
                    themePaletteId = themePaletteId,
                    isBackgroundMusicPlaying = audioController.isPlaying(),
                    pendingDifficulty = difficulty,
                    pendingDifficultySliderPosition = difficulty.toSliderPosition(),
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

    private fun updatePlayerChosenDifficulty(gameDifficulty: GameDifficulty) {
        _uiState.update { current ->
            current.copy(
                gameDifficulty = gameDifficulty,
                pendingDifficulty = gameDifficulty,
                pendingDifficultySliderPosition = gameDifficulty.toSliderPosition(),
            )
        }
        viewModelScope.launch {
            settingsRepository.setGameDifficulty(gameDifficulty)
        }
    }

    private fun updatePlayerChosenCategory(gameCategory: GameCategory) {
        _uiState.update { current ->
            current.copy(gameCategory = gameCategory)
        }
        viewModelScope.launch {
            settingsRepository.setGameCategory(gameCategory)
        }
    }

    private fun updateThemePalette(themePaletteId: ThemePaletteId) {
        _uiState.update { current ->
            current.copy(
                isPaletteMenuExpanded = false,
            )
        }
        viewModelScope.launch {
            settingsRepository.setThemePaletteId(themePaletteId)
        }
    }

    private fun observeThemePalette() {
        viewModelScope.launch {
            settingsRepository.observeThemePaletteId().collect { themePaletteId ->
                _uiState.update { current -> current.copy(themePaletteId = themePaletteId) }
            }
        }
    }

    override fun onCleared() {
        stopBackgroundMusic()
    }
}
