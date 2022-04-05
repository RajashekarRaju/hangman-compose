package com.hangman.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        OnBoardingScreenContent(
            navigateToGameScreen = navigateToGameScreen,
            finishActivity = finishActivity
        )
    }
}

@Composable
private fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (gameHeadlineText, gameStartButton, gameExitButton) = createRefs()

        Text(
            text = "Hangman",
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(gameHeadlineText) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 28.dp)
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
                .constrainAs(gameStartButton) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Text(
                text = "START",
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
                .constrainAs(gameExitButton) {
                    centerHorizontallyTo(parent)
                    top.linkTo(gameStartButton.bottom, 8.dp)
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
