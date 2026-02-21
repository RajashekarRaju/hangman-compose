package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.Alphabet
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.LEVELS_PER_GAME
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL

data class GameUiState(
    val alphabetsList: List<Alphabet> = emptyList(),
    val playerGuesses: List<String> = emptyList(),
    val gameOverByWinning: Boolean = false,
    val revealGuessingWord: Boolean = false,
    val wordToGuess: String = "",
    val attemptsLeftToGuess: Int = MAX_ATTEMPTS_PER_LEVEL,
    val pointsScoredOverall: Int = 0,
    val currentPlayerLevel: Int = 0,
    val maxLevelReached: Int = LEVELS_PER_GAME,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val showInstructionsDialog: Boolean = false,
    val showExitDialog: Boolean = false,
    val levelTimeTotalMillis: Long = 60_000L,
    val levelTimeRemainingMillis: Long = 60_000L,
)

val GameUiState.levelTimeProgress: Float
    get() = when {
        levelTimeTotalMillis <= 0L -> 0f
        else -> (levelTimeRemainingMillis.toFloat() / levelTimeTotalMillis.toFloat()).coerceIn(0f, 1f)
    }

val GameUiState.displayedLevel: Int
    get() = resolveDisplayedLevel(currentPlayerLevel, maxLevelReached)

internal fun resolveDisplayedLevel(
    currentPlayerLevel: Int,
    maxLevelReached: Int,
): Int {
    return if (currentPlayerLevel < maxLevelReached) currentPlayerLevel + 1 else maxLevelReached
}

internal fun levelProgress(currentPlayerLevel: Int): Float {
    return (currentPlayerLevel.coerceIn(0, LEVELS_PER_GAME).toFloat() / LEVELS_PER_GAME.toFloat())
        .coerceIn(0f, 1f)
}

internal fun attemptsUsedProgress(attemptsLeft: Int): Float {
    val clampedAttemptsLeft = attemptsLeft.coerceIn(0, MAX_ATTEMPTS_PER_LEVEL)
    val attemptsUsed = MAX_ATTEMPTS_PER_LEVEL - clampedAttemptsLeft
    return (attemptsUsed.toFloat() / MAX_ATTEMPTS_PER_LEVEL.toFloat()).coerceIn(0f, 1f)
}
