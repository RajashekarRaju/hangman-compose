package com.hangman.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hangman.hangman.R
import com.hangman.hangman.ui.game.GameInstructionsInfoDialog
import com.hangman.hangman.utils.GameDifficulty
import org.koin.androidx.compose.getViewModel


@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    finishActivity: () -> Unit,
) {
    val viewModel = getViewModel<OnBoardingViewModel>()

    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            Image(
                painter = painterResource(id = R.drawable.bg_dodge),
                contentDescription = "background image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
) {
    val highScore = viewModel.getLatestHighScore()

    val openGameDifficultyDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameDifficultyDialog.value) {
        AdjustGameDifficultyDialog(viewModel, openGameDifficultyDialog)
    }

    val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameInstructionsDialog.value) {
        GameInstructionsInfoDialog(
            viewModel.difficultyValueText,
            openGameInstructionsDialog
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.rope_with_title),
            contentDescription = "Hangman game",
            modifier = Modifier.size(220.dp)
        )

        Text(
            text = "Be Aware. Letters Can Demise You",
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary.copy(0.75f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = buildAnnotatedString {
                append("Highest Score - ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.onSurface,
                        fontSize = 18.sp,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(if (highScore == "null") "0" else highScore)
                }
            },
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.primary.copy(0.75f),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            item {

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.width(160.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary.copy(0.5f)
                    ),
                    onClick = {
                        viewModel.releaseBackgroundMusic()
                        navigateToGameScreen()
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.skull),
                        contentDescription = "Play game",
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    OnBoardingButtonText(buttonName = "Play")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.width(160.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                    onClick = {
                        viewModel.gameDifficultyPreferences.updateGameDifficultyPref(GameDifficulty.EASY)
                        finishActivity()
                    },
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary.copy(0.5f)
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.demon),
                        contentDescription = "Exit game",
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    OnBoardingButtonText(buttonName = "Exit")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.width(160.dp),
                    onClick = { navigateToHistoryScreen() },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary.copy(0.5f)
                    ),
                ) {
                    OnBoardingButtonText(buttonName = "History")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.width(160.dp),
                    onClick = { openGameDifficultyDialog.value = !openGameDifficultyDialog.value },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary.copy(0.5f)
                    )
                ) {
                    OnBoardingButtonText(buttonName = "Difficulty")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    IconButton(
                        onClick = {
                            openGameInstructionsDialog.value = !openGameInstructionsDialog.value
                        },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primary.copy(0.15f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Read instructions",
                            tint = MaterialTheme.colors.primary,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    var volumeIcon = R.drawable.ic_volume_enabled
                    if (!viewModel.isBackgroundMusicPlaying) {
                        volumeIcon = R.drawable.ic_volume_disabled
                    }

                    IconButton(
                        onClick = {
                            if (!viewModel.isBackgroundMusicPlaying) {
                                viewModel.playGameBackgroundMusicOnStart()
                            } else {
                                viewModel.releaseBackgroundMusic()
                            }
                        },
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primary.copy(0.15f),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = volumeIcon),
                            contentDescription = "Game sound on",
                            tint = MaterialTheme.colors.primary,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun OnBoardingButtonText(
    buttonName: String
) {
    Text(
        text = buttonName,
        style = MaterialTheme.typography.button,
        color = MaterialTheme.colors.primary.copy(0.75f),
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
