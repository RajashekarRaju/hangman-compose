package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.flow.StateFlow

interface GameSettingsRepository {

    suspend fun getGameDifficulty(): GameDifficulty

    suspend fun getGameCategory(): GameCategory

    suspend fun getThemePaletteId(): ThemePaletteId

    suspend fun getAppLanguage(): AppLanguage

    fun observeThemePaletteId(): StateFlow<ThemePaletteId>

    fun observeAppLanguage(): StateFlow<AppLanguage>

    suspend fun setGameDifficulty(gameDifficulty: GameDifficulty)

    suspend fun setGameCategory(gameCategory: GameCategory)

    suspend fun setThemePaletteId(themePaletteId: ThemePaletteId)

    suspend fun setAppLanguage(appLanguage: AppLanguage)
}
