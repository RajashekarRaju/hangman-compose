package com.developersbreach.hangman.repository

import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import kotlinx.coroutines.flow.Flow

interface AchievementsRepository {
    fun observeAchievementProgress(): Flow<List<AchievementProgress>>

    suspend fun replaceAchievementProgress(progress: List<AchievementProgress>)

    fun observeAchievementStatCounters(): Flow<AchievementStatCounters>

    suspend fun saveAchievementStatCounters(counters: AchievementStatCounters)
}
