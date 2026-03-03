package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle

@Composable
internal actual fun ThemedSkullCursorContainer(
    enabled: Boolean,
    cursorStyle: CursorStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    content()
}
