package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlin.math.abs
import org.jetbrains.compose.resources.StringResource

data class SettingsUiState(
    val availableCategories: List<GameCategory> = categoryOptions,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameDifficultyLabelRes: StringResource = GameDifficulty.EASY.labelRes(),
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val selectedLanguage: AppLanguage = AppLanguage.default,
    val availableLanguages: List<AppLanguage> = AppLanguage.entries,
    val themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
    val isPaletteMenuExpanded: Boolean = false,
    val isDifficultyDialogOpen: Boolean = false,
    val isCategoryDialogOpen: Boolean = false,
    val isLanguageDialogOpen: Boolean = false,
    val pendingDifficulty: GameDifficulty = GameDifficulty.EASY,
    val pendingDifficultySliderPosition: Float = 1f,
)

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
