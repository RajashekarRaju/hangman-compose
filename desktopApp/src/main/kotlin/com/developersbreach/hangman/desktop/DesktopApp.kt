package com.developersbreach.hangman.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.developersbreach.hangman.desktop.game.DesktopGameScreen
import com.developersbreach.hangman.desktop.theme.DesktopHangmanTheme
import com.developersbreach.hangman.repository.DesktopHistoryStorage
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.PlatformAudioPlayer
import com.developersbreach.hangman.utils.PlatformSettings
import com.developersbreach.hangman.viewmodel.GameViewModel

fun main() = application {
    val repository = GameRepository(DesktopHistoryStorage())
    val settings = PlatformSettings()
    val gameViewModel = GameViewModel(repository, GamePref(settings), PlatformAudioPlayer())
    Window(onCloseRequest = ::exitApplication, title = "Hangman Desktop") {
        DesktopHangmanTheme {
            DesktopGameScreen(gameViewModel)
        }
    }
}
