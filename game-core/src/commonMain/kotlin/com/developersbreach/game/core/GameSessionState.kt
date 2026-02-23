package com.developersbreach.game.core

data class GameSessionState(
    val alphabets: List<Alphabet>,
    val playerGuesses: List<String>,
    val currentWord: String,
    val attemptsLeftToGuess: Int,
    val currentPlayerLevel: Int,
    val pointsScoredOverall: Int,
    val gameOverByWinning: Boolean,
    val gameOverByNoAttemptsLeft: Boolean,
    val maxLevelReached: Int,
    val hintsRemaining: Int,
    val hintsUsedTotal: Int,
    val hintTypesUsed: Set<HintType>,
)

data class GameSessionUpdate(
    val state: GameSessionState,
    val levelCompleted: Boolean,
    val gameWon: Boolean,
    val gameLost: Boolean,
    val hintType: HintType? = null,
    val hintApplied: Boolean = false,
    val hintError: HintError? = null,
    val revealedIndexes: List<Int> = emptyList(),
    val eliminatedAlphabetIds: List<Int> = emptyList(),
)