package com.hangman.hangman.ui.onboarding

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hangman.hangman.utils.GameDifficulty
import com.hangman.hangman.utils.GameDifficultyPref


class OnBoardingViewModel(
    application: Application
) : AndroidViewModel(application) {

    var difficultyValueText by mutableStateOf(GameDifficulty.EASY)
    val gameDifficultyPref = GameDifficultyPref(application)

    fun updatePlayerChosenDifficulty(
        sliderPosition: Float
    ) {
        difficultyValueText = when (sliderPosition) {
            1.0f -> GameDifficulty.EASY
            2.0f -> GameDifficulty.MEDIUM
            3.0f -> GameDifficulty.HARD
            else -> GameDifficulty.EASY
        }

        gameDifficultyPref.updateGameDifficultyPref(difficultyValueText)
    }

    companion object {

        fun provideFactory(
            application: Application
        ): ViewModelProvider.AndroidViewModelFactory {
            return object : ViewModelProvider.AndroidViewModelFactory(application) {
                @Suppress("unchecked_cast")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(OnBoardingViewModel::class.java)) {
                        return OnBoardingViewModel(application) as T
                    }
                    throw IllegalArgumentException("Cannot create Instance for OnBoardingViewModel class")
                }
            }
        }
    }
}