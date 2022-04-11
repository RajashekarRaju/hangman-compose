package com.hangman.hangman.ui.onboarding

import android.app.Application
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hangman.hangman.R
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.utils.GameDifficulty
import com.hangman.hangman.utils.GameDifficultyPref
import kotlinx.coroutines.launch


class OnBoardingViewModel(
    application: Application,
    private val repository: GameRepository
) : AndroidViewModel(application) {

    var difficultyValueText by mutableStateOf(GameDifficulty.EASY)
    val gameDifficultyPreferences = GameDifficultyPref(application)
    var isBackgroundMusicPlaying by mutableStateOf(false)
    var highestScore: Int? by mutableStateOf(0)

    private lateinit var mediaPlayer: MediaPlayer

    init {
        viewModelScope.launch {
            val gameHistoryList = repository.getCompleteGameHistory()
            highestScore = gameHistoryList.maxByOrNull {
                it.gameScore
            }?.gameScore
        }

        playGameBackgroundMusicOnStart()
    }

    fun playGameBackgroundMusicOnStart() {
        viewModelScope.launch {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.game_background_music)
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

    companion object {

        fun provideFactory(
            application: Application,
            repository: GameRepository
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(OnBoardingViewModel::class.java)) {
                        return OnBoardingViewModel(application, repository) as T
                    }
                    throw IllegalArgumentException("Cannot create Instance for OnBoardingViewModel class")
                }
            }
        }
    }
}