package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal expect fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier

internal expect fun isCustomSkullCursorSupported(): Boolean

@Composable
internal expect fun PlatformCursorSideEffect(enabled: Boolean)
