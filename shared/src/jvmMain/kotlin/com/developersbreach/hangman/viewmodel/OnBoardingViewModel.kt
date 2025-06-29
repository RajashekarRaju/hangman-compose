package com.developersbreach.hangman.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.PlatformAudioPlayer
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    repository: GameRepository,
    private val gamePreferences: GamePref,
    private val audioPlayer: PlatformAudioPlayer
) : BaseViewModel() {

    val highestScore: Flow<Int?> = repository.getHighestScore()

    var gameDifficulty: GameDifficulty by mutableStateOf(GameDifficulty.EASY)
    var gameCategory by mutableStateOf(GameCategory.COUNTRIES)

    var isBackgroundMusicPlaying by mutableStateOf(false)

    init {
        gameDifficulty = gamePreferences.getGameDifficultyPref()
        playGameBackgroundMusicOnStart()
    }

    fun playGameBackgroundMusicOnStart() {
        viewModelScope.launch {
            audioPlayer.play("game_background_music.mp3")
            isBackgroundMusicPlaying = audioPlayer.isPlaying
        }
    }

    fun releaseBackgroundMusic() {
        audioPlayer.release()
        isBackgroundMusicPlaying = false
    }

    fun updatePlayerChosenDifficulty(sliderPosition: Float) {
        gameDifficulty = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }
        gamePreferences.updateGameDifficultyPref(gameDifficulty)
    }

    fun updatePlayerChosenCategory(category: Int) {
        gameCategory = when (category) {
            0 -> GameCategory.COUNTRIES
            1 -> GameCategory.LANGUAGES
            2 -> GameCategory.COMPANIES
            else -> GameCategory.COUNTRIES
        }
        gamePreferences.updateGameCategoryPref(gameCategory.ordinal)
    }
}
