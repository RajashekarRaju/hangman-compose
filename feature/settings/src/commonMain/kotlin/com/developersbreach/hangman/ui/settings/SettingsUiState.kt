package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.feature.mainmenu.generated.resources.Res
import com.developersbreach.hangman.feature.mainmenu.generated.resources.mainmenu_button_category
import com.developersbreach.hangman.feature.mainmenu.generated.resources.mainmenu_button_difficulty
import com.developersbreach.hangman.feature.mainmenu.generated.resources.mainmenu_button_language
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_button_audio
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_button_cursor
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_button_game_visual
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_button_theme_palette
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlin.math.abs
import org.jetbrains.compose.resources.StringResource

data class SettingsUiState(
    val availableCategories: List<GameCategory> = GameCategory.entries,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameDifficultyLabelRes: StringResource = GameDifficulty.EASY.labelRes(),
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val selectedLanguage: AppLanguage = AppLanguage.default,
    val availableLanguages: List<AppLanguage> = AppLanguage.entries,
    val themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
    val isBackgroundMusicEnabled: Boolean = true,
    val isSoundEffectsEnabled: Boolean = true,
    val selectedCursorStyle: CursorStyle = CursorStyle.default,
    val availableCursorStyles: List<CursorStyle> = CursorStyle.entries,
    val selectedGameProgressVisualPreference: GameProgressVisualPreference =
        GameProgressVisualPreference.default,
    val pendingDifficulty: GameDifficulty = GameDifficulty.EASY,
    val pendingDifficultySliderPosition: Float = 1f,
    val visibleSettingsSections: List<SettingsSection> = SettingsSection.all,
    val selectedSettingsSection: SettingsSection = SettingsSection.Difficulty,
)

sealed class SettingsSection(
    val labelRes: StringResource,
    private val requiresCursorSupport: Boolean = false,
) {
    data object Difficulty : SettingsSection(Res.string.mainmenu_button_difficulty)
    data object Category : SettingsSection(Res.string.mainmenu_button_category)
    data object Language : SettingsSection(Res.string.mainmenu_button_language)
    data object Theme : SettingsSection(Res.string.settings_button_theme_palette)
    data object GameVisual : SettingsSection(Res.string.settings_button_game_visual)
    data object Audio : SettingsSection(Res.string.settings_button_audio)
    data object Cursor : SettingsSection(
        labelRes = Res.string.settings_button_cursor,
        requiresCursorSupport = true,
    )

    fun isVisible(cursorSettingsSupported: Boolean): Boolean {
        return !requiresCursorSupport || cursorSettingsSupported
    }

    companion object {
        val all: List<SettingsSection> = listOf(
            Difficulty,
            Category,
            Language,
            Theme,
            GameVisual,
            Audio,
            Cursor,
        )
    }
}

internal fun GameDifficulty.toSliderPosition(): Float {
    return DIFFICULTY_TO_SLIDER_VALUE[this]
        ?: DIFFICULTY_TO_SLIDER_VALUE.getValue(GameDifficulty.EASY)
}

internal fun Float.toGameDifficulty(): GameDifficulty {
    return DIFFICULTY_TO_SLIDER_VALUE
        .minByOrNull { (_, sliderValue) -> abs(sliderValue - this) }
        ?.key
        ?: GameDifficulty.EASY
}

private val DIFFICULTY_TO_SLIDER_VALUE = mapOf(
    GameDifficulty.EASY to 1f,
    GameDifficulty.MEDIUM to 2f,
    GameDifficulty.HARD to 3f,
    GameDifficulty.VERY_HARD to 4f,
)

internal data class GameProgressVisualOption(
    val gameProgressVisualPreference: GameProgressVisualPreference,
    val labelRes: StringResource,
)