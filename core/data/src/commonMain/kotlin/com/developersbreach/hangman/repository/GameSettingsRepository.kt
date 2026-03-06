package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.flow.StateFlow

interface GameSettingsRepository {

    suspend fun getGameDifficulty(): GameDifficulty

    suspend fun getGameCategory(): GameCategory

    suspend fun getThemePaletteId(): ThemePaletteId

    suspend fun getThemeMode(): ThemeMode

    suspend fun getAppLanguage(): AppLanguage

    suspend fun isBackgroundMusicEnabled(): Boolean

    suspend fun isSoundEffectsEnabled(): Boolean

    suspend fun getCursorStyle(): CursorStyle

    suspend fun getGameProgressVisualPreference(): GameProgressVisualPreference

    fun observeThemePaletteId(): StateFlow<ThemePaletteId>

    fun observeThemeMode(): StateFlow<ThemeMode>

    fun observeAppLanguage(): StateFlow<AppLanguage>

    fun observeCursorStyle(): StateFlow<CursorStyle>

    suspend fun setGameDifficulty(gameDifficulty: GameDifficulty)

    suspend fun setGameCategory(gameCategory: GameCategory)

    suspend fun setThemePaletteId(themePaletteId: ThemePaletteId)

    suspend fun setThemeMode(themeMode: ThemeMode)

    suspend fun setAppLanguage(appLanguage: AppLanguage)

    suspend fun setBackgroundMusicEnabled(isEnabled: Boolean)

    suspend fun setSoundEffectsEnabled(isEnabled: Boolean)

    suspend fun setCursorStyle(cursorStyle: CursorStyle)

    suspend fun setGameProgressVisualPreference(
        gameProgressVisualPreference: GameProgressVisualPreference,
    )
}
