package com.developersbreach.game.core.achievements

object AchievementEvaluator {

    fun evaluate(
        currentProgress: List<AchievementProgress>,
        counters: AchievementStatCounters,
        session: AchievementSessionSignals,
        unlockedAtEpochMillis: Long,
    ): AchievementEvaluationResult {
        val progressById = AchievementCatalog.definitions.associate { definition ->
            val existing = currentProgress.firstOrNull { value -> value.achievementId == definition.id }
                ?: definition.initialProgress()
            definition.id to existing
        }.toMutableMap()

        val newlyUnlocked = linkedSetOf<AchievementId>()

        AchievementCatalog.definitions.forEach { definition ->
            if (definition.trigger == AchievementTrigger.ACHIEVEMENTS_UNLOCKED_TOTAL) {
                return@forEach
            }
            val existing = progressById.getValue(definition.id)
            val measured = measuredValue(definition, counters, session)
            val updated = updateProgress(
                existing = existing,
                measured = measured,
                target = definition.target,
                unlockedAtEpochMillis = unlockedAtEpochMillis,
            )
            if (!existing.isUnlocked && updated.isUnlocked) {
                newlyUnlocked += definition.id
            }
            progressById[definition.id] = updated
        }

        AchievementCatalog.definitions
            .firstOrNull { definition -> definition.trigger == AchievementTrigger.ACHIEVEMENTS_UNLOCKED_TOTAL }
            ?.let { definition ->
                val existing = progressById.getValue(definition.id)
                val unlockedCount = progressById.values.count { value -> value.isUnlocked }
                val updated = updateProgress(
                    existing = existing,
                    measured = unlockedCount,
                    target = definition.target,
                    unlockedAtEpochMillis = unlockedAtEpochMillis,
                )
                if (!existing.isUnlocked && updated.isUnlocked) {
                    newlyUnlocked += definition.id
                }
                progressById[definition.id] = updated
            }

        val orderedProgress = AchievementCatalog.definitions.map { definition ->
            progressById.getValue(definition.id)
        }
        return AchievementEvaluationResult(
            updatedProgress = orderedProgress,
            newlyUnlockedAchievementIds = orderedProgress
                .map { value -> value.achievementId }
                .filter { id -> id in newlyUnlocked },
        )
    }
}

private fun measuredValue(
    definition: AchievementDefinition,
    counters: AchievementStatCounters,
    session: AchievementSessionSignals,
): Int {
    return when (definition.trigger) {
        AchievementTrigger.GAMES_PLAYED_TOTAL -> counters.gamesPlayed
        AchievementTrigger.GAMES_WON_TOTAL -> counters.gamesWon
        AchievementTrigger.BEST_WIN_STREAK -> counters.bestWinStreak
        AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL -> counters.gamesWonWithoutHints
        AchievementTrigger.PERFECT_WINS_TOTAL -> counters.perfectWins
        AchievementTrigger.HIGHEST_SCORE -> counters.highestScore
        AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING -> when {
            definition.timeRemainingThresholdSeconds == null -> 0
            definition.timeRemainingThresholdSeconds >= 45 -> session.levelsFinishedWithAtLeast45Seconds
            else -> session.levelsFinishedWithAtLeast30Seconds
        }

        AchievementTrigger.WIN_IN_CATEGORY -> definition.requiredCategory?.let { category ->
            counters.winsByCategory[category] ?: 0
        } ?: 0

        AchievementTrigger.WIN_IN_DIFFICULTY -> definition.requiredDifficulty?.let { difficulty ->
            counters.winsByDifficulty[difficulty] ?: 0
        } ?: 0

        AchievementTrigger.UNIQUE_CATEGORIES_WON -> counters.winsByCategory.count { (_, wins) -> wins > 0 }
        AchievementTrigger.UNIQUE_DIFFICULTIES_WON -> counters.winsByDifficulty.count { (_, wins) -> wins > 0 }
        AchievementTrigger.SESSION_GAMES_PLAYED -> session.sessionGamesPlayed
        AchievementTrigger.LEVELS_COMPLETED_TOTAL -> session.levelsCompletedTotal
        AchievementTrigger.FULL_GAME_REVEAL_HINT_MAX -> qualifiesByHintLimit(
            lastGameWon = session.lastGameWon,
            hintUses = session.fullGameRevealHintsUsed,
            maxAllowedHintUses = definition.maxAllowedHintUses,
        )

        AchievementTrigger.FULL_GAME_ELIMINATE_HINT_MAX -> qualifiesByHintLimit(
            lastGameWon = session.lastGameWon,
            hintUses = session.fullGameEliminateHintsUsed,
            maxAllowedHintUses = definition.maxAllowedHintUses,
        )

        AchievementTrigger.LOW_HINT_WINS_TOTAL -> session.lowHintWinsTotal
        AchievementTrigger.HISTORY_ENTRIES_TOTAL -> counters.historyEntriesRecordedTotal
        AchievementTrigger.ACHIEVEMENTS_UNLOCKED_TOTAL -> 0
    }
}

private fun qualifiesByHintLimit(
    lastGameWon: Boolean,
    hintUses: Int,
    maxAllowedHintUses: Int?,
): Int {
    if (!lastGameWon || maxAllowedHintUses == null) return 0
    return if (hintUses <= maxAllowedHintUses) 1 else 0
}

private fun updateProgress(
    existing: AchievementProgress,
    measured: Int,
    target: Int,
    unlockedAtEpochMillis: Long,
): AchievementProgress {
    val boundedProgress = measured.coerceAtLeast(0).coerceAtMost(target)
    val mergedProgress = maxOf(existing.progressCurrent, boundedProgress)
    val shouldUnlock = existing.isUnlocked || mergedProgress >= target
    val unlockedNow = !existing.isUnlocked && shouldUnlock
    return existing.copy(
        isUnlocked = shouldUnlock,
        isUnread = when {
            unlockedNow -> true
            else -> existing.isUnread
        },
        unlockedAtEpochMillis = when {
            existing.unlockedAtEpochMillis != null -> existing.unlockedAtEpochMillis
            shouldUnlock -> unlockedAtEpochMillis
            else -> null
        },
        progressCurrent = mergedProgress,
        progressTarget = target,
    )
}
