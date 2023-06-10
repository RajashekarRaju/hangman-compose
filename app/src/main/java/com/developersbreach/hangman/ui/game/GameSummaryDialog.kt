package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.developersbreach.hangman.R
import com.developersbreach.hangman.utils.ApplyAnimatedVisibility

/**
 * Dialog shown when player wins the game.
 */
@Composable
fun ShowDialogWhenGameWon(
    viewModel: GameViewModel,
    navigateUp: () -> Unit,
) {
    Dialog(
        onDismissRequest = { navigateUp() }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = buildAnnotatedString {

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 24.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(id = R.string.dialog_game_won_title))
                    }

                    append(stringResource(id = R.string.dialog_game_won_points))

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 28.sp
                        )
                    ) {
                        append(viewModel.pointsScoredOverall.toString())
                    }

                    append(stringResource(id = R.string.dialog_game_won_difficulty))

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 28.sp
                        )
                    ) {
                        append(viewModel.gameDifficulty.toString())
                    }
                },
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
            )
        }
    }
}

/**
 * Dialog shown when player loses the game.
 */
@Composable
fun ShowPopupWhenGameLost(
    viewModel: GameViewModel,
    navigateUp: () -> Unit
) {
    Dialog(
        onDismissRequest = { navigateUp() }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colors.surface,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {

            ApplyAnimatedVisibility(
                densityValue = (-400).dp,
                content = {
                    Image(
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary),
                        painter = painterResource(id = R.drawable.rope_empty_title),
                        contentDescription = stringResource(R.string.cd_game_lost_hangman_image),
                        alpha = 0.75f,
                        alignment = Alignment.TopCenter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.TopCenter),
                    )
                }
            )

            Image(
                colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary),
                painter = painterResource(id = R.drawable.evil_hand),
                contentDescription = stringResource(R.string.cd_game_lost_hangman_image),
                modifier = Modifier.align(alignment = Alignment.BottomCenter),
                alpha = 0.25f
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 24.sp,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(stringResource(id = R.string.dialog_game_lost_title))
                    }

                    append(stringResource(id = R.string.dialog_game_lost_word))

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 28.sp,
                        )
                    ) {
                        append(viewModel.wordToGuess)
                    }
                },
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
            )
        }
    }
}