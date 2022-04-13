package com.hangman.hangman.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hangman.hangman.R

/**
 * TopAppBar for [HistoryScreen].
 * @param navigateUp navigates to previous screen.
 * @param showDeleteIconInAppBar if history game list is empty then this value is true.
 * @param deleteAllGameHistoryData deletes all the game history from database.
 */
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
                text = stringResource(R.string.game_history_app_bar_title),
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
                    contentDescription = stringResource(R.string.cd_navigate_up),
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
                        contentDescription = stringResource(R.string.cd_delete_icon),
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    )
}