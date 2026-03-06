package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementEvaluator
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementSessionSignals
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.GameSessionUpdate
import com.developersbreach.game.core.HintType
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.game.core.achievements.initialProgress

internal class GameAchievementTracker(
    private val nowMillis: () -> Long,
) {
    private var achievementProgress: List<AchievementProgress> =
        AchievementCatalog.definitions.map { it.initialProgress() }
    private var achievementCounters: AchievementStatCounters = AchievementStatCounters()
    private var sessionGamesPlayed: Int = 0
    private var levelsCompletedTotal: Int = 0
    private var levelsFinishedAtLeast30Seconds: Int = 0
    private var levelsFinishedAtLeast45Seconds: Int = 0
    private var lowHintWinsTotal: Int = 0

    fun hydrate(
        initialProgress: List<AchievementProgress>,
        initialCounters: AchievementStatCounters,
    ) {
        achievementProgress = initialProgress
        achievementCounters = initialCounters
        hydrateDerivedSignalsFromProgress(initialProgress)
    }

    fun onGameUpdate(
        update: GameSessionUpdate,
        levelTimeRemainingAtUpdate: Long,
        gameCategory: GameCategory,
        gameDifficulty: GameDifficulty,
    ): AchievementTrackingResult {
        val nextCounters = updatedCounters(
            previous = achievementCounters,
            update = update,
            gameCategory = gameCategory,
            gameDifficulty = gameDifficulty,
        )

        val nextSignals = updatedSignals(
            update = update,
            levelTimeRemainingAtUpdate = levelTimeRemainingAtUpdate,
        )

        val evaluation = AchievementEvaluator.evaluate(
            currentProgress = achievementProgress,
            counters = nextCounters,
            session = nextSignals,
            unlockedAtEpochMillis = nowMillis(),
        )

        achievementCounters = nextCounters
        achievementProgress = evaluation.updatedProgress

        return AchievementTrackingResult(
            updatedProgress = evaluation.updatedProgress,
            updatedCounters = nextCounters,
            newlyUnlockedIds = evaluation.newlyUnlockedAchievementIds,
        )
    }

    fun onHistoryRecorded(): AchievementTrackingResult {
        val nextCounters = achievementCounters.copy(
            historyEntriesRecordedTotal = achievementCounters.historyEntriesRecordedTotal + 1,
        )
        val evaluation = AchievementEvaluator.evaluate(
            currentProgress = achievementProgress,
            counters = nextCounters,
            session = AchievementSessionSignals(
                sessionGamesPlayed = sessionGamesPlayed,
                levelsCompletedTotal = levelsCompletedTotal,
                levelsFinishedWithAtLeast30Seconds = levelsFinishedAtLeast30Seconds,
                levelsFinishedWithAtLeast45Seconds = levelsFinishedAtLeast45Seconds,
                lowHintWinsTotal = lowHintWinsTotal,
                lastGameWon = false,
            ),
            unlockedAtEpochMillis = nowMillis(),
        )

        achievementCounters = nextCounters
        achievementProgress = evaluation.updatedProgress

        return AchievementTrackingResult(
            updatedProgress = evaluation.updatedProgress,
            updatedCounters = nextCounters,
            newlyUnlockedIds = evaluation.newlyUnlockedAchievementIds,
        )
    }

    private fun updatedCounters(
        previous: AchievementStatCounters,
        update: GameSessionUpdate,
        gameCategory: GameCategory,
        gameDifficulty: GameDifficulty,
    ): AchievementStatCounters {
        if (!update.gameWon && !update.gameLost) return previous

        val currentState = update.state
        val nextGamesPlayed = previous.gamesPlayed + 1
        val nextGamesWon = previous.gamesWon + if (update.gameWon) 1 else 0
        val nextGamesLost = previous.gamesLost + if (update.gameLost) 1 else 0
        val nextCurrentWinStreak = when {
            update.gameWon -> previous.currentWinStreak + 1
            else -> 0
        }
        val nextBestWinStreak = maxOf(previous.bestWinStreak, nextCurrentWinStreak)
        val nextTotalHintsUsed = previous.totalHintsUsed + currentState.hintsUsedTotal
        val nextGamesWonWithoutHints = previous.gamesWonWithoutHints + when {
            update.gameWon && currentState.hintsUsedTotal == 0 -> 1
            else -> 0
        }
        val nextPerfectWins = previous.perfectWins + when {
            update.gameWon && currentState.attemptsLeftToGuess == MAX_ATTEMPTS_PER_LEVEL -> 1
            else -> 0
        }
        val nextHighestScore = maxOf(previous.highestScore, currentState.pointsScoredOverall)
        val nextWinsByCategory = previous.winsByCategory.toMutableMap().apply {
            if (update.gameWon) {
                this[gameCategory] = (this[gameCategory] ?: 0) + 1
            }
        }
        val nextWinsByDifficulty = previous.winsByDifficulty.toMutableMap().apply {
            if (update.gameWon) {
                this[gameDifficulty] = (this[gameDifficulty] ?: 0) + 1
            }
        }

        return previous.copy(
            gamesPlayed = nextGamesPlayed,
            gamesWon = nextGamesWon,
            gamesLost = nextGamesLost,
            currentWinStreak = nextCurrentWinStreak,
            bestWinStreak = nextBestWinStreak,
            totalHintsUsed = nextTotalHintsUsed,
            gamesWonWithoutHints = nextGamesWonWithoutHints,
            perfectWins = nextPerfectWins,
            highestScore = nextHighestScore,
            winsByCategory = nextWinsByCategory,
            winsByDifficulty = nextWinsByDifficulty,
        )
    }

    private fun updatedSignals(
        update: GameSessionUpdate,
        levelTimeRemainingAtUpdate: Long,
    ): AchievementSessionSignals {
        if (update.levelCompleted) {
            levelsCompletedTotal += 1
            val secondsRemaining = (levelTimeRemainingAtUpdate / 1_000L).toInt()
            if (secondsRemaining >= 30) levelsFinishedAtLeast30Seconds += 1
            if (secondsRemaining >= 45) levelsFinishedAtLeast45Seconds += 1
        }

        val currentHintsUsed = update.state.hintsUsedTotal
        val revealHintsUsed = when {
            update.gameWon || update.gameLost -> update.state.hintTypesUsed.count { it == HintType.REVEAL_LETTER }
            else -> 0
        }
        val eliminateHintsUsed = when {
            update.gameWon || update.gameLost -> update.state.hintTypesUsed.count { it == HintType.ELIMINATE_LETTERS }
            else -> 0
        }

        if (update.gameWon && currentHintsUsed <= 1) {
            lowHintWinsTotal += 1
        }
        if (update.gameWon || update.gameLost) {
            sessionGamesPlayed += 1
        }

        return AchievementSessionSignals(
            sessionGamesPlayed = sessionGamesPlayed,
            levelsCompletedTotal = levelsCompletedTotal,
            levelsFinishedWithAtLeast30Seconds = levelsFinishedAtLeast30Seconds,
            levelsFinishedWithAtLeast45Seconds = levelsFinishedAtLeast45Seconds,
            fullGameRevealHintsUsed = revealHintsUsed,
            fullGameEliminateHintsUsed = eliminateHintsUsed,
            lowHintWinsTotal = lowHintWinsTotal,
            lastGameWon = update.gameWon,
        )
    }

    private fun hydrateDerivedSignalsFromProgress(progress: List<AchievementProgress>) {

        fun progressFor(id: AchievementId): Int {
            return progress.firstOrNull { value -> value.achievementId == id }?.progressCurrent ?: 0
        }

        levelsCompletedTotal = maxOf(
            progressFor(AchievementId.GRINDSTONE),
            progressFor(AchievementId.BONE_MILL),
            progressFor(AchievementId.NIGHT_REAPER),
        )
        levelsFinishedAtLeast30Seconds = maxOf(
            progressFor(AchievementId.TIME_KEEPER),
            progressFor(AchievementId.CLOCK_BREAKER),
        )
        levelsFinishedAtLeast45Seconds = progressFor(AchievementId.SANDGLASS_LORD)
        lowHintWinsTotal = progressFor(AchievementId.HINT_MINIMALIST)
    }

    internal data class AchievementTrackingResult(
        val updatedProgress: List<AchievementProgress>,
        val updatedCounters: AchievementStatCounters,
        val newlyUnlockedIds: List<AchievementId>,
    )
}
