package com.hangman.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    finishActivity: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        OnBoardingScreenContent(
            navigateToGameScreen = navigateToGameScreen,
            finishActivity = finishActivity,
            navigateToHistoryScreen = navigateToHistoryScreen
        )
    }
}

@Composable
private fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (
            gameHeadlineText, gameTaglineText, playGameButton, exitGameButton, gameHistoryText
        ) = createRefs()

        Text(
            text = "Hangman",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(gameHeadlineText) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 28.dp)
            }
        )

        Text(
            text = "Be Aware, Letters Can Demise You",
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary.copy(0.75f),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .constrainAs(gameTaglineText) {
                    centerHorizontallyTo(parent)
                    top.linkTo(gameHeadlineText.bottom, 20.dp)
                }
        )

        Text(
            text = "Score History",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary.copy(0.75f),
            modifier = Modifier
                .clickable { navigateToHistoryScreen() }
                .constrainAs(gameHistoryText) {
                    centerHorizontallyTo(parent)
                    top.linkTo(gameTaglineText.bottom, 28.dp)
                }
        )

        Button(
            onClick = { navigateToGameScreen() },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(0.5f)
            ),
            modifier = Modifier
                .width(120.dp)
                .constrainAs(playGameButton) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(
                text = "PLAY",
                letterSpacing = 4.sp,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Button(
            onClick = { finishActivity() },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(0.5f)
            ),
            modifier = Modifier
                .width(120.dp)
                .constrainAs(exitGameButton) {
                    centerHorizontallyTo(parent)
                    top.linkTo(playGameButton.bottom, 8.dp)
                }
        ) {
            Text(
                text = "EXIT",
                letterSpacing = 4.sp,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
