package com.developersbreach.hangman.composeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.ThemeMode
import com.developersbreach.hangman.ui.common.notification.AchievementBannerUiState
import com.developersbreach.hangman.ui.common.notification.AchievementNotificationCoordinator
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AppInitializerState(
    val appLanguage: AppLanguage = AppLanguage.default,
    val themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
    val themeMode: ThemeMode = ThemeMode.default,
    val cursorStyle: CursorStyle = CursorStyle.default,
    val achievementBannerState: AchievementBannerUiState = AchievementBannerUiState(),
)

class AppInitializerViewModel(
    private val settingsRepository: GameSettingsRepository,
    private val achievementNotificationCoordinator: AchievementNotificationCoordinator,
) : ViewModel() {

    private val _appState = MutableStateFlow(AppInitializerState())
    val appState: StateFlow<AppInitializerState> = _appState.asStateFlow()

    init {
        observeAppLanguage()
        observeThemePalette()
        observeThemeMode()
        observeCursorStyle()
        observeAchievementBanner()
    }

    private fun observeAppLanguage() {
        viewModelScope.launch {
            val initialLanguage = settingsRepository.getAppLanguage()
            applyAppLanguage(initialLanguage)
            _appState.update { current ->
                current.copy(appLanguage = initialLanguage)
            }
            settingsRepository.observeAppLanguage().collect { language ->
                applyAppLanguage(language)
                _appState.update { current ->
                    current.copy(appLanguage = language)
                }
            }
        }
    }

    private fun observeThemePalette() {
        viewModelScope.launch {
            _appState.update { current ->
                current.copy(themePaletteId = settingsRepository.getThemePaletteId())
            }
            settingsRepository.observeThemePaletteId().collect { paletteId ->
                _appState.update { current ->
                    current.copy(themePaletteId = paletteId)
                }
            }
        }
    }

    private fun observeThemeMode() {
        viewModelScope.launch {
            _appState.update { current ->
                current.copy(themeMode = settingsRepository.getThemeMode())
            }
            settingsRepository.observeThemeMode().collect { themeMode ->
                _appState.update { current ->
                    current.copy(themeMode = themeMode)
                }
            }
        }
    }

    private fun observeAchievementBanner() {
        viewModelScope.launch {
            achievementNotificationCoordinator.bannerState.collect { bannerState ->
                _appState.update { current ->
                    current.copy(achievementBannerState = bannerState)
                }
            }
        }
    }

    private fun observeCursorStyle() {
        viewModelScope.launch {
            _appState.update { current ->
                current.copy(cursorStyle = settingsRepository.getCursorStyle())
            }
            settingsRepository.observeCursorStyle().collect { cursorStyle ->
                _appState.update { current ->
                    current.copy(cursorStyle = cursorStyle)
                }
            }
        }
    }
}
