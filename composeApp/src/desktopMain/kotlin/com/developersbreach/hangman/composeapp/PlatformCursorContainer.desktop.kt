package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal actual fun ThemedSkullCursorContainer(
    enabled: Boolean,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    PlatformCursorSideEffect(enabled = enabled)
    content()
}
