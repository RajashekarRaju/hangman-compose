package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.database.dao.GameSettingsDao
import com.developersbreach.hangman.repository.database.entity.GameSettingsEntity
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.toThemePaletteId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RoomGameSettingsRepository(
    private val gameSettingsDao: GameSettingsDao,
) : GameSettingsRepository {

    private val themePaletteIdState = MutableStateFlow(ThemePaletteId.INSANE_RED)

    override suspend fun getGameDifficulty(): GameDifficulty {
        return getOrDefaultSettings().gameDifficulty
    }

    override suspend fun getGameCategory(): GameCategory {
        return getOrDefaultSettings().gameCategory
    }

    override suspend fun getThemePaletteId(): ThemePaletteId {
        return getOrDefaultSettings().themePaletteId.also { themePaletteIdState.value = it }
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> {
        return themePaletteIdState.asStateFlow()
    }

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
                themePaletteId = current.themePaletteId.name,
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
            ),
        )
        themePaletteIdState.value = themePaletteId
    }

    private suspend fun getOrDefaultSettings(): PersistedGameSettings {
        val settings = gameSettingsDao.getSettings()
            ?: return PersistedGameSettings(
                gameDifficulty = GameDifficulty.EASY,
                gameCategory = GameCategory.COUNTRIES,
                themePaletteId = ThemePaletteId.INSANE_RED,
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
        )
    }
}
