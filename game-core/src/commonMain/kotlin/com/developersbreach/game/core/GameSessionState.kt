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
    val maxLevelReached: Int
)

data class GameSessionUpdate(
    val state: GameSessionState,
    val levelCompleted: Boolean,
    val gameWon: Boolean,
    val gameLost: Boolean
)