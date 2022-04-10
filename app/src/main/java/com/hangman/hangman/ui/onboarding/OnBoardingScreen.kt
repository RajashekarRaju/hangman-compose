package com.hangman.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hangman.hangman.HangmanApp
import com.hangman.hangman.R
import com.hangman.hangman.utils.GameDifficulty


@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    application: HangmanApp,
    finishActivity: () -> Unit,
) {
    val viewModel = viewModel(
        factory = OnBoardingViewModel.provideFactory(application),
        modelClass = OnBoardingViewModel::class.java
    )

    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.bg_dodge),
                contentDescription = "",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 1.0f
            )

            OnBoardingScreenContent(
                navigateToGameScreen = navigateToGameScreen,
                finishActivity = finishActivity,
                navigateToHistoryScreen = navigateToHistoryScreen,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        val (
            gameHeadlineText, gameTaglineText, playGameButton, exitGameButton, gameHistoryText,
            difficultyHeaderText, difficultySlider, playerChosenDifficultyText
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
            onClick = {
                viewModel.gameDifficultyPref.updateGameDifficultyPref(GameDifficulty.EASY)
                finishActivity()
            },
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

        Text(
            text = "Difficulty",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary.copy(0.75f),
            modifier = Modifier.constrainAs(difficultyHeaderText) {
                centerHorizontallyTo(parent)
                top.linkTo(exitGameButton.bottom, 28.dp)
            }
        )

        var sliderDifficultyPosition by rememberSaveable { mutableStateOf(1.0f) }

        Slider(
            value = sliderDifficultyPosition,
            onValueChange = { sliderDifficultyPosition = it },
            valueRange = 1.0f..3.0f,
            steps = 1,
            onValueChangeFinished = {
                viewModel.updatePlayerChosenDifficulty(sliderDifficultyPosition)
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colors.primary,
                activeTrackColor = MaterialTheme.colors.primary
            ),
            modifier = Modifier
                .padding(horizontal = 60.dp)
                .fillMaxWidth()
                .constrainAs(difficultySlider) {
                    centerHorizontallyTo(parent)
                    top.linkTo(difficultyHeaderText.bottom, 20.dp)
                }
        )

        Text(
            text = viewModel.difficultyValueText.name,
            style = MaterialTheme.typography.h4,
            color = Color.White,
            modifier = Modifier.constrainAs(playerChosenDifficultyText) {
                centerHorizontallyTo(parent)
                top.linkTo(difficultySlider.bottom, 20.dp)
            }
        )
    }
}
