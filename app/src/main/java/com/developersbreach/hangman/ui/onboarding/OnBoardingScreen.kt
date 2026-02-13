package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.developersbreach.hangman.ui.components.GameInstructionsInfoDialog
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
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
    finishActivity: () -> Unit
) {
    val highScore by viewModel.highestScore.observeAsState(0)
    OnBoardingScreenUI(
        navigateToGameScreen = navigateToGameScreen,
        navigateToHistoryScreen = navigateToHistoryScreen,
        finishActivity = finishActivity,
        highScore = highScore ?: 0,
        releaseBackgroundMusic = { viewModel.releaseBackgroundMusic() },
        gameDifficulty = viewModel.gameDifficulty,
        updatePlayerChosenCategory = { viewModel.updatePlayerChosenDifficulty(it) },
        gameCategory = viewModel.gameCategory,
        isBackgroundMusicPlaying = viewModel.isBackgroundMusicPlaying,
        onClickPlayGameBackgroundMusicOnStart = { viewModel.playGameBackgroundMusicOnStart() },
        onClickReleaseBackgroundMusic = { viewModel.releaseBackgroundMusic() }

    )
}

/**
 * Main content for this screen.
 *
 * @param navigateToGameScreen navigates to the game screen with button click.
 * @param finishActivity triggers activity to finish.
 * @param navigateToHistoryScreen navigates to history screen with button click.
 */
@Composable
fun OnBoardingScreenContent(
    navigateToGameScreen: () -> Unit,
    finishActivity: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    highScore: Int,
    releaseBackgroundMusic: () -> Unit,
    gameDifficulty: GameDifficulty,
    updatePlayerChosenCategory: (Float) -> Unit,
    gameCategory: GameCategory,
    isBackgroundMusicPlaying: Boolean,
    onClickPlayGameBackgroundMusicOnStart: () -> Unit,
    onClickReleaseBackgroundMusic: () -> Unit,
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
        HighestGameScoreName(highScore = highScore)

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
                    releaseBackgroundMusic()
                }

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, finishes the activity to close the app.
                ExitGameButton { finishActivity() }

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, navigates to history screen.
                GameHistoryButton(navigateToHistoryScreen)

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, open the dialog with game difficulty adjustments.
                GameDifficultyButton(
                    gameDifficulty = gameDifficulty,
                    updatePlayerChosenCategory = updatePlayerChosenCategory
                )

                Spacer(modifier = Modifier.height(8.dp))

                // OnClick button, open the dialog with game category options.
                GameCategoryButton(
                    gameCategory = gameCategory,
                    updatePlayerChosenCategory = {
                        updatePlayerChosenCategory(it.toFloat())
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    // OnClick icon, opens dialog with game instructions.
                    GameInstructionIconButton(
                        gameDifficulty = gameDifficulty,
                        gameCategory = gameCategory
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // OnClick icon, play/stops background music.
                    BackgroundVolumeIconButton(
                        isBackgroundMusicPlaying = isBackgroundMusicPlaying,
                        onClickPlayGameBackgroundMusicOnStart = onClickPlayGameBackgroundMusicOnStart,
                        onClickReleaseBackgroundMusic = onClickReleaseBackgroundMusic
                    )
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
    highScore: Int
) {
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
        }
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
    finishActivity: () -> Unit
) {
    Button(
        modifier = Modifier.width(160.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        onClick = { finishActivity() },
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
    navigateToHistoryScreen: () -> Unit
) {
    Button(
        modifier = Modifier.width(160.dp),
        onClick = { navigateToHistoryScreen() },
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colors.primary.copy(0.5f)
        )
    ) {
        OnBoardingButtonText(buttonName = stringResource(R.string.button_title_history))
    }
}

@Composable
fun GameDifficultyButton(
    gameDifficulty: GameDifficulty,
    updatePlayerChosenCategory: (Float) -> Unit
) {
    // Change state value to open the dialog.
    val openGameDifficultyDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameDifficultyDialog.value) {
        AdjustGameDifficultyDialog(
            openGameDifficultyDialog = openGameDifficultyDialog,
            gameDifficulty = gameDifficulty,
            updatePlayerChosenDifficulty = updatePlayerChosenCategory
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
    updatePlayerChosenCategory: (Int) -> Unit,
    gameCategory: GameCategory
) {
    // Change state value to open the dialog.
    val openGameCategoryDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameCategoryDialog.value) {
        ChooseGameCategoryDialog(
            openGameCategoryDialog = openGameCategoryDialog,
            updatePlayerChosenCategory = updatePlayerChosenCategory,
            gameCategory = gameCategory
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
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory
) {
    // Change state value to open the dialog.
    val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameInstructionsDialog.value) {
        GameInstructionsInfoDialog(
            gameDifficulty = gameDifficulty,
            gameCategory = gameCategory,
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
            tint = MaterialTheme.colors.primary
        )
    }
}

@Composable
private fun BackgroundVolumeIconButton(
    isBackgroundMusicPlaying: Boolean,
    onClickPlayGameBackgroundMusicOnStart: () -> Unit,
    onClickReleaseBackgroundMusic: () -> Unit,
) {
    // Change the icon to match game sound play pause state.
    var volumeIcon = R.drawable.ic_volume_enabled
    if (!isBackgroundMusicPlaying) {
        volumeIcon = R.drawable.ic_volume_disabled
    }

    IconButton(
        onClick = {
            // Play or release the game sound.
            if (!isBackgroundMusicPlaying) {
                onClickPlayGameBackgroundMusicOnStart()
            } else {
                onClickReleaseBackgroundMusic()
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
            tint = MaterialTheme.colors.primary
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