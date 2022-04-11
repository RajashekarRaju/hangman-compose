package com.hangman.hangman.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HistoryAppBar(
    navigateUp: () -> Unit,
    showDeleteIconInAppBar: Boolean,
    deleteAllGameHistoryData: () -> Unit,
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Game History",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { navigateUp() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigate to previous screen",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        actions = {
            if (showDeleteIconInAppBar) {
                IconButton(
                    onClick = { deleteAllGameHistoryData() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete all game history",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    )
}