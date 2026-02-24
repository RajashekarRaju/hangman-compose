package com.developersbreach.hangman.composeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppInitializerUiState(
    val themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
)

class AppInitializerViewModel(
    private val settingsRepository: GameSettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppInitializerUiState())
    val uiState: StateFlow<AppInitializerUiState> = _uiState.asStateFlow()

    init {
        observeThemePalette()
    }

    private fun observeThemePalette() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(themePaletteId = settingsRepository.getThemePaletteId())
            }
            settingsRepository.observeThemePaletteId().collect { paletteId ->
                _uiState.update { current ->
                    current.copy(themePaletteId = paletteId)
                }
            }
        }
    }
}
