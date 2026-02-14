package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_category
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_difficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_exit
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_history
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_play
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_exit_game
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_game_banner
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_game_sound_play_pause
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_open_instructions_dialog
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_play_game_button
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_game_tagline
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_highest_score_header
import com.developersbreach.hangman.feature.onboarding.generated.resources.rope_with_title
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
    finishActivity: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                OnBoardingEffect.NavigateToGame -> navigateToGameScreen()
                OnBoardingEffect.NavigateToHistory -> navigateToHistoryScreen()
                OnBoardingEffect.FinishActivity -> finishActivity()
            }
        }
    }

    OnBoardingScreenUI(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun OnBoardingScreenContent(
    uiState: OnBoardingUiState,
    onEvent: (OnBoardingEvent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        FullScreenGameBackground()
        GameTaglineText()

        Spacer(modifier = Modifier.height(12.dp))

        HighestGameScoreText(highScore = uiState.highScore)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                PlayGameButton {
                    onEvent(OnBoardingEvent.NavigateToGameClicked)
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExitGameButton {
                    onEvent(OnBoardingEvent.ExitClicked)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GameHistoryButton {
                    onEvent(OnBoardingEvent.NavigateToHistoryClicked)
                }

                Spacer(modifier = Modifier.height(8.dp))

                GameDifficultyButton(
                    gameDifficulty = uiState.gameDifficulty,
                    onEvent = onEvent
                )

                Spacer(modifier = Modifier.height(8.dp))

                GameCategoryButton(
                    gameCategory = uiState.gameCategory,
                    onEvent = onEvent
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GameInstructionIconButton(
                        gameDifficulty = uiState.gameDifficulty,
                        gameCategory = uiState.gameCategory
                    )

                    BackgroundVolumeIconButton(
                        isBackgroundMusicPlaying = uiState.isBackgroundMusicPlaying,
                        onToggleBackgroundMusic = {
                            onEvent(OnBoardingEvent.ToggleBackgroundMusic)
                        }
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
        painter = painterResource(Res.drawable.rope_with_title),
        contentDescription = stringResource(Res.string.onboarding_cd_game_banner),
        modifier = Modifier.size(220.dp),
        colorFilter = ColorFilter.tint(color = HangmanTheme.colorScheme.primary)
    )
}

@Composable
private fun GameTaglineText() {
    BodySmallText(
        text = stringResource(Res.string.onboarding_game_tagline),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp)
    )
}

@Composable
private fun HighestGameScoreText(highScore: Int) {
    TitleMediumText(
        text = stringResource(Res.string.onboarding_highest_score_header, highScore),
        color = HangmanTheme.colorScheme.secondary,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
private fun PlayGameButton(onClick: () -> Unit) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_play),
        icon = {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = stringResource(Res.string.onboarding_cd_play_game_button),
                tint = HangmanTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },
        onClick = onClick
    )
}

@Composable
private fun ExitGameButton(onClick: () -> Unit) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_exit),
        icon = {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(Res.string.onboarding_cd_exit_game),
                tint = HangmanTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        },
        onClick = onClick
    )
}

@Composable
private fun GameHistoryButton(onClick: () -> Unit) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_history),
        onClick = onClick
    )
}

@Composable
fun GameDifficultyButton(
    gameDifficulty: GameDifficulty,
    onEvent: (OnBoardingEvent) -> Unit
) {
    val openGameDifficultyDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameDifficultyDialog.value) {
        AdjustGameDifficultyDialog(
            openGameDifficultyDialog = openGameDifficultyDialog,
            gameDifficulty = gameDifficulty,
            updatePlayerChosenDifficulty = { sliderPosition ->
                onEvent(OnBoardingEvent.DifficultyChanged(sliderPosition))
            }
        )
    }

    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_difficulty),
        onClick = { openGameDifficultyDialog.value = !openGameDifficultyDialog.value }
    )
}

@Composable
fun GameCategoryButton(
    gameCategory: GameCategory,
    onEvent: (OnBoardingEvent) -> Unit
) {
    val openGameCategoryDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameCategoryDialog.value) {
        ChooseGameCategoryDialog(
            openGameCategoryDialog = openGameCategoryDialog,
            updatePlayerChosenCategory = { categoryId ->
                onEvent(OnBoardingEvent.CategoryChanged(categoryId))
            },
            gameCategory = gameCategory
        )
    }

    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_category),
        onClick = { openGameCategoryDialog.value = !openGameCategoryDialog.value }
    )
}

@Composable
private fun GameInstructionIconButton(
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory
) {
    val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }
    if (openGameInstructionsDialog.value) {
        GameInstructionsDialog(
            gameDifficulty = gameDifficulty,
            gameCategory = gameCategory,
            openGameInstructionsDialog = openGameInstructionsDialog
        )
    }

    IconButton(
        onClick = { openGameInstructionsDialog.value = !openGameInstructionsDialog.value },
        modifier = Modifier.background(
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.15f),
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(Res.string.onboarding_cd_open_instructions_dialog),
            tint = HangmanTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BackgroundVolumeIconButton(
    isBackgroundMusicPlaying: Boolean,
    onToggleBackgroundMusic: () -> Unit,
) {
    val icon = if (isBackgroundMusicPlaying) Icons.AutoMirrored.Filled.VolumeUp else Icons.Filled.VolumeOff

    IconButton(
        onClick = onToggleBackgroundMusic,
        modifier = Modifier.background(
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.15f),
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(Res.string.onboarding_cd_game_sound_play_pause),
            tint = HangmanTheme.colorScheme.primary
        )
    }
}

@Composable
private fun OnBoardingActionButton(
    text: String,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    OutlinedButton(
        modifier = Modifier.width(180.dp),
        onClick = onClick,
        shape = HangmanTheme.shapes.medium,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = HangmanTheme.colorScheme.primary
        ),
        border = BorderStroke(
            width = 2.dp,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(10.dp))
        }

        LabelLargeText(
            text = text,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}