package com.developersbreach.hangman.ui.onboarding

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.hangman.R
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty
import com.developersbreach.hangman.utils.GamePref
import kotlinx.coroutines.launch

/**
 * ViewModel for screen [OnBoardingScreen].
 * Initialized with koin.
 */
class OnBoardingViewModel(
    private val application: Application,
    repository: GameRepository
) : ViewModel() {

    // Keeps track of the highest score from the game history database.
    val highestScore: LiveData<Int?> = repository.getHighestScore()

    // Updated the slider position to current difficulty.
    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)

    // Updates the radio button from saved player preferences for game category.
    var gameCategory by mutableStateOf(GameCategory.COUNTRIES)

    // Get shared preferences for value game category.
    val gamePreferences = GamePref(application)

    // Tracks media player current play/pause/release state.
    var isBackgroundMusicPlaying by mutableStateOf(false)
    private lateinit var mediaPlayer: MediaPlayer

    init {
        gameDifficulty = gamePreferences.getGameDifficultyPref()
        // Start game sound on screen launch.
        playGameBackgroundMusicOnStart()
    }

    // Initialize the media player and manage the play/release state.
    // Updates the isBackgroundMusicPlaying values for screen elements to change.
    fun playGameBackgroundMusicOnStart() {
        viewModelScope.launch {
            mediaPlayer =
                MediaPlayer.create(application.applicationContext, R.raw.game_background_music)
            isBackgroundMusicPlaying = if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                true
            } else {
                mediaPlayer.release()
                false
            }
        }
    }

    // Releases media player and stop current playing sound.
    fun releaseBackgroundMusic() {
        mediaPlayer.release()
        isBackgroundMusicPlaying = false
    }

    // Once user makes changes to slider position this function will be triggered.
    fun updatePlayerChosenDifficulty(
        sliderPosition: Float
    ) {
        gameDifficulty = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }

        // Update the value in preferences with latest player chosen game difficulty mode.
        gamePreferences.updateGameDifficultyPref(gameDifficulty)
    }

    // Once user makes changes to radio button this function will be triggered.
    fun updatePlayerChosenCategory(
        category: Int
    ) {
        gameCategory = when (category) {
            0 -> GameCategory.COUNTRIES
            1 -> GameCategory.LANGUAGES
            2 -> GameCategory.COMPANIES
            else -> GameCategory.COUNTRIES
        }

        // Update the value in preferences with latest player chosen game category mode.
        gamePreferences.updateGameCategoryPref(gameCategory.ordinal)
    }
}