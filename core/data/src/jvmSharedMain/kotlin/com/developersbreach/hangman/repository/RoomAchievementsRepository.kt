package com.developersbreach.hangman.repository

import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.hangman.repository.database.GameDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class RoomAchievementsRepository(
    private val database: GameDatabase,
) : AchievementsRepository {

    private val json = Json { ignoreUnknownKeys = true }

    override fun observeAchievementProgress(): Flow<List<AchievementProgress>> {
        return database.achievementsDao.observeAchievementProgress().map { entities ->
            entities.mapNotNull { entity -> entity.toDomainOrNull() }
        }
    }

    override suspend fun replaceAchievementProgress(progress: List<AchievementProgress>) {
        withContext(Dispatchers.IO) {
            database.achievementsDao.deleteAllAchievementProgress()
            if (progress.isNotEmpty()) {
                database.achievementsDao.upsertAchievementProgress(
                    progress.map { value -> value.toEntity() },
                )
            }
        }
    }

    override fun observeAchievementStatCounters(): Flow<AchievementStatCounters> {
        return database.achievementsDao.observeAchievementStats().map { entity ->
            entity?.toDomain(json) ?: AchievementStatCounters()
        }
    }

    override suspend fun saveAchievementStatCounters(counters: AchievementStatCounters) {
        withContext(Dispatchers.IO) {
            database.achievementsDao.upsertAchievementStats(
                counters.toEntity(json),
            )
        }
    }
}