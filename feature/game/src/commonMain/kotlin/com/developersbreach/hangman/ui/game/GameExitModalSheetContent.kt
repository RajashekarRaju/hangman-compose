package com.developersbreach.hangman.ui.game

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_confirm
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_dismiss
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_title
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowExitGameDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TitleMediumText(
                text = stringResource(Res.string.game_exit_title),
                color = HangmanTheme.colorScheme.primary,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmExit) {
                LabelLargeText(
                    text = stringResource(Res.string.game_exit_confirm),
                    color = HangmanTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                LabelLargeText(
                    text = stringResource(Res.string.game_exit_dismiss),
                    color = HangmanTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.surfaceContainer,
    )
}