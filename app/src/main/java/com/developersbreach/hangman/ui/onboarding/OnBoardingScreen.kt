package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.R
import com.developersbreach.hangman.ui.game.GameInstructionsInfoDialog
import com.developersbreach.hangman.utils.createInfiniteRepeatableRotateAnimation


/**
 * Default screen first visible to the player.
 * This screen has it's own ViewModel [OnBoardingViewModel]
 */
@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
    finishActivity: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Full screen occupying background image.
            Image(
                painter = painterResource(id = R.drawable.game_background),
                contentDescription = stringResource(R.string.cd_image_screen_bg),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.1f
            )

            // Main screen content
            OnBoardingScreenContent(
                navigateToGameScreen = navigateToGameScreen,
                finishActivity = finishActivity,
                navigateToHistoryScreen = navigateToHistoryScreen,
                viewModel = viewModel
            )
        }
    }
}

/**
 * Main content for this screen.
 *
 * @param navigateToGameScreen navigates to the game screen with button click.
 * @param finishActivity triggers activity to finish.
 * @param navigateToHistoryScreen navigates to history screen with button click.
 */
@Composable
private fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        // Displays hanging rope image on top of screen.
        FullScreenGameBackground()

        // Tagline for the game.
        GameTaglineText()

        Spacer(modifier = Modifier.height(12.dp))

        // Shows highest score.
        HighestGameScoreName(viewModel)

        Spacer(modifier = Modifier.height(8.dp))

        // This content needs to be scrolled for small screen devices.
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            item {

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, navigates to game screen.
                PlayGameButton(navigateToGameScreen) {
                    // Stop the background music once game screen opens.
                    viewModel.releaseBackgroundMusic()
                }

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, finishes the activity to close the app.
                ExitGameButton(viewModel) {
                    finishActivity()
                }

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, navigates to history screen.
                GameHistoryButton(navigateToHistoryScreen)

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, open the dialog with game difficulty adjustments.
                GameDifficultyButton(viewModel)

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, open the dialog with game category options.
                GameCategoryButton(viewModel)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    // OnClick icon, opens dialog with game instructions.
                    GameInstructionIconButton(viewModel)

                    Spacer(modifier = Modifier.width(16.dp))

                    // OnClick icon, play/stops background music.
                    BackgroundVolumeIconButton(viewModel)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FullScreenGameBackground() {
    Image(
        painter = painterResource(id = R.drawable.rope_with_title),
        contentDescription = stringResource(R.string.cd_game_banner),
        modifier = Modifier.size(220.dp),
        colorFilter = ColorFilter.tint(color = MaterialTheme.colors.primary)
    )
}

@Composable
private fun GameTaglineText() {
    Text(
        text = stringResource(R.string.game_tagline),
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.primary.copy(0.75f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)
    )
}

@Composable
private fun HighestGameScoreName(
    viewModel: OnBoardingViewModel
) {
    val highScore by viewModel.highestScore.observeAsState(0)

    Text(
        text = buildAnnotatedString {
            append(stringResource(R.string.highest_score_header))
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 18.sp,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                // To prevent text on screen to show 0 instead on 0 when no history is available.
                append(if (highScore.toString() == "null") "0" else highScore.toString())
            }
        },
        textDecoration = TextDecoration.Underline,
        style = MaterialTheme.typography.subtitle1,
        color = MaterialTheme.colors.primary.copy(0.75f),
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun PlayGameButton(
    navigateToGameScreen: () -> Unit,
    releaseBackgroundMusic: () -> Unit
) {
    Button(
        modifier = Modifier.width(160.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primary.copy(0.5f)
        ),
        onClick = {
            releaseBackgroundMusic()
            navigateToGameScreen()
        },
    ) {

        Icon(
            painter = painterResource(id = R.drawable.skull),
            contentDescription = stringResource(R.string.cd_play_game_button),
            modifier = Modifier
                .size(20.dp)
                .rotate(createInfiniteRepeatableRotateAnimation())
        )

        Spacer(modifier = Modifier.width(16.dp))

        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_play))
    }
}

@Composable
private fun ExitGameButton(
    viewModel: OnBoardingViewModel,
    finishActivity: () -> Unit
) {
    Button(
        modifier = Modifier.width(160.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        onClick = {
            // Before closing the app reset teh game difficulty to default difficulty.
            viewModel.resetGameDifficultyPreferences()
            finishActivity()
        },
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primary.copy(0.5f)
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.demon),
            contentDescription = stringResource(R.string.cd_exit_game),
            modifier = Modifier
                .size(28.dp)
                .rotate(
                    createInfiniteRepeatableRotateAnimation(
                        initialValue = 360f,
                        targetValue = 0f
                    )
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_exit))
    }
}

@Composable
private fun GameHistoryButton(
    navigateToHistoryScreen: () -> Unit,
) {
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
        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_history))
    }
}

@Composable
fun GameDifficultyButton(
    viewModel: OnBoardingViewModel,
) {
    // Change state value to open the dialog.
    val openGameDifficultyDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameDifficultyDialog.value) {
        AdjustGameDifficultyDialog(
            viewModel = viewModel,
            openGameDifficultyDialog = openGameDifficultyDialog
        )
    }

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
        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_difficulty))
    }
}

@Composable
fun GameCategoryButton(
    viewModel: OnBoardingViewModel,
) {
    // Change state value to open the dialog.
    val openGameCategoryDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameCategoryDialog.value) {
        ChooseGameCategoryDialog(
            viewModel = viewModel,
            openGameCategoryDialog = openGameCategoryDialog
        )
    }

    Button(
        modifier = Modifier.width(160.dp),
        onClick = { openGameCategoryDialog.value = !openGameCategoryDialog.value },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primary.copy(0.5f)
        )
    ) {
        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_category))
    }
}

@Composable
private fun GameInstructionIconButton(
    viewModel: OnBoardingViewModel,
) {
    // Change state value to open the dialog.
    val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameInstructionsDialog.value) {
        GameInstructionsInfoDialog(
            gameDifficulty = viewModel.gameDifficulty,
            gameCategory = viewModel.gameCategory,
            openGameInstructionsDialog = openGameInstructionsDialog
        )
    }

    IconButton(
        onClick = { openGameInstructionsDialog.value = !openGameInstructionsDialog.value },
        modifier = Modifier.background(
            color = MaterialTheme.colors.primary.copy(0.15f),
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(R.string.cd_open_instructions_dialog),
            tint = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun BackgroundVolumeIconButton(
    viewModel: OnBoardingViewModel
) {
    // Change the icon to match game sound play pause state.
    var volumeIcon = R.drawable.ic_volume_enabled
    if (!viewModel.isBackgroundMusicPlaying) {
        volumeIcon = R.drawable.ic_volume_disabled
    }

    IconButton(
        onClick = {
            // Play or release the game sound.
            if (!viewModel.isBackgroundMusicPlaying) {
                viewModel.playGameBackgroundMusicOnStart()
            } else {
                viewModel.releaseBackgroundMusic()
            }
        },
        modifier = Modifier.background(
            color = MaterialTheme.colors.primary.copy(0.15f),
            shape = CircleShape
        )
    ) {
        Icon(
            painter = painterResource(id = volumeIcon),
            contentDescription = stringResource(R.string.cd_game_sound_play_pause),
            tint = MaterialTheme.colors.primary,
        )
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
