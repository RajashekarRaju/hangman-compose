package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle

internal expect fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier

internal expect fun isCustomCursorSupported(cursorStyle: CursorStyle): Boolean

@Composable
internal expect fun PlatformCursorSideEffect(
    enabled: Boolean,
    cursorStyle: CursorStyle,
)
