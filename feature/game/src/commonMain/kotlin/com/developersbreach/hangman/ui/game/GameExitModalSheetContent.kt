package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_confirm
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_dismiss
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_title
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.HangmanDialogFooterButton
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowExitGameDialog(
    onConfirmExit: () -> Unit,
    onDismiss: () -> Unit,
) {
    HangmanDialog(
        onDismissRequest = onDismiss,
        seed = 963,
        headerTitle = stringResource(Res.string.game_exit_title),
        threshold = 0.10f,
        footerButtons = listOf(
            HangmanDialogFooterButton(
                text = stringResource(Res.string.game_exit_dismiss),
                onClick = onDismiss,
                color = HangmanTheme.colorScheme.onSurfaceVariant,
            ),
            HangmanDialogFooterButton(
                text = stringResource(Res.string.game_exit_confirm),
                onClick = onConfirmExit,
                color = HangmanTheme.colorScheme.primary,
            ),
        ),
    )
}
