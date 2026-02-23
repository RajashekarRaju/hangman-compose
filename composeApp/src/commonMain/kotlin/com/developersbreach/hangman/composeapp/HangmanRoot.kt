package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.koin.core.context.startKoin

private var koinInitialized = false

fun initKoinIfNeeded() {
    if (!koinInitialized) {
        startKoin {
            modules(initKoinComponents())
        }
        koinInitialized = true
    }
}

@Composable
fun HangmanRoot(closeApplication: () -> Unit) {
    HangmanTheme {
        AppNavigation(closeApplication = closeApplication)
    }
}
