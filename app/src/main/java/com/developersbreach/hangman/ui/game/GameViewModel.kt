package com.developersbreach.hangman.ui.game

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.R
import com.developersbreach.hangman.modal.Alphabets
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameCategoryPref
import com.developersbreach.hangman.utils.GameDifficulty
import com.developersbreach.hangman.utils.GameDifficultyPref
import com.developersbreach.hangman.utils.Words
import com.developersbreach.hangman.utils.alphabetsList
import com.developersbreach.hangman.utils.getDateAndTime
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for screen [GameScreen].
 * Instance created with koin..
 */
class GameViewModel(
    private val application: Application,
    private val repository: GameRepository
) : ViewModel() {

    /**
     * Will update the UI state for all player guessing letters.
     * This value always has latest guesses made by player to guess correct letters in word.
     * Helps us determine whether game should be finished or not by reading values in indices.
     * This value won't be used directly to update any composables in [GameScreen].
     *
     * If each value in indices is empty string after player runs out of attempts to guess, at that
     * point player lost the game.
     * To update the UI state player won, we will pass boolean to [playerWonTheCurrentLevel] true.
     * And once for all 5 levels the value is true we will update [gameOverByWinning].
     *
     * If each value in indices is a valid string while player actively keeps guessing, at that
     * point player won that level.
     * To update the UI state player won, we will pass boolean to [gameOverByNoAttemptsLeft].
     */
    val updatePlayerGuesses = mutableStateListOf<String>()

    // List of A-Z alphabets, will let the player access alphabets in any order they want.
    private var _alphabetsList = MutableLiveData<List<Alphabets>>()
    val alphabetsList: LiveData<List<Alphabets>>
        get() = _alphabetsList

    // Determine whether player won or lost the game
    private var playerWonTheCurrentLevel by mutableStateOf(false)

    // To prevent player keep playing the current game level.
    private var gameOverByNoAttemptsLeft by mutableStateOf(false)

    // Player has completed all 5 levels and won the game.
    var gameOverByWinning by mutableStateOf(false)

    // Contains the randomly generated guessing word.
    // Random word will be assigned replacing empty string inside ViewModel init block.
    var wordToGuess: String by mutableStateOf("")

    // Keeps track of attempts left to find out whether or not to finish the game.
    var attemptsLeftToGuess: Int by mutableIntStateOf(8)

    // Reveal the word if player lost the game at any level.
    private var _revealGuessingWord = MutableLiveData(gameOverByNoAttemptsLeft)
    val revealGuessingWord: LiveData<Boolean>
        get() = _revealGuessingWord

    // Number of points depend on length of the string for guessed word.
    private var pointsScoredPerWord: Int by mutableIntStateOf(0)

    // Keeps track of all points scored in each level.
    var pointsScoredOverall: Int by mutableIntStateOf(0)

    // Starting level with 1, last level is 5
    var currentPlayerLevel: Int by mutableIntStateOf(0)

    // Get shared preferences for value game difficulty.
    private val gameDifficultyPreferences = GameDifficultyPref(application)

    // Get shared preferences for value game category.
    private val gameCategoryPreferences = GameCategoryPref(application)

    // Set default state game difficulty value to easy mode.
    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)

    // Set default state game category value to countries.
    var gameCategory: GameCategory by mutableStateOf(GameCategory.COUNTRIES)

    // Contains 5 words in a list for current game, 1 for each level.
    private var guessingWordsForCurrentGame: List<Words> by mutableStateOf(listOf())

    // 5 is the last level player needs to reach. At that point do not increment level.
    val maxLevelReached: Int = 5

    // Keeps track of player scored from each level.
    private val pointsScoredInEachLevel = mutableListOf(0, 0, 0, 0, 0)

    // Initialize media player for the game sound.
    private lateinit var mediaPlayer: MediaPlayer

    init {
        viewModelScope.launch {
            // Get player saved game difficulty level from preferences.
            gameDifficulty = gameDifficultyPreferences.getGameDifficultyPref()
            // Get player saved game category from preferences.
            gameCategory = gameCategoryPreferences.getGameCategoryPref()

            // Based on category and difficulty, get 5 unique random words from database.
            guessingWordsForCurrentGame =
                repository.getRandomGuessingWord(gameDifficulty, gameCategory)
            // From list of 5 words, starting from it's first index position sequentially return
            // a new word to guess for matching level.
            // If level is one -> first word will be returned, till level 5.
            wordToGuess = guessingWordsForCurrentGame[currentPlayerLevel].wordName
            // Reset guesses, update new word indices for current/next level word.
            updateOrResetWordToGuess()
        }

        playCurrentGameSoundBased(R.raw.level_won)
    }

    // Called everytime when player chosen any letter from list of alphabets.
    fun checkIfLetterMatches(
        alphabet: Alphabets
    ) {
        viewModelScope.launch {
            if (gameOverByNoAttemptsLeft) {
                return@launch
            }

            // Make sure to compare valid strings/chars by keeping it same letter case.
            val currentAlphabet: String = alphabet.alphabet.lowercase()
            val currentGuessingWord: String = wordToGuess.lowercase()

            // Update to mark the guessed alphabet
            _alphabetsList.value = _alphabetsList.value?.map {
                when (it.alphabetId) {
                    alphabet.alphabetId -> it.copy(isAlphabetGuessed = true)
                    else -> it
                }
            }

            if (currentGuessingWord.contains(currentAlphabet)) {
                // Since letter was a match, loop into indices range.
                for (notI in currentGuessingWord.indices) {
                    // From the matched word, find at which position alphabet match took place.
                    if (currentGuessingWord[notI].toString() == currentAlphabet) {
                        // For matched position, pass that alphabet to the position to reflect in UI.
                        updatePlayerGuesses[notI] = currentAlphabet
                    }
                }

                // Reaching at this point, word has been guessed correctly.
                if (!updatePlayerGuesses.contains(" ")) {
                    // When none of the characters from word to guess contains empty character,
                    // player has won the current level, but not the whole game.
                    playerWonTheCurrentLevel = true
                    gameOverByNoAttemptsLeft = false
                    _revealGuessingWord.value = gameOverByNoAttemptsLeft
                }
            } else {
                // When match wasn't successful, this will be executed to deduct attempts.
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
                // Game isn't over, but level is. For clarity update this value to false.
                gameOverByNoAttemptsLeft = false

                // Reset guesses, update new word indices for next level word.
                kotlin.run {
                    // Delaying because, in UI progress animation takes 500 millis exactly.
                    // Later delay will be removed by adding a callback which triggers when
                    // animation is completed.
                    delay(500)
                    // Reset alphabets, guessing word indices.
                    updateOrResetWordToGuess()
                }

                // Only when player wins the last level change the gameOverByWinning to true.
                if (playerWonTheCurrentLevel && currentPlayerLevel == 5) {
                    gameOverByWinning = true
                    playCurrentGameSoundBased(R.raw.game_won)
                    viewModelScope.launch {
                        // Delaying half-a-second to save the game to database history.
                        // This time allows us to count the last level points scored.
                        delay(500)
                        saveCurrentGameToHistory()
                    }
                }

                // Prevents media player from playing game won sound, instead of level won sound.
                if (playerWonTheCurrentLevel && currentPlayerLevel < 5) {
                    playCurrentGameSoundBased(R.raw.level_won)
                }

                // Reset player current level to false for next level to begin.
                playerWonTheCurrentLevel = false
            }

            // Keeps track of all level scores everytime player wins a level.
            calculateOverallPointsScoredEachLevel()
        }

        // Play alphabet tap game sound.
        playCurrentGameSoundBased(R.raw.alphabet_tap)
    }

    // Called everytime a level has been completed to update overall score.
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
            // When player lost, change this value to true for dialog to appear in UI.
            _revealGuessingWord.value = gameOverByNoAttemptsLeft
            if (gameOverByNoAttemptsLeft) {
                // Saves the game to history.
                viewModelScope.launch {
                    playCurrentGameSoundBased(R.raw.game_lost)
                    saveCurrentGameToHistory()
                }
            }
        }
    }

    // Reset guesses, update new word indices for next level word.
    private fun updateOrResetWordToGuess() {
        // Once data has been reset in alphabets list, a property isAlphabetGuessed in list of
        // every objects value needs to reset to false, so that player can choose same alphabets
        // in next level.
        _alphabetsList.value = when (_alphabetsList.value) {
            null -> alphabetsList()
            else -> _alphabetsList.value?.map {
                it.copy(isAlphabetGuessed = false)
            }
        }

        // Clears the previously guessed word for new one to take place with empty string.
        updatePlayerGuesses.clear()
        for (i in wordToGuess.indices) {
            updatePlayerGuesses.add(" ")
        }
        // This allows you to cheat.
        Log.e("GameViewModel", "Word to guess is $wordToGuess")
    }

    // When this function triggered, we will save the existing game progress to the database,
    // irrespective of player wins or loses the game.
    // But we will not save the progress if player chooses to exit the game in the middle.
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