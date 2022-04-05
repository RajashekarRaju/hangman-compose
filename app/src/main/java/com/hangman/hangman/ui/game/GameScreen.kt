package com.hangman.hangman.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun GameScreen(
    navigateToHistoryScreen: () -> Unit,
    navigateUp: () -> Unit
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        GameScreenContent(
            navigateToHistoryScreen = navigateToHistoryScreen,
            navigateUp = navigateUp
        )
    }
}

@Composable
private fun GameScreenContent(
    navigateToHistoryScreen: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}