package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.database.dao.GameSettingsDao
import com.developersbreach.hangman.repository.database.entity.GameSettingsEntity
import com.developersbreach.hangman.repository.storage.toAppLanguage
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.toThemePaletteId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoomGameSettingsRepository(
    private val gameSettingsDao: GameSettingsDao,
) : GameSettingsRepository {

    private val themePaletteIdState = MutableStateFlow(ThemePaletteId.INSANE_RED)
    private val appLanguageState = MutableStateFlow(AppLanguage.default)
    private val cursorStyleState = MutableStateFlow(CursorStyle.default)

    override suspend fun getGameDifficulty(): GameDifficulty {
        return getOrDefaultSettings().gameDifficulty
    }

    override suspend fun getGameCategory(): GameCategory {
        return getOrDefaultSettings().gameCategory
    }

    override suspend fun getThemePaletteId(): ThemePaletteId {
        return getOrDefaultSettings().themePaletteId.also { themePaletteIdState.value = it }
    }

    override suspend fun getAppLanguage(): AppLanguage {
        return getOrDefaultSettings().appLanguage.also { appLanguageState.value = it }
    }

    override suspend fun isBackgroundMusicEnabled(): Boolean {
        return getOrDefaultSettings().isBackgroundMusicEnabled
    }

    override suspend fun isSoundEffectsEnabled(): Boolean {
        return getOrDefaultSettings().isSoundEffectsEnabled
    }

    override suspend fun getCursorStyle(): CursorStyle {
        return getOrDefaultSettings().cursorStyle.also { cursorStyleState.value = it }
    }

    override suspend fun getGameProgressVisualPreference(): GameProgressVisualPreference {
        return getOrDefaultSettings().gameProgressVisualPreference
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> {
        return themePaletteIdState.asStateFlow()
    }

    override fun observeAppLanguage(): StateFlow<AppLanguage> {
        return appLanguageState.asStateFlow()
    }

    override fun observeCursorStyle(): StateFlow<CursorStyle> {
        return cursorStyleState.asStateFlow()
    }

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
    }

    override suspend fun setThemePaletteId(themePaletteId: ThemePaletteId) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
        themePaletteIdState.value = themePaletteId
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
        appLanguageState.value = appLanguage
    }

    override suspend fun setBackgroundMusicEnabled(isEnabled: Boolean) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = isEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
    }

    override suspend fun setSoundEffectsEnabled(isEnabled: Boolean) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = isEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
    }

    override suspend fun setCursorStyle(cursorStyle: CursorStyle) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = cursorStyle.name,
                gameProgressVisualPreference = current.gameProgressVisualPreference.name,
            ),
        )
        cursorStyleState.value = cursorStyle
    }

    override suspend fun setGameProgressVisualPreference(
        gameProgressVisualPreference: GameProgressVisualPreference,
    ) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
                appLanguageCode = current.appLanguage.languageTag,
                isBackgroundMusicEnabled = current.isBackgroundMusicEnabled,
                isSoundEffectsEnabled = current.isSoundEffectsEnabled,
                cursorStyle = current.cursorStyle.name,
                gameProgressVisualPreference = gameProgressVisualPreference.name,
            ),
        )
    }

    private suspend fun getOrDefaultSettings(): PersistedGameSettings {
        val settings = gameSettingsDao.getSettings()
            ?: return PersistedGameSettings(
                gameDifficulty = GameDifficulty.EASY,
                gameCategory = GameCategory.COUNTRIES,
                themePaletteId = ThemePaletteId.INSANE_RED,
                appLanguage = AppLanguage.default,
                isBackgroundMusicEnabled = true,
                isSoundEffectsEnabled = true,
                cursorStyle = CursorStyle.default,
                gameProgressVisualPreference = GameProgressVisualPreference.default,
            )

        val difficulty = runCatching { GameDifficulty.valueOf(settings.gameDifficulty) }
            .getOrDefault(GameDifficulty.EASY)
        val category = GameCategory.entries
            .getOrNull(settings.gameCategoryOrdinal)
            ?: GameCategory.COUNTRIES

        return PersistedGameSettings(
            gameDifficulty = difficulty,
            gameCategory = category,
            themePaletteId = settings.themePaletteId
                .ifBlank { ThemePaletteId.INSANE_RED.name }
                .toThemePaletteId(),
            appLanguage = settings.appLanguageCode.toAppLanguage(),
            isBackgroundMusicEnabled = settings.isBackgroundMusicEnabled,
            isSoundEffectsEnabled = settings.isSoundEffectsEnabled,
            cursorStyle = CursorStyle.fromStorage(settings.cursorStyle),
            gameProgressVisualPreference = GameProgressVisualPreference.fromStorage(
                settings.gameProgressVisualPreference,
            ),
        )
    }
}
