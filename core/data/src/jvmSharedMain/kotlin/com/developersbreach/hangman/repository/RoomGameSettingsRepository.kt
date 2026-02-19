package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.database.dao.GameSettingsDao
import com.developersbreach.hangman.repository.database.entity.GameSettingsEntity

class RoomGameSettingsRepository(
    private val gameSettingsDao: GameSettingsDao,
) : GameSettingsRepository {

    override suspend fun getGameDifficulty(): GameDifficulty {
        return getOrDefaultSettings().gameDifficulty
    }

    override suspend fun getGameCategory(): GameCategory {
        return getOrDefaultSettings().gameCategory
    }

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = gameDifficulty.name,
                gameCategoryOrdinal = current.gameCategory.ordinal,
            ),
        )
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        val current = getOrDefaultSettings()
        gameSettingsDao.upsertSettings(
            GameSettingsEntity(
                gameDifficulty = current.gameDifficulty.name,
                gameCategoryOrdinal = gameCategory.ordinal,
            ),
        )
    }

    private suspend fun getOrDefaultSettings(): PersistedGameSettings {
        val settings = gameSettingsDao.getSettings()
            ?: return PersistedGameSettings(
                gameDifficulty = GameDifficulty.EASY,
                gameCategory = GameCategory.COUNTRIES,
            )

        val difficulty = runCatching { GameDifficulty.valueOf(settings.gameDifficulty) }
            .getOrDefault(GameDifficulty.EASY)
        val category = GameCategory.entries
            .getOrNull(settings.gameCategoryOrdinal)
            ?: GameCategory.COUNTRIES

        return PersistedGameSettings(
            gameDifficulty = difficulty,
            gameCategory = category,
        )
    }
}
