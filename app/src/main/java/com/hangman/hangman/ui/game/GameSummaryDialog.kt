package com.hangman.hangman.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.hangman.hangman.R
import com.hangman.hangman.utils.ApplyAnimatedVisibility

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
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
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
                        append("You Have Won !\n\n")
                    }
                    append("Points you have scored\n\n")
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primary,
                            fontSize = 28.sp
                        )
                    ) {
                        append(viewModel.pointsScoredOverall.toString())
                    }
                    append("\n\nDifficulty Mode\n")
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
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


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
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
        ) {

            ApplyAnimatedVisibility(
                densityValue = (-400).dp,
                content = {
                    Image(
                        painter = painterResource(id = R.drawable.rope_empty_title),
                        contentDescription = "Hangman rope image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(alignment = Alignment.TopCenter),
                        alpha = 0.75f,
                        alignment = Alignment.TopCenter
                    )
                }
            )

            Image(
                painter = painterResource(id = R.drawable.evil_hand),
                contentDescription = "",
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
                        append("\n\n\nYou Lost\n\n")
                    }
                    append("The word you didn't guess was\n\n")
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
                modifier = Modifier
                    .padding(horizontal = 40.dp, vertical = 60.dp)
                    .fillMaxWidth()
                    .align(alignment = Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}