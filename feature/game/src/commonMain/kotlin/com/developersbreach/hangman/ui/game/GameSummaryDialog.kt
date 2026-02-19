package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.evil_hand
import com.developersbreach.hangman.feature.game.generated.resources.game_dialog_done
import com.developersbreach.hangman.feature.game.generated.resources.game_lost_title
import com.developersbreach.hangman.feature.game.generated.resources.game_lost_word
import com.developersbreach.hangman.feature.game.generated.resources.game_won_difficulty
import com.developersbreach.hangman.feature.game.generated.resources.game_won_points
import com.developersbreach.hangman.feature.game.generated.resources.game_won_title
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedEnter(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    offsetY = 80.dp
                ) {
                    Image(
                        painter = painterResource(Res.drawable.evil_hand),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(color = HangmanTheme.colorScheme.primary),
                        alpha = 0.25f,
                    )
                }
                BodyLargeText(
                    text = stringResource(Res.string.game_lost_word, wordToGuess),
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
