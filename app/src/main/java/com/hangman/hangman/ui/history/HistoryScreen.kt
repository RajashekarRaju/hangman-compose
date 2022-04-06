package com.hangman.hangman.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hangman.hangman.HangmanApp
import com.hangman.hangman.repository.GameRepository

@Composable
fun HistoryScreen(
    navigateUp: () -> Unit,
    application: HangmanApp,
    repository: GameRepository
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        HistoryScreenContent(
            navigateUp
        )
    }
}

@Composable
fun HistoryScreenContent(
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}
