package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.ui.theme.ThemePaletteId

sealed interface SettingsEvent {
    data object NavigateUpClicked : SettingsEvent
    data class SettingsSectionSelected(val section: SettingsSection) : SettingsEvent
    data class DifficultySliderPositionChanged(val sliderPosition: Float) : SettingsEvent
    data class DifficultyChanged(val difficulty: GameDifficulty) : SettingsEvent
    data class CategoryChanged(val category: GameCategory) : SettingsEvent
    data class LanguageChanged(val language: AppLanguage) : SettingsEvent
    data class ThemePaletteChanged(val paletteId: ThemePaletteId) : SettingsEvent
    data class BackgroundMusicToggled(val isEnabled: Boolean) : SettingsEvent
    data class SoundEffectsToggled(val isEnabled: Boolean) : SettingsEvent
    data class CursorStyleChanged(val cursorStyle: CursorStyle) : SettingsEvent
}
