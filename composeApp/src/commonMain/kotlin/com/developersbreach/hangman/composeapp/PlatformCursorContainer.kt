package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle

@Composable
internal expect fun ThemedSkullCursorContainer(
    enabled: Boolean,
    cursorStyle: CursorStyle,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
)
