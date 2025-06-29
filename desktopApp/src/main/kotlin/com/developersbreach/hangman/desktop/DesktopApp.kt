package com.developersbreach.hangman.desktop

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.developersbreach.hangman.repository.DesktopHistoryStorage
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.PlatformAudioPlayer
import com.developersbreach.hangman.utils.PlatformSettings
import com.developersbreach.hangman.viewmodel.OnBoardingViewModel

fun main() = application {
    val repository = GameRepository(DesktopHistoryStorage())
    val settings = PlatformSettings()
    val viewModel = OnBoardingViewModel(repository, GamePref(settings), PlatformAudioPlayer())
    Window(onCloseRequest = ::exitApplication, title = "Hangman Desktop") {
        Text("Highest score: ${viewModel.highestScore.value ?: 0}")
    }
}
