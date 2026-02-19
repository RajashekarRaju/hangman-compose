package com.developersbreach.hangman.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developersbreach.hangman.repository.database.entity.GameSettingsEntity

@Dao
interface GameSettingsDao {
    @Query("SELECT * FROM game_settings_table WHERE settingsId = :settingsId LIMIT 1")
    suspend fun getSettings(settingsId: Int = GameSettingsEntity.SETTINGS_SINGLETON_ID): GameSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSettings(settings: GameSettingsEntity)
}
