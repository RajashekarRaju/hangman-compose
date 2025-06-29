package com.developersbreach.hangman.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.modal.Alphabets
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.PlatformAudioPlayer
import com.developersbreach.hangman.utils.Words
import com.developersbreach.hangman.utils.alphabetsList
import com.developersbreach.hangman.utils.getDateAndTime
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepository,
    private val gamePref: GamePref,
    private val audioPlayer: PlatformAudioPlayer
) : BaseViewModel() {

    val updatePlayerGuesses = mutableStateListOf<String>()

    private var _alphabetsList = MutableStateFlow<List<Alphabets>>(emptyList())
    val alphabetsList: StateFlow<List<Alphabets>> get() = _alphabetsList

    private var playerWonTheCurrentLevel by mutableStateOf(false)
    private var gameOverByNoAttemptsLeft by mutableStateOf(false)
    var gameOverByWinning by mutableStateOf(false)

    var wordToGuess: String by mutableStateOf("")
    var attemptsLeftToGuess: Int by mutableIntStateOf(8)

    private var _revealGuessingWord = MutableStateFlow(gameOverByNoAttemptsLeft)
    val revealGuessingWord: StateFlow<Boolean> get() = _revealGuessingWord

    private var pointsScoredPerWord: Int by mutableIntStateOf(0)
    var pointsScoredOverall: Int by mutableIntStateOf(0)
    var currentPlayerLevel: Int by mutableIntStateOf(0)

    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)
    var gameCategory: GameCategory by mutableStateOf(GameCategory.COUNTRIES)

    private var guessingWordsForCurrentGame: List<Words> by mutableStateOf(listOf())

    val maxLevelReached: Int = 5
    private val pointsScoredInEachLevel = mutableListOf(0, 0, 0, 0, 0)

    init {
        viewModelScope.launch {
            gameDifficulty = gamePref.getGameDifficultyPref()
            gameCategory = gamePref.getGameCategoryPref()
            guessingWordsForCurrentGame = repository.getRandomGuessingWord(gameDifficulty, gameCategory)
            wordToGuess = guessingWordsForCurrentGame[currentPlayerLevel].wordName
            updateOrResetWordToGuess()
        }
        audioPlayer.play("level_won.mp3")
    }

    fun checkIfLetterMatches(alphabet: Alphabets) {
        viewModelScope.launch {
            if (gameOverByNoAttemptsLeft) return@launch

            val currentAlphabet: String = alphabet.alphabet.lowercase()
            val currentGuessingWord: String = wordToGuess.lowercase()

            _alphabetsList.value = _alphabetsList.value.map {
                if (it.alphabetId == alphabet.alphabetId) it.copy(isAlphabetGuessed = true) else it
            }

            if (currentGuessingWord.contains(currentAlphabet)) {
                for (notI in currentGuessingWord.indices) {
                    if (currentGuessingWord[notI].toString() == currentAlphabet) {
                        updatePlayerGuesses[notI] = currentAlphabet
                    }
                }
                if (!updatePlayerGuesses.contains(" ")) {
                    playerWonTheCurrentLevel = true
                    gameOverByNoAttemptsLeft = false
                    _revealGuessingWord.value = gameOverByNoAttemptsLeft
                }
            } else {
                minimizeAttempt()
            }

            if (playerWonTheCurrentLevel) {
                pointsScoredPerWord = wordToGuess.length
                attemptsLeftToGuess = 8
                if (currentPlayerLevel < maxLevelReached) currentPlayerLevel += 1
                if (currentPlayerLevel < maxLevelReached) {
                    wordToGuess = guessingWordsForCurrentGame[currentPlayerLevel].wordName
                }
                gameOverByNoAttemptsLeft = false
                delay(500)
                updateOrResetWordToGuess()
                if (playerWonTheCurrentLevel && currentPlayerLevel == 5) {
                    gameOverByWinning = true
                    audioPlayer.play("game_won.mp3")
                    viewModelScope.launch {
                        delay(500)
                        saveCurrentGameToHistory()
                    }
                }
                if (playerWonTheCurrentLevel && currentPlayerLevel < 5) {
                    audioPlayer.play("level_won.mp3")
                }
                playerWonTheCurrentLevel = false
            }
            calculateOverallPointsScoredEachLevel()
        }
        audioPlayer.play("alphabet_tap.mp3")
    }

    private fun calculateOverallPointsScoredEachLevel() {
        when (currentPlayerLevel) {
            1 -> pointsScoredInEachLevel[0] = pointsScoredPerWord
            2 -> pointsScoredInEachLevel[1] = pointsScoredPerWord
            3 -> pointsScoredInEachLevel[2] = pointsScoredPerWord
            4 -> pointsScoredInEachLevel[3] = pointsScoredPerWord
            5 -> pointsScoredInEachLevel[4] = pointsScoredPerWord
        }
        pointsScoredOverall = pointsScoredInEachLevel.sum()
    }

    private fun minimizeAttempt() {
        if (attemptsLeftToGuess > 0) {
            attemptsLeftToGuess -= 1
            gameOverByNoAttemptsLeft = attemptsLeftToGuess == 0
            _revealGuessingWord.value = gameOverByNoAttemptsLeft
            if (gameOverByNoAttemptsLeft) {
                viewModelScope.launch {
                    audioPlayer.play("game_lost.mp3")
                    saveCurrentGameToHistory()
                }
            }
        }
    }

    private fun updateOrResetWordToGuess() {
        _alphabetsList.value = if (_alphabetsList.value.isEmpty()) {
            alphabetsList()
        } else {
            _alphabetsList.value.map { it.copy(isAlphabetGuessed = false) }
        }
        updatePlayerGuesses.clear()
        for (i in wordToGuess.indices) {
            updatePlayerGuesses.add(" ")
        }
    }

    private suspend fun saveCurrentGameToHistory() {
        val (date, time) = getDateAndTime()
        repository.saveCurrentGameToHistory(
            HistoryEntity(
                gameId = UUID.randomUUID().toString(),
                gameScore = pointsScoredOverall,
                gameLevel = currentPlayerLevel,
                gameSummary = gameOverByWinning,
                gameDifficulty = gameDifficulty,
                gameCategory = gameCategory,
                gamePlayedTime = time,
                gamePlayedDate = date
            )
        )
    }

    override fun onCleared() {
        audioPlayer.release()
    }
}
