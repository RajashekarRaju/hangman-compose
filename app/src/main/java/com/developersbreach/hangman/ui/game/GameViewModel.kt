package com.developersbreach.hangman.ui.game

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.Alphabet
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.GameSessionEngine
import com.developersbreach.game.core.GameSessionState
import com.developersbreach.game.core.Words
import com.developersbreach.hangman.R
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.getDateAndTime
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for screen [GameScreen].
 * Instance created with koin.
 */
class GameViewModel(
    private val application: Application,
    private val repository: GameRepository
) : ViewModel() {

    val updatePlayerGuesses = mutableStateListOf<String>()

    private var _alphabetsList = MutableLiveData<List<Alphabet>>()
    val alphabetsList: LiveData<List<Alphabet>>
        get() = _alphabetsList

    var gameOverByWinning by mutableStateOf(false)
    var wordToGuess: String by mutableStateOf("")
    var attemptsLeftToGuess: Int by mutableIntStateOf(8)

    private var _revealGuessingWord = MutableLiveData(false)
    val revealGuessingWord: LiveData<Boolean>
        get() = _revealGuessingWord

    var pointsScoredOverall: Int by mutableIntStateOf(0)
    var currentPlayerLevel: Int by mutableIntStateOf(0)

    private val gamePreferences = GamePref(application)

    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)
    var gameCategory: GameCategory by mutableStateOf(GameCategory.COUNTRIES)

    private var guessingWordsForCurrentGame: List<Words> by mutableStateOf(listOf())

    val maxLevelReached: Int = 5

    private var gameSessionEngine: GameSessionEngine? = null
    private var gameOverByNoAttemptsLeft: Boolean = false

    private lateinit var mediaPlayer: MediaPlayer

    init {
        viewModelScope.launch {
            gameDifficulty = gamePreferences.getGameDifficultyPref()
            gameCategory = gamePreferences.getGameCategoryPref()

            guessingWordsForCurrentGame =
                repository.getRandomGuessingWord(gameDifficulty, gameCategory)

            gameSessionEngine = GameSessionEngine(
                guessingWordsForCurrentGame = guessingWordsForCurrentGame,
                maxAttempts = 8,
                levelsPerGame = maxLevelReached
            )
            syncState(gameSessionEngine!!.snapshot())
        }

        playCurrentGameSoundBased(R.raw.level_won)
    }

    fun checkIfLetterMatches(
        alphabet: Alphabet
    ) {
        val engine = gameSessionEngine ?: return
        val update = engine.guessAlphabet(alphabet.alphabetId)
        syncState(update.state)

        if (update.levelCompleted && !update.gameWon) {
            playCurrentGameSoundBased(R.raw.level_won)
        }

        if (update.gameWon) {
            playCurrentGameSoundBased(R.raw.game_won)
            viewModelScope.launch {
                delay(500)
                saveCurrentGameToHistory()
            }
        }

        if (update.gameLost) {
            viewModelScope.launch {
                playCurrentGameSoundBased(R.raw.game_lost)
                saveCurrentGameToHistory()
            }
        }

        playCurrentGameSoundBased(R.raw.alphabet_tap)
    }

    private fun syncState(state: GameSessionState) {
        _alphabetsList.value = state.alphabets
        updatePlayerGuesses.clear()
        updatePlayerGuesses.addAll(state.playerGuesses)
        wordToGuess = state.currentWord
        attemptsLeftToGuess = state.attemptsLeftToGuess
        currentPlayerLevel = state.currentPlayerLevel
        pointsScoredOverall = state.pointsScoredOverall
        gameOverByWinning = state.gameOverByWinning
        gameOverByNoAttemptsLeft = state.gameOverByNoAttemptsLeft
        _revealGuessingWord.value = state.gameOverByNoAttemptsLeft
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

    private fun playCurrentGameSoundBased(
        audio: Int
    ) {
        viewModelScope.launch {
            mediaPlayer = MediaPlayer.create(application.applicationContext, audio)
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }
    }

    override fun onCleared() {
        mediaPlayer.release()
    }
}