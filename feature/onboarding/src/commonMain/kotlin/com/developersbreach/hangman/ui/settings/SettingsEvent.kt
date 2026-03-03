package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.ui.theme.ThemePaletteId

sealed interface SettingsEvent {
    data object NavigateUpClicked : SettingsEvent
    data object OpenDifficultyDialog : SettingsEvent
    data object DismissDifficultyDialog : SettingsEvent
    data class DifficultySliderPositionChanged(val sliderPosition: Float) : SettingsEvent
    data class DifficultyChanged(val difficulty: GameDifficulty) : SettingsEvent
    data object OpenCategoryDialog : SettingsEvent
    data object DismissCategoryDialog : SettingsEvent
    data class CategoryChanged(val category: GameCategory) : SettingsEvent
    data object OpenLanguageDialog : SettingsEvent
    data object DismissLanguageDialog : SettingsEvent
    data class LanguageChanged(val language: AppLanguage) : SettingsEvent
    data object OpenThemePaletteMenu : SettingsEvent
    data object DismissThemePaletteMenu : SettingsEvent
    data class ThemePaletteChanged(val paletteId: ThemePaletteId) : SettingsEvent
}
