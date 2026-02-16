package com.developersbreach.hangman.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_settings_table")
data class GameSettingsEntity(
    @PrimaryKey val settingsId: Int = SETTINGS_SINGLETON_ID,
    val gameDifficulty: String,
    val gameCategoryOrdinal: Int,
) {
    companion object {
        const val SETTINGS_SINGLETON_ID = 0
    }
}
