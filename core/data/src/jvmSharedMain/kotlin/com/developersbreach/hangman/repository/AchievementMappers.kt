package com.developersbreach.hangman.repository

import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.database.entity.AchievementProgressEntity
import com.developersbreach.hangman.repository.database.entity.AchievementStatsEntity
import kotlinx.serialization.json.Json
import kotlin.collections.component1
import kotlin.collections.component2

internal fun AchievementProgress.toEntity(): AchievementProgressEntity {
    return AchievementProgressEntity(
        achievementId = achievementId.name,
        isUnlocked = isUnlocked,
        isUnread = isUnread,
        unlockedAtEpochMillis = unlockedAtEpochMillis,
        progressCurrent = progressCurrent,
        progressTarget = progressTarget,
    )
}

internal fun AchievementProgressEntity.toDomainOrNull(): AchievementProgress? {
    val id = runCatching { AchievementId.valueOf(achievementId) }.getOrNull() ?: return null
    return AchievementProgress(
        achievementId = id,
        isUnlocked = isUnlocked,
        isUnread = isUnread,
        unlockedAtEpochMillis = unlockedAtEpochMillis,
        progressCurrent = progressCurrent,
        progressTarget = progressTarget,
    )
}

internal fun AchievementStatCounters.toEntity(json: Json): AchievementStatsEntity {
    return AchievementStatsEntity(
        statsId = AchievementStatsEntity.STATS_SINGLETON_ID,
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesLost = gamesLost,
        historyEntriesRecordedTotal = historyEntriesRecordedTotal,
        currentWinStreak = currentWinStreak,
        bestWinStreak = bestWinStreak,
        totalHintsUsed = totalHintsUsed,
        gamesWonWithoutHints = gamesWonWithoutHints,
        perfectWins = perfectWins,
        highestScore = highestScore,
        winsByCategory = json.encodeToString(
            winsByCategory.mapKeys { (category, _) -> category.name },
        ),
        winsByDifficulty = json.encodeToString(
            winsByDifficulty.mapKeys { (difficulty, _) -> difficulty.name },
        ),
    )
}

internal fun AchievementStatsEntity.toDomain(json: Json): AchievementStatCounters {
    return AchievementStatCounters(
        gamesPlayed = gamesPlayed,
        gamesWon = gamesWon,
        gamesLost = gamesLost,
        historyEntriesRecordedTotal = historyEntriesRecordedTotal,
        currentWinStreak = currentWinStreak,
        bestWinStreak = bestWinStreak,
        totalHintsUsed = totalHintsUsed,
        gamesWonWithoutHints = gamesWonWithoutHints,
        perfectWins = perfectWins,
        highestScore = highestScore,
        winsByCategory = runCatching {
            json.decodeFromString<Map<String, Int>>(winsByCategory).mapNotNull { (key, value) ->
                runCatching { GameCategory.valueOf(key) }.getOrNull()?.let { category ->
                    category to value
                }
            }.toMap()
        }.getOrDefault(emptyMap()),
        winsByDifficulty = runCatching {
            json.decodeFromString<Map<String, Int>>(winsByDifficulty).mapNotNull { (key, value) ->
                runCatching { GameDifficulty.valueOf(key) }.getOrNull()?.let { difficulty ->
                    difficulty to value
                }
            }.toMap()
        }.getOrDefault(emptyMap()),
    )
}
