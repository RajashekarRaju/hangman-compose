package com.developersbreach.hangman.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: GameSettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects: SharedFlow<SettingsEffect> = _effects.asSharedFlow()

    init {
        hydrateFromPreferences()
        observeThemePalette()
        observeAppLanguage()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.NavigateUpClicked -> emitEffect(SettingsEffect.NavigateUp)
            SettingsEvent.OpenDifficultyDialog -> {
                _uiState.update { current ->
                    current.copy(
                        isDifficultyDialogOpen = true,
                        pendingDifficulty = current.gameDifficulty,
                        pendingDifficultySliderPosition = current.gameDifficulty.toSliderPosition(),
                    )
                }
            }

            SettingsEvent.DismissDifficultyDialog -> {
                _uiState.update { current ->
                    current.copy(
                        isDifficultyDialogOpen = false,
                        pendingDifficulty = current.gameDifficulty,
                        pendingDifficultySliderPosition = current.gameDifficulty.toSliderPosition(),
                    )
                }
            }

            is SettingsEvent.DifficultySliderPositionChanged -> {
                val difficulty = event.sliderPosition.toGameDifficulty()
                _uiState.update { current ->
                    current.copy(
                        pendingDifficulty = difficulty,
                        pendingDifficultySliderPosition = event.sliderPosition,
                    )
                }
            }

            is SettingsEvent.DifficultyChanged -> updatePlayerChosenDifficulty(event.difficulty)
            SettingsEvent.OpenCategoryDialog -> {
                _uiState.update { current -> current.copy(isCategoryDialogOpen = true) }
            }

            SettingsEvent.DismissCategoryDialog -> {
                _uiState.update { current -> current.copy(isCategoryDialogOpen = false) }
            }

            is SettingsEvent.CategoryChanged -> updatePlayerChosenCategory(event.category)
            SettingsEvent.OpenLanguageDialog -> {
                _uiState.update { current -> current.copy(isLanguageDialogOpen = true) }
            }

            SettingsEvent.DismissLanguageDialog -> {
                _uiState.update { current -> current.copy(isLanguageDialogOpen = false) }
            }

            is SettingsEvent.LanguageChanged -> updateAppLanguage(event.language)
            SettingsEvent.OpenThemePaletteMenu -> {
                _uiState.update { current -> current.copy(isPaletteMenuExpanded = true) }
            }

            SettingsEvent.DismissThemePaletteMenu -> {
                _uiState.update { current -> current.copy(isPaletteMenuExpanded = false) }
            }

            is SettingsEvent.ThemePaletteChanged -> updateThemePalette(event.paletteId)
        }
    }

    private fun emitEffect(effect: SettingsEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private fun hydrateFromPreferences() {
        viewModelScope.launch {
            val difficulty = settingsRepository.getGameDifficulty()
            val category = settingsRepository.getGameCategory()
            val themePaletteId = settingsRepository.getThemePaletteId()
            val appLanguage = settingsRepository.getAppLanguage()
            _uiState.update { current ->
                current.copy(
                    gameDifficulty = difficulty,
                    gameDifficultyLabelRes = difficulty.labelRes(),
                    gameCategory = category,
                    selectedLanguage = appLanguage,
                    themePaletteId = themePaletteId,
                    pendingDifficulty = difficulty,
                    pendingDifficultySliderPosition = difficulty.toSliderPosition(),
                )
            }
        }
    }

    private fun updatePlayerChosenDifficulty(gameDifficulty: GameDifficulty) {
        _uiState.update { current ->
            current.copy(
                gameDifficulty = gameDifficulty,
                gameDifficultyLabelRes = gameDifficulty.labelRes(),
                pendingDifficulty = gameDifficulty,
                pendingDifficultySliderPosition = gameDifficulty.toSliderPosition(),
            )
        }
        viewModelScope.launch {
            settingsRepository.setGameDifficulty(gameDifficulty)
        }
    }

    private fun updatePlayerChosenCategory(gameCategory: GameCategory) {
        _uiState.update { current -> current.copy(gameCategory = gameCategory) }
        viewModelScope.launch {
            settingsRepository.setGameCategory(gameCategory)
        }
    }

    private fun updateThemePalette(themePaletteId: ThemePaletteId) {
        _uiState.update { current -> current.copy(isPaletteMenuExpanded = false) }
        viewModelScope.launch {
            settingsRepository.setThemePaletteId(themePaletteId)
        }
    }

    private fun updateAppLanguage(language: AppLanguage) {
        _uiState.update { current ->
            current.copy(
                selectedLanguage = language,
                isLanguageDialogOpen = false
            )
        }
        viewModelScope.launch {
            settingsRepository.setAppLanguage(language)
        }
    }

    private fun observeThemePalette() {
        viewModelScope.launch {
            settingsRepository.observeThemePaletteId().collect { themePaletteId ->
                _uiState.update { current -> current.copy(themePaletteId = themePaletteId) }
            }
        }
    }

    private fun observeAppLanguage() {
        viewModelScope.launch {
            settingsRepository.observeAppLanguage().collect { language ->
                _uiState.update { current -> current.copy(selectedLanguage = language) }
            }
        }
    }
}
