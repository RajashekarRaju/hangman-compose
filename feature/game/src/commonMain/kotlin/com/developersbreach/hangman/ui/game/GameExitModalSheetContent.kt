package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_confirm
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_dismiss
import com.developersbreach.hangman.feature.game.generated.resources.game_exit_title
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.HangmanTextActionButton
import com.developersbreach.hangman.ui.components.TitleLargeText
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
        threshold = 0.10f,
    ) {
        TitleLargeText(
            text = stringResource(Res.string.game_exit_title),
            color = HangmanTheme.colorScheme.primary,
        )

        Spacer(Modifier.height(28.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            HangmanTextActionButton(
                text = stringResource(Res.string.game_exit_dismiss),
                color = HangmanTheme.colorScheme.onSurfaceVariant,
                onClick = onDismiss,
            )
            HangmanTextActionButton(
                text = stringResource(Res.string.game_exit_confirm),
                color = HangmanTheme.colorScheme.primary,
                onClick = onConfirmExit,
            )
        }
    }
}