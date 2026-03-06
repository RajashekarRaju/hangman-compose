package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.logging.AuditSpec
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.repository.ThemeMode
import com.developersbreach.hangman.ui.theme.ThemePaletteId

sealed interface SettingsEvent {
    data object NavigateUpClicked : SettingsEvent
    data class SettingsSectionSelected(val section: SettingsSection) : SettingsEvent
    data class DifficultySliderPositionChanged(val sliderPosition: Float) : SettingsEvent
    data class DifficultyChanged(val difficulty: GameDifficulty) : SettingsEvent
    data class CategoryChanged(val category: GameCategory) : SettingsEvent
    data class LanguageChanged(val language: AppLanguage) : SettingsEvent
    data class ThemePaletteChanged(val paletteId: ThemePaletteId) : SettingsEvent
    data class ThemeModeChanged(val mode: ThemeMode) : SettingsEvent
    data class BackgroundMusicToggled(val isEnabled: Boolean) : SettingsEvent
    data class SoundEffectsToggled(val isEnabled: Boolean) : SettingsEvent
    data class CursorStyleChanged(val cursorStyle: CursorStyle) : SettingsEvent
    data class GameProgressVisualPreferenceChanged(
        val gameProgressVisualPreference: GameProgressVisualPreference,
    ) : SettingsEvent
}

internal fun SettingsEvent.auditSpec(current: SettingsUiState): AuditSpec? = when (this) {
    is SettingsEvent.DifficultyChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "difficulty" to difficulty.name,
        ),
    )
    is SettingsEvent.CategoryChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "category" to category.name,
        ),
    )
    is SettingsEvent.LanguageChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "language" to language.name,
        ),
    )
    is SettingsEvent.ThemePaletteChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "theme" to paletteId.name,
        ),
    )
    is SettingsEvent.CursorStyleChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "cursor_style" to cursorStyle.name,
        ),
    )
    is SettingsEvent.GameProgressVisualPreferenceChanged -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "settings",
            "visual" to gameProgressVisualPreference.name,
        ),
    )
    is SettingsEvent.DifficultySliderPositionChanged,
    is SettingsEvent.BackgroundMusicToggled,
    SettingsEvent.NavigateUpClicked,
    is SettingsEvent.SettingsSectionSelected,
    is SettingsEvent.ThemeModeChanged,
    is SettingsEvent.SoundEffectsToggled -> null
}