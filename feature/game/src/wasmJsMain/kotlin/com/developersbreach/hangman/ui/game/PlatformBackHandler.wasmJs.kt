package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No platform back action on wasm.
}
