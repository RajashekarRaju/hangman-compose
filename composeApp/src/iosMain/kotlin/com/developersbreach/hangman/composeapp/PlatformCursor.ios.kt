package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle

internal actual fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier = this

internal actual fun isCustomCursorSupported(cursorStyle: CursorStyle): Boolean = false

@Composable
internal actual fun PlatformCursorSideEffect(
    enabled: Boolean,
    cursorStyle: CursorStyle,
) = Unit
