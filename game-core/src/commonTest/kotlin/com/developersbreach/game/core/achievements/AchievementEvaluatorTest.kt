package com.developersbreach.game.core.achievements

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AchievementEvaluatorTest {

    @Test
    fun `evaluator unlocks streak achievements using best streak`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(bestWinStreak = 5),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 100L,
        )

        assertEquals(
            listOf(AchievementId.UNBROKEN, AchievementId.RELENTLESS),
            result.newlyUnlockedAchievementIds,
        )
        assertTrue(result.updatedProgress.byId(AchievementId.UNBROKEN).isUnlocked)
        assertTrue(result.updatedProgress.byId(AchievementId.RELENTLESS).isUnlocked)
        assertEquals(5, result.updatedProgress.byId(AchievementId.RELENTLESS).progressCurrent)
    }

    @Test
    fun `evaluator unlocks no hint achievements from no hint wins`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(gamesWonWithoutHints = 10),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 200L,
        )

        assertEquals(
            listOf(
                AchievementId.NO_CRUTCHES,
                AchievementId.MIND_READER,
                AchievementId.COLD_TURKEY,
            ),
            result.newlyUnlockedAchievementIds,
        )
    }

    @Test
    fun `evaluator unlocks perfect achievements from perfect wins`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(perfectWins = 5),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 300L,
        )

        assertEquals(
            listOf(AchievementId.PERFECT_HUNT, AchievementId.SURGEON),
            result.newlyUnlockedAchievementIds,
        )
    }

    @Test
    fun `evaluator unlocks category completion achievements`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(
                winsByCategory = mapOf(
                    GameCategory.COUNTRIES to 2,
                    GameCategory.LANGUAGES to 1,
                    GameCategory.COMPANIES to 1,
                    GameCategory.ANIMALS to 4,
                ),
            ),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 400L,
        )

        assertEquals(
            listOf(
                AchievementId.COUNTRY_SLAYER,
                AchievementId.TONGUE_TAMER,
                AchievementId.CORPORATE_CURSE,
                AchievementId.BEAST_BINDER,
                AchievementId.WORLD_COMPLETIONIST,
            ),
            result.newlyUnlockedAchievementIds,
        )
    }

    @Test
    fun `evaluator unlocks difficulty completion achievements`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(
                winsByDifficulty = mapOf(
                    GameDifficulty.EASY to 3,
                    GameDifficulty.MEDIUM to 2,
                    GameDifficulty.HARD to 1,
                    GameDifficulty.VERY_HARD to 1,
                ),
            ),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 500L,
        )

        assertEquals(
            listOf(
                AchievementId.EASY_CLEARED,
                AchievementId.MEDIUM_CLEARED,
                AchievementId.HARD_CLEARED,
                AchievementId.VERY_HARD_CLEARED,
                AchievementId.DIFFICULTY_MASTER,
            ),
            result.newlyUnlockedAchievementIds,
        )
    }

    @Test
    fun `evaluator unlocks score thresholds from highest score`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(highestScore = 500),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 600L,
        )

        assertEquals(
            listOf(AchievementId.SCORE_100, AchievementId.SCORE_250, AchievementId.SCORE_500),
            result.newlyUnlockedAchievementIds,
        )
    }

    @Test
    fun `evaluator is idempotent for already unlocked achievements`() {
        val firstResult = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(gamesWon = 1),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 700L,
        )
        val secondResult = AchievementEvaluator.evaluate(
            currentProgress = firstResult.updatedProgress,
            counters = AchievementStatCounters(gamesWon = 1),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 900L,
        )

        assertEquals(listOf(AchievementId.FIRST_WIN), firstResult.newlyUnlockedAchievementIds)
        assertTrue(secondResult.newlyUnlockedAchievementIds.isEmpty())
        assertEquals(700L, secondResult.updatedProgress.byId(AchievementId.FIRST_WIN).unlockedAtEpochMillis)
    }

    @Test
    fun `evaluator unlocks collector after same pass unlocks`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(
                gamesPlayed = 50,
                gamesWon = 25,
                bestWinStreak = 10,
                historyEntriesRecordedTotal = 20,
                gamesWonWithoutHints = 10,
                perfectWins = 5,
                highestScore = 500,
                winsByCategory = mapOf(
                    GameCategory.COUNTRIES to 1,
                    GameCategory.LANGUAGES to 1,
                    GameCategory.COMPANIES to 1,
                    GameCategory.ANIMALS to 1,
                ),
                winsByDifficulty = mapOf(
                    GameDifficulty.EASY to 1,
                    GameDifficulty.MEDIUM to 1,
                    GameDifficulty.HARD to 1,
                    GameDifficulty.VERY_HARD to 1,
                ),
            ),
            session = AchievementSessionSignals(
                sessionGamesPlayed = 5,
                levelsCompletedTotal = 50,
                levelsFinishedWithAtLeast30Seconds = 10,
                levelsFinishedWithAtLeast45Seconds = 5,
                lowHintWinsTotal = 3,
                fullGameRevealHintsUsed = 1,
                fullGameEliminateHintsUsed = 1,
                lastGameWon = true,
            ),
            unlockedAtEpochMillis = 800L,
        )

        assertTrue(AchievementId.CURSE_COLLECTOR in result.newlyUnlockedAchievementIds)
    }

    @Test
    fun `evaluator uses monotonic history entries counter for archivist`() {
        val result = AchievementEvaluator.evaluate(
            currentProgress = initialProgress(),
            counters = AchievementStatCounters(historyEntriesRecordedTotal = 20),
            session = AchievementSessionSignals(),
            unlockedAtEpochMillis = 900L,
        )

        assertTrue(AchievementId.ARCHIVIST in result.newlyUnlockedAchievementIds)
    }
}

private fun initialProgress(): List<AchievementProgress> {
    return AchievementCatalog.definitions.map { definition -> definition.initialProgress() }
}

private fun List<AchievementProgress>.byId(id: AchievementId): AchievementProgress {
    return first { progress -> progress.achievementId == id }
}
