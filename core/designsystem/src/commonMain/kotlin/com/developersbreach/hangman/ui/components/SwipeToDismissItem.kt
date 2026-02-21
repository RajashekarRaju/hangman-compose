package com.developersbreach.hangman.ui.components

import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.RowScope

@Composable
fun HangmanSwipeToDismissItem(
    dismissKey: Any,
    onDismissed: () -> Unit,
    backgroundContent: @Composable RowScope.() -> Unit = {},
    enableDismissFromStartToEnd: Boolean = false,
    enableDismissFromEndToStart: Boolean = true,
    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState()
    LaunchedEffect(
        key1 = dismissState.currentValue,
        key2 = dismissKey
    ) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDismissed()
        }
    }
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = enableDismissFromStartToEnd,
        enableDismissFromEndToStart = enableDismissFromEndToStart,
        backgroundContent = backgroundContent,
        content = { content() },
    )
}
