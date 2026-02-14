package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.Alphabet
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

data class GameUiState(
    val alphabetsList: List<Alphabet> = emptyList(),
    val playerGuesses: List<String> = emptyList(),
    val gameOverByWinning: Boolean = false,
    val revealGuessingWord: Boolean = false,
    val wordToGuess: String = "",
    val attemptsLeftToGuess: Int = 8,
    val pointsScoredOverall: Int = 0,
    val currentPlayerLevel: Int = 0,
    val maxLevelReached: Int = 5,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val showInstructionsDialog: Boolean = false,
    val showExitDialog: Boolean = false,
)
