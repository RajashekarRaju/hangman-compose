package com.developersbreach.hangman.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
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
    private val backgroundAudioController: BackgroundAudioController,
) : ViewModel() {

    private val visibleSettingsSections = SettingsSection.all.filter { section ->
        section.isVisible(supportsCursorSettings())
    }
    private val _uiState = MutableStateFlow(
        SettingsUiState(
            visibleSettingsSections = visibleSettingsSections,
            selectedSettingsSection = visibleSettingsSections.first(),
        )
    )
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects: SharedFlow<SettingsEffect> = _effects.asSharedFlow()

    init {
        hydrateFromPreferences()
        observeThemePalette()
        observeAppLanguage()
        observeCursorStyle()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.NavigateUpClicked -> emitEffect(SettingsEffect.NavigateUp)
            is SettingsEvent.SettingsSectionSelected -> {
                val targetSection = event.section.takeIf { section ->
                    section in visibleSettingsSections
                } ?: visibleSettingsSections.first()
                _uiState.update { current -> current.copy(selectedSettingsSection = targetSection) }
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
            is SettingsEvent.CategoryChanged -> updatePlayerChosenCategory(event.category)
            is SettingsEvent.LanguageChanged -> updateAppLanguage(event.language)
            is SettingsEvent.ThemePaletteChanged -> updateThemePalette(event.paletteId)
            is SettingsEvent.BackgroundMusicToggled -> updateBackgroundMusic(event.isEnabled)
            is SettingsEvent.SoundEffectsToggled -> updateSoundEffects(event.isEnabled)
            is SettingsEvent.CursorStyleChanged -> updateCursorStyle(event.cursorStyle)
            is SettingsEvent.GameProgressVisualPreferenceChanged -> {
                updateGameProgressVisualPreference(event.gameProgressVisualPreference)
            }
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
            val isBackgroundMusicEnabled = settingsRepository.isBackgroundMusicEnabled()
            val isSoundEffectsEnabled = settingsRepository.isSoundEffectsEnabled()
            val cursorStyle = settingsRepository.getCursorStyle()
            val gameProgressVisualPreference = settingsRepository.getGameProgressVisualPreference()
            _uiState.update { current ->
                current.copy(
                    gameDifficulty = difficulty,
                    gameDifficultyLabelRes = difficulty.labelRes(),
                    gameCategory = category,
                    selectedLanguage = appLanguage,
                    themePaletteId = themePaletteId,
                    isBackgroundMusicEnabled = isBackgroundMusicEnabled,
                    isSoundEffectsEnabled = isSoundEffectsEnabled,
                    selectedCursorStyle = cursorStyle,
                    selectedGameProgressVisualPreference = gameProgressVisualPreference,
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
        viewModelScope.launch {
            settingsRepository.setThemePaletteId(themePaletteId)
        }
    }

    private fun updateAppLanguage(language: AppLanguage) {
        _uiState.update { current -> current.copy(selectedLanguage = language) }
        viewModelScope.launch {
            settingsRepository.setAppLanguage(language)
        }
    }

    private fun updateBackgroundMusic(isEnabled: Boolean) {
        _uiState.update { current -> current.copy(isBackgroundMusicEnabled = isEnabled) }
        when {
            isEnabled -> backgroundAudioController.playLoop()
            else -> backgroundAudioController.stop()
        }
        viewModelScope.launch {
            settingsRepository.setBackgroundMusicEnabled(isEnabled)
        }
    }

    private fun updateSoundEffects(isEnabled: Boolean) {
        _uiState.update { current -> current.copy(isSoundEffectsEnabled = isEnabled) }
        viewModelScope.launch {
            settingsRepository.setSoundEffectsEnabled(isEnabled)
        }
    }

    private fun updateCursorStyle(cursorStyle: CursorStyle) {
        _uiState.update { current -> current.copy(selectedCursorStyle = cursorStyle) }
        viewModelScope.launch {
            settingsRepository.setCursorStyle(cursorStyle)
        }
    }

    private fun updateGameProgressVisualPreference(
        gameProgressVisualPreference: GameProgressVisualPreference,
    ) {
        _uiState.update { current ->
            current.copy(selectedGameProgressVisualPreference = gameProgressVisualPreference)
        }
        viewModelScope.launch {
            settingsRepository.setGameProgressVisualPreference(gameProgressVisualPreference)
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

    private fun observeCursorStyle() {
        viewModelScope.launch {
            settingsRepository.observeCursorStyle().collect { cursorStyle ->
                _uiState.update { current -> current.copy(selectedCursorStyle = cursorStyle) }
            }
        }
    }
}
