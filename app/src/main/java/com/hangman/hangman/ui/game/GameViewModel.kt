package com.hangman.hangman.ui.game

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.alphabetsList
import kotlinx.coroutines.launch


class GameViewModel(
    application: Application,
    private val repository: GameRepository
) : AndroidViewModel(application) {

    // Determine whether player won or lost the game
    private var gameSummary by mutableStateOf(false)

    // To prevent player keep playing the game
    var gameOver by mutableStateOf(false)

    // Track the player progress towards current word.
    private var trackGuessingWord by mutableStateOf("")

    // Generated a random word to guess from database.
    var guessingWord by mutableStateOf("")

    // List of A-Z alphabets, let's user access alphabets in any order
    var alphabets by mutableStateOf(alphabetsList)

    // Keep track of attempts to find out game is
    var attemptsLeft by mutableStateOf(5)

    // Number of points depend on length of the string for guessed word.
    var pointsScoredPerWord by mutableStateOf(0)

    // Starting level with 1, last level is 5
    var currentPlayerLevel by mutableStateOf(1)

    init {
        viewModelScope.launch {
            guessingWord = repository.getRandomGuessingWord()
        }
    }

    fun checkIfLetterMatches(
        alphabet: Alphabets
    ) {
        viewModelScope.launch {
            val currentAlphabet = alphabet.alphabet.lowercase()
            val currentGuessingWord = guessingWord.lowercase()
            if (currentGuessingWord.contains(currentAlphabet)) {
                guessingWord = currentGuessingWord.replace(currentAlphabet, " ")
                trackGuessingWord += " "
            } else {
                minimizeAttempt()
            }

            // if true, player won the game
            gameSummary = guessingWord == trackGuessingWord
            if (gameSummary) {

                pointsScoredPerWord = guessingWord.length
                currentPlayerLevel += 1
                attemptsLeft = 5
                guessingWord = repository.getRandomGuessingWord()

                // Update game over to true immediately to prevent player keep guessing.
                gameOver = true
            }
        }
    }

    // Everytime player chooses the alphabet, reduce the attempt by 1.
    // If attempt reaches 0, player has lost.
    private fun minimizeAttempt() {
        if (attemptsLeft > 0) {
            attemptsLeft -= 1
            gameOver = attemptsLeft == 0
        }
    }

    companion object {

        fun provideFactory(
            application: Application,
            repository: GameRepository,
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                        return GameViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Cannot create Instance for GameViewModel class")
                }
            }
        }
    }
}