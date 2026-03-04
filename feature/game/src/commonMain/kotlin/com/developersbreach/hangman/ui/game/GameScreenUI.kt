package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.HangmanScaffold
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun GameUiState.GameScreenUI(
    onEvent: (GameEvent) -> Unit,
) {
    HangmanScaffold(
        topBar = {
            AnimatedEnter(offsetY = 16.dp) {
                GameNavigationActionIcons(
                    uiState = this,
                    onEvent = onEvent,
                )
            }
        },
        bottomBar = {
            AnimatedEnter(offsetY = 12.dp) {
                HintBottomTray(
                    uiState = this@GameScreenUI,
                    onEvent = onEvent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.background,
        contentColor = HangmanTheme.colorScheme.onBackground,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            AnimatedEnter {
                PlatformGameMainLayout(
                    uiState = this@GameScreenUI,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}