package com.developersbreach.game.core.achievements

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class AchievementDefinition(
    val id: AchievementId,
    val title: String,
    val description: String,
    val group: AchievementGroup,
    val rarity: AchievementRarity,
    val trigger: AchievementTrigger,
    val target: Int,
    val requiredCategory: GameCategory? = null,
    val requiredDifficulty: GameDifficulty? = null,
    val timeRemainingThresholdSeconds: Int? = null,
    val maxAllowedHintUses: Int? = null,
)

data class AchievementProgress(
    val achievementId: AchievementId,
    val isUnlocked: Boolean = false,
    val isUnread: Boolean = false,
    val unlockedAtEpochMillis: Long? = null,
    val progressCurrent: Int = 0,
    val progressTarget: Int,
)

data class AchievementStatCounters(
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
    val winsByCategory: Map<GameCategory, Int> = emptyMap(),
    val winsByDifficulty: Map<GameDifficulty, Int> = emptyMap(),
)

data class AchievementSessionSignals(
    val sessionGamesPlayed: Int = 0,
    val levelsCompletedTotal: Int = 0,
    val levelsFinishedWithAtLeast30Seconds: Int = 0,
    val levelsFinishedWithAtLeast45Seconds: Int = 0,
    val fullGameRevealHintsUsed: Int = 0,
    val fullGameEliminateHintsUsed: Int = 0,
    val lowHintWinsTotal: Int = 0,
    val lastGameWon: Boolean = false,
)

data class AchievementEvaluationResult(
    val updatedProgress: List<AchievementProgress>,
    val newlyUnlockedAchievementIds: List<AchievementId>,
)

fun AchievementDefinition.initialProgress(): AchievementProgress {
    return AchievementProgress(
        achievementId = id,
        progressTarget = target,
    )
}
