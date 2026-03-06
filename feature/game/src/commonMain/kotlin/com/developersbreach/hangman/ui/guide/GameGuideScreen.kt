package com.developersbreach.hangman.ui.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun GameGuideScreen(
    navigateUp: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HangmanTheme.colorScheme.background),
    ) {
        GameGuideContent(
            onClose = navigateUp,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
internal fun GameGuideOverlay(
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HangmanTheme.colorScheme.background.copy(alpha = 0.96f)),
    ) {
        GameGuideContent(
            onClose = onClose,
            modifier = Modifier.fillMaxSize(),
        )
    }
}