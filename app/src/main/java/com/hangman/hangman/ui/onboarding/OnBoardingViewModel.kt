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
        getLatestHighScore()
        playGameBackgroundMusicOnStart()
    }

    fun getLatestHighScore(): String {
        viewModelScope.launch {
            val gameHistoryList = repository.getCompleteGameHistory()
            highestScore = gameHistoryList.maxByOrNull {
                it.gameScore
            }?.gameScore.toString()
        }

        return highestScore
    }

    fun playGameBackgroundMusicOnStart() {
        viewModelScope.launch {
            mediaPlayer =
                MediaPlayer.create(application.applicationContext, R.raw.game_background_music)
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                isBackgroundMusicPlaying = true
            }
        }
    }

    fun releaseBackgroundMusic() {
        mediaPlayer.release()
        isBackgroundMusicPlaying = false
    }

    fun updatePlayerChosenDifficulty(
        sliderPosition: Float
    ) {
        difficultyValueText = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }

        gameDifficultyPreferences.updateGameDifficultyPref(difficultyValueText)
    }
}