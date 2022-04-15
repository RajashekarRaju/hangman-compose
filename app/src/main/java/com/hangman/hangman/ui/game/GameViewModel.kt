package com.hangman.hangman.ui.game

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.hangman.hangman.R
import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.repository.*
import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


/**
 * ViewModel for screen [GameScreen].
 * Initialized with koin.
 */
class GameViewModel(
    private val application: Application,
    private val repository: GameRepository
) : ViewModel() {

    // Determine whether player won or lost the game
    private var playerWonTheCurrentLevel by mutableStateOf(false)

    // To prevent player keep playing the current game level.
    var gameOverByNoAttemptsLeft by mutableStateOf(false)

    // Player has completed all 5 levels and won the game.
    var gameOverByWinning by mutableStateOf(false)

    // Generated a random country word to guess from database.
    var wordToGuess by mutableStateOf("")

    // Keep track of attempts to find out whether or not to finish the game
    var attemptsLeftToGuess by mutableStateOf(8)

    // Reveal the word if player lost the game at any level.
    private var _revealGuessingWord = MutableLiveData(false)
    val revealGuessingWord: LiveData<Boolean>
        get() = _revealGuessingWord

    // Number of points depend on length of the string for guessed word.
    private var pointsScoredPerWord by mutableStateOf(0)

    // Keeps track of all points scored in each level.
    var pointsScoredOverall by mutableStateOf(0)

    // Starting level with 1, last level is 5
    var currentPlayerLevel by mutableStateOf(0)

    // Get shared preferences for value game difficulty.
    private val gameDifficultyPreferences = GameDifficultyPref(application)
    private val gameCategoryPreferences = GameCategoryPref(application)

    // Set default state game difficulty value to easy and update with latest changes.
    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)
    var gameCategory: GameCategory by mutableStateOf(GameCategory.COUNTRIES)

    // Contains 5 words in a list for current game, 1 for each level.
    private var guessingWordsForCurrentGame by mutableStateOf(listOf<Words>())

    // 5 is the last level player needs to reach. At the point do not increment level
    val maxLevelReached = 5

    // Keeps track of player score from each level.
    private val pointsScoredInEachLevel = arrayListOf(0, 0, 0, 0, 0)

    // Everytime initializes and plays the game sound.
    private lateinit var mediaPlayer: MediaPlayer

    // This acts like a mediator, makes sure indices are reset/updated.
    // Helps us determine whether game should be finished by reading values in indices.
    private val updatePlayerGuesses = arrayListOf<Char>()

    private var _updateGuessesByPlayer = MutableLiveData(updatePlayerGuesses)
    val updateGuessesByPlayer: LiveData<ArrayList<Char>>
        get() = _updateGuessesByPlayer

    // List of A-Z alphabets, let's player access alphabets in any order
    private var _alphabets = MutableLiveData(alphabetsList())
    val alphabets: LiveData<List<Alphabets>>
        get() = _alphabets

    init {
        viewModelScope.launch {
            // Get player saved game difficulty level from preferences.
            gameDifficulty = gameDifficultyPreferences.getGameDifficultyPref()
            gameCategory = gameCategoryPreferences.getGameCategoryPref()

            // Based on difficulty level, get 5 unique random words from database.
            guessingWordsForCurrentGame =
                repository.getRandomGuessingWord(gameDifficulty, gameCategory)
            // From list of 5 words, starting from it's first index position sequentially return
            // a new word to guess for matching level.
            // If level is one first word will be returned, till level 5.
            wordToGuess = guessingWordsForCurrentGame[currentPlayerLevel].wordName
            // Reset guesses, update new word indices for current/next level word.
            updateOrResetWordToGuess()

            Timber.e(wordToGuess)
        }

        playCurrentGameSoundBased(R.raw.level_won)
    }

    // Called everytime when player chosen any word from list of alphabets.
    fun checkIfLetterMatches(
        alphabet: Alphabets
    ) {
        viewModelScope.launch {
            // Make sure to compare valid strings/chars by keeping it same letter case.
            val currentAlphabet = alphabet.alphabet.lowercase().first()
            val currentGuessingWord = wordToGuess.lowercase()

            if (currentGuessingWord.contains(currentAlphabet)) {
                // Since letter was a match, loop into indices range.
                for (notI in currentGuessingWord.indices) {
                    // From the matched word, find at which position alphabet match took place.
                    if (currentGuessingWord[notI] == currentAlphabet) {
                        // For matched position, pass that alphabet to the position to reflect in UI.
                        updatePlayerGuesses[notI] = currentAlphabet
                    }
                }

                // Reaching at this point, word has been guessed correctly.
                if (!updatePlayerGuesses.contains(' ')) {
                    // When none of the characters from word to guess contains empty character,
                    // player has won the current level, but not the whole game.
                    playerWonTheCurrentLevel = true
                    gameOverByNoAttemptsLeft = false
                    _revealGuessingWord.value = gameOverByNoAttemptsLeft
                }
            } else {
                // When match wasn't successful, this will be executed.
                minimizeAttempt()
            }

            // if true, player won the level, now move to next level
            if (playerWonTheCurrentLevel) {

                // Reward points for level by length of guessed word.
                pointsScoredPerWord = wordToGuess.length
                // If level is reset/new, reset the attempts left to default.
                attemptsLeftToGuess = 8

                // Everytime player clears the level, update to new level by +1.
                if (currentPlayerLevel < maxLevelReached) {
                    currentPlayerLevel += 1
                }

                if (currentPlayerLevel < maxLevelReached) {
                    // Get new word from saved guessing list by updated level.
                    wordToGuess = guessingWordsForCurrentGame[currentPlayerLevel].wordName
                }
                // Game isn't over, but level is. For clarity update this game isn't over.
                gameOverByNoAttemptsLeft = false

                // Reset guesses, update new word indices for next level word.
                kotlin.run {
                    delay(500)
                    updateOrResetWordToGuess()
                }
                Timber.e("Level 2 ==> $wordToGuess")

                if (playerWonTheCurrentLevel && currentPlayerLevel == 5) {
                    gameOverByWinning = true
                    playCurrentGameSoundBased(R.raw.game_won)
                    // Saves the game to history, needs couple of changes yet.
                    saveCurrentGameToHistory()
                }

                // Prevents media player from playing game won sound instead of level won sound.
                if (playerWonTheCurrentLevel && currentPlayerLevel < 5) {
                    playCurrentGameSoundBased(R.raw.level_won)
                }

                // This needs to reset player current level to false for next level to begin.
                playerWonTheCurrentLevel = false
            }

            calculateOverallPointsScoredEachLevel()
        }

        // Play alphabet tap game sound.
        playCurrentGameSoundBased(R.raw.alphabet_tap)
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

    // Everytime player chooses the alphabet, reduce the attempt by 1.
    // If attempt reaches 0, player has lost.
    private fun minimizeAttempt() {
        if (attemptsLeftToGuess > 0) {
            attemptsLeftToGuess -= 1
            gameOverByNoAttemptsLeft = attemptsLeftToGuess == 0
            _revealGuessingWord.value = gameOverByNoAttemptsLeft
            if (gameOverByNoAttemptsLeft) {
                // Saves the game to history, needs couple of changes yet.
                viewModelScope.launch {
                    playCurrentGameSoundBased(R.raw.game_lost)
                    saveCurrentGameToHistory()
                }
            }
        }
    }

    //var updater by mutableStateOf(GuessHolder(guesses = updatePlayerGuesses))

    // Reset guesses, update new word indices for next level word.
    private fun updateOrResetWordToGuess() {
        _alphabets.value = alphabetsList()
        updatePlayerGuesses.clear()
        for (i in wordToGuess.indices) {
            updatePlayerGuesses.add(' ')
            //GuessHolder(guesses = updatePlayerGuesses)
        }
    }

    // When this function triggered, we will save the existing game progress to the database,
    // irrespective of player wins or loses the game.
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

    // Re-initializes media player everytime this function called with different game sound.
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

    // Stop the game sound once ViewModel destroyed.
    override fun onCleared() {
        mediaPlayer.release()
    }
}