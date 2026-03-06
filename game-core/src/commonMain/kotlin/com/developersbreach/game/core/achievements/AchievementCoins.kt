package com.developersbreach.game.core.achievements

fun AchievementDefinition.coinReward(): Int = rarity.coinReward

fun totalAchievementCoins(progress: List<AchievementProgress>): Int {
    val unlockedIds = progress.filter { value -> value.isUnlocked }.map { value -> value.achievementId }.toSet()
    return AchievementCatalog.definitions.sumOf { definition ->
        if (definition.id in unlockedIds) definition.coinReward() else 0
    }
}
