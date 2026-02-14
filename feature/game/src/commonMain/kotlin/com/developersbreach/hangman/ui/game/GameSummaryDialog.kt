package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_dialog_done
import com.developersbreach.hangman.feature.game.generated.resources.game_lost_title
import com.developersbreach.hangman.feature.game.generated.resources.game_lost_word
import com.developersbreach.hangman.feature.game.generated.resources.game_won_difficulty
import com.developersbreach.hangman.feature.game.generated.resources.game_won_points
import com.developersbreach.hangman.feature.game.generated.resources.game_won_title
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ShowDialogWhenGameWon(
    pointsScoredOverall: Int,
    gameDifficulty: GameDifficulty,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TitleLargeText(
                text = stringResource(Res.string.game_won_title),
                color = HangmanTheme.colorScheme.primary,
            )
        },
        text = {
            Column {
                BodyLargeText(
                    text = stringResource(Res.string.game_won_points, pointsScoredOverall),
                    color = HangmanTheme.colorScheme.onSurface,
                )
                BodyLargeText(
                    text = stringResource(
                        Res.string.game_won_difficulty,
                        difficultyLabel(gameDifficulty),
                    ),
                    color = HangmanTheme.colorScheme.onSurface,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                LabelLargeText(
                    text = stringResource(Res.string.game_dialog_done),
                    color = HangmanTheme.colorScheme.primary,
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.surfaceContainer,
    )
}

@Composable
fun ShowPopupWhenGameLost(
    wordToGuess: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TitleLargeText(
                text = stringResource(Res.string.game_lost_title),
                color = HangmanTheme.colorScheme.error,
            )
        },
        text = {
            BodyLargeText(
                text = stringResource(Res.string.game_lost_word, wordToGuess),
                color = HangmanTheme.colorScheme.onSurface,
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                LabelLargeText(
                    text = stringResource(Res.string.game_dialog_done),
                    color = HangmanTheme.colorScheme.primary,
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.surfaceContainer,
    )
}
