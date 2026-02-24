package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal actual fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier = this

internal actual fun isCustomSkullCursorSupported(): Boolean = false

@Composable
internal actual fun PlatformCursorSideEffect(enabled: Boolean) = Unit
