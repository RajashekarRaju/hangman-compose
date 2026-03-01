package com.developersbreach.hangman.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.developersbreach.hangman.repository.database.dao.AchievementsDao
import com.developersbreach.hangman.repository.database.dao.GameSettingsDao
import com.developersbreach.hangman.repository.database.dao.HistoryDao
import com.developersbreach.hangman.repository.database.entity.AchievementProgressEntity
import com.developersbreach.hangman.repository.database.entity.AchievementStatsEntity
import com.developersbreach.hangman.repository.database.entity.GameSettingsEntity
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.Dispatchers

internal const val DATABASE_NAME = "hangman.db"

@Database(
    entities = [
        HistoryEntity::class,
        GameSettingsEntity::class,
        AchievementProgressEntity::class,
        AchievementStatsEntity::class,
    ],
    version = 7,
    exportSchema = false,
)
abstract class GameDatabase : RoomDatabase() {
    abstract val historyDao: HistoryDao
    abstract val gameSettingsDao: GameSettingsDao
    abstract val achievementsDao: AchievementsDao
}

internal fun buildDatabase(
    builder: RoomDatabase.Builder<GameDatabase>,
): GameDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}
