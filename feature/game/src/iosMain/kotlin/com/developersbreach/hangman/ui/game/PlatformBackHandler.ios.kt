package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS navigation back is handled by the native container.
}
