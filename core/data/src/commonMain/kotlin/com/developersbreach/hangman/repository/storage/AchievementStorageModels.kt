package com.developersbreach.hangman.repository.storage

import com.developersbreach.game.core.AchievementId
import com.developersbreach.game.core.AchievementProgress
import com.developersbreach.game.core.AchievementStatCounters
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import kotlinx.serialization.Serializable

@Serializable
data class StoredAchievementProgress(
    val achievementId: String,
    val isUnlocked: Boolean,
    val unlockedAtEpochMillis: Long? = null,
    val progressCurrent: Int,
    val progressTarget: Int,
)

fun StoredAchievementProgress.toDomain(): AchievementProgress? {
    val id = runCatching { AchievementId.valueOf(achievementId) }.getOrNull() ?: return null
    return AchievementProgress(
        achievementId = id,
        isUnlocked = isUnlocked,
        unlockedAtEpochMillis = unlockedAtEpochMillis,
        progressCurrent = progressCurrent,
        progressTarget = progressTarget,
    )
}

fun AchievementProgress.toStored(): StoredAchievementProgress {
    return StoredAchievementProgress(
        achievementId = achievementId.name,
        isUnlocked = isUnlocked,
        unlockedAtEpochMillis = unlockedAtEpochMillis,
        progressCurrent = progressCurrent,
        progressTarget = progressTarget,
    )
}

@Serializable
data class StoredAchievementStatCounters(
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val gamesLost: Int = 0,
    val historyEntriesRecordedTotal: Int = 0,
    val currentWinStreak: Int = 0,
    val bestWinStreak: Int = 0,
    val totalHintsUsed: Int = 0,
    val gamesWonWithoutHints: Int = 0,
    val perfectWins: Int = 0,
    val highestScore: Int = 0,
    val winsByCategory: Map<String, Int> = emptyMap(),
    val winsByDifficulty: Map<String, Int> = emptyMap(),
)

fun StoredAchievementStatCounters.toDomain(): AchievementStatCounters {
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
        winsByCategory = winsByCategory.mapNotNull { (key, value) ->
            runCatching { GameCategory.valueOf(key) }.getOrNull()?.let { it to value }
        }.toMap(),
        winsByDifficulty = winsByDifficulty.mapNotNull { (key, value) ->
            runCatching { GameDifficulty.valueOf(key) }.getOrNull()?.let { it to value }
        }.toMap(),
    )
}

fun AchievementStatCounters.toStored(): StoredAchievementStatCounters {
    return StoredAchievementStatCounters(
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
        winsByCategory = winsByCategory.mapKeys { (category, _) -> category.name },
        winsByDifficulty = winsByDifficulty.mapKeys { (difficulty, _) -> difficulty.name },
    )
}