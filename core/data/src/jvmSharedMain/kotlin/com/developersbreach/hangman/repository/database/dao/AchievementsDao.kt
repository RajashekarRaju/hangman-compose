package com.developersbreach.hangman.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developersbreach.hangman.repository.database.entity.AchievementProgressEntity
import com.developersbreach.hangman.repository.database.entity.AchievementStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementsDao {

    @Query("SELECT * FROM achievement_progress_table")
    fun observeAchievementProgress(): Flow<List<AchievementProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAchievementProgress(progress: List<AchievementProgressEntity>)

    @Query("DELETE FROM achievement_progress_table")
    suspend fun deleteAllAchievementProgress()

    @Query("SELECT * FROM achievement_stats_table WHERE column_stats_id = :statsId LIMIT 1")
    fun observeAchievementStats(
        statsId: Int = AchievementStatsEntity.STATS_SINGLETON_ID,
    ): Flow<AchievementStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAchievementStats(stats: AchievementStatsEntity)
}
