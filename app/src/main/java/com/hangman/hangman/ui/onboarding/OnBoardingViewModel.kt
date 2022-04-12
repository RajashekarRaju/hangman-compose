package com.hangman.hangman.ui.onboarding

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.R
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.utils.GameDifficulty
import com.hangman.hangman.utils.GameDifficultyPref
import kotlinx.coroutines.launch

/**
 * ViewModel for screen [OnBoardingScreen].
 * Initialized with koin.
 */
class OnBoardingViewModel(
    private val application: Application,
    private val repository: GameRepository
) : ViewModel() {

    // Keeps track of highest score from the game history.
    private var highestScore by mutableStateOf("0")

    // From slider position updates the current difficulty text value.
    var difficultyValueText by mutableStateOf(GameDifficulty.EASY)

    // Updates the preferences with game difficulty slider position.
    val gameDifficultyPreferences = GameDifficultyPref(application)

    // Tracks media player current play/pause/release state.
    var isBackgroundMusicPlaying by mutableStateOf(false)
    private lateinit var mediaPlayer: MediaPlayer

    init {
        // Fetch latest game score.
        getLatestHighScore()
        // Start game sound on screen launch.
        playGameBackgroundMusicOnStart()
    }

    // Launches a coroutine and fetches the highest score from the game history database.
    fun getLatestHighScore(): String {
        viewModelScope.launch {
            val gameHistoryList = repository.getCompleteGameHistory()
            highestScore = gameHistoryList.maxByOrNull {
                it.gameScore
            }?.gameScore.toString()
        }

        // Updates the previous highest score saved in highestScore with new one.
        return highestScore
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
        difficultyValueText = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }

        // Update the value in preferences with latest player chosen game difficulty mode.
        gameDifficultyPreferences.updateGameDifficultyPref(difficultyValueText)
    }
}