package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_image_screen_bg
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.onboarding.generated.resources.demon
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_category
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_difficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_exit
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_history
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_play
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_report_issue
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_exit_game
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_game_banner
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_game_sound_play_pause
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_open_instructions_dialog
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_play_game_button
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_report_issue
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_game_tagline
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_highest_score_header
import com.developersbreach.hangman.feature.onboarding.generated.resources.rope_with_title
import com.developersbreach.hangman.feature.onboarding.generated.resources.skull
import com.developersbreach.hangman.ui.common.HangmanInstructionsDialog
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanTextActionButton
import com.developersbreach.hangman.ui.components.HangmanPrimaryButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.rememberInfiniteRotation
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val GITHUB_ISSUES_URL = "https://github.com/RajashekarRaju/hangman-compose/issues"

@Composable
fun OnBoardingUiState.OnBoardingScreenUI(
    onEvent: (OnBoardingEvent) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val showBottomIssueAction = maxWidth >= 900.dp

        Image(
            painter = painterResource(DesignRes.drawable.game_background),
            contentDescription = stringResource(Res.string.onboarding_cd_image_screen_bg),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f
        )

        AnimatedEnter {
            OnBoardingScreenContent(
                uiState = this@OnBoardingScreenUI,
                onEvent = onEvent
            )
        }

        if (isDifficultyDialogOpen) {
            AdjustGameDifficultyDialog(
                selectedDifficulty = pendingDifficulty,
                sliderDifficultyPosition = pendingDifficultySliderPosition,
                onSliderPositionChanged = { onEvent(OnBoardingEvent.DifficultySliderPositionChanged(it)) },
                onDifficultyConfirmed = { onEvent(OnBoardingEvent.DifficultyChanged(it)) },
                onDismissRequest = { onEvent(OnBoardingEvent.DismissDifficultyDialog) }
            )
        }

        if (isCategoryDialogOpen) {
            ChooseGameCategoryDialog(
                gameCategory = gameCategory,
                updatePlayerChosenCategory = { onEvent(OnBoardingEvent.CategoryChanged(it)) },
                onDismissRequest = { onEvent(OnBoardingEvent.DismissCategoryDialog) }
            )
        }

        if (isInstructionsDialogOpen) {
            HangmanInstructionsDialog(
                difficultyValue = gameDifficultyName(gameDifficulty),
                categoryValue = categoryName(gameCategory),
                onDismissRequest = { onEvent(OnBoardingEvent.DismissInstructionsDialog) },
            )
        }

        if (showBottomIssueAction) {
            ReportIssueTextButton(
                onClick = { uriHandler.openUri(GITHUB_ISSUES_URL) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
            )
        }
    }
}

@Composable
private fun OnBoardingScreenContent(
    uiState: OnBoardingUiState,
    onEvent: (OnBoardingEvent) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        stickyHeader {
            FullScreenGameBackground()
            GameTaglineText()
            HighestGameScoreText(highScore = uiState.highScore)
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item { PlayGameButton { onEvent(OnBoardingEvent.NavigateToGameClicked) } }

        item { ExitGameButton { onEvent(OnBoardingEvent.ExitClicked) } }

        item { GameHistoryButton { onEvent(OnBoardingEvent.NavigateToHistoryClicked) } }

        item { GameDifficultyButton(onEvent = onEvent) }

        item { GameCategoryButton(onEvent = onEvent) }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GameInstructionIconButton(onEvent = onEvent)

                BackgroundVolumeIconButton(
                    isBackgroundMusicPlaying = uiState.isBackgroundMusicPlaying,
                    onToggleBackgroundMusic = { onEvent(OnBoardingEvent.ToggleBackgroundMusic) }
                )

                ThemePaletteDropdown(
                    selectedPaletteId = uiState.themePaletteId,
                    expanded = uiState.isPaletteMenuExpanded,
                    onExpandRequest = { onEvent(OnBoardingEvent.OpenThemePaletteMenu) },
                    onDismissRequest = { onEvent(OnBoardingEvent.DismissThemePaletteMenu) },
                    onPaletteChanged = {
                        onEvent(OnBoardingEvent.ThemePaletteChanged(it))
                        onEvent(OnBoardingEvent.DismissThemePaletteMenu)
                    },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ReportIssueTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    HangmanTextActionButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        LabelLargeText(
        text = stringResource(Res.string.onboarding_button_report_issue),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.82f),
            modifier = Modifier.padding(vertical = 4.dp),
        )
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
    val rotation = rememberInfiniteRotation()

    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_play),
        creepinessThreshold = 0.28f,
        icon = {
            HangmanIcon(
                painter = painterResource(Res.drawable.skull),
                contentDescription = stringResource(Res.string.onboarding_cd_play_game_button),
                tint = HangmanTheme.colorScheme.primary,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(rotation)
            )
        },
        onClick = onClick
    )
}

@Composable
private fun ExitGameButton(onClick: () -> Unit) {
    val rotation = rememberInfiniteRotation(initialValue = 360f, targetValue = 0f)

    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_exit),
        creepinessThreshold = 0.28f,
        icon = {
            HangmanIcon(
                painter = painterResource(Res.drawable.demon),
                contentDescription = stringResource(Res.string.onboarding_cd_exit_game),
                tint = HangmanTheme.colorScheme.primary,
                modifier = Modifier
                    .size(28.dp)
                    .rotate(rotation)
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
    onEvent: (OnBoardingEvent) -> Unit
) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_difficulty),
        onClick = { onEvent(OnBoardingEvent.OpenDifficultyDialog) }
    )
}

@Composable
fun GameCategoryButton(
    onEvent: (OnBoardingEvent) -> Unit
) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_category),
        onClick = { onEvent(OnBoardingEvent.OpenCategoryDialog) }
    )
}

@Composable
private fun GameInstructionIconButton(
    onEvent: (OnBoardingEvent) -> Unit
) {
    HangmanIconActionButton(
        onClick = { onEvent(OnBoardingEvent.OpenInstructionsDialog) },
        seed = 701,
        size = 42,
        threshold = 0.14f,
        backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
    ) {
        HangmanIcon(
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
    val icon = when {
        isBackgroundMusicPlaying -> Icons.AutoMirrored.Filled.VolumeUp
        else -> Icons.AutoMirrored.Filled.VolumeOff
    }

    HangmanIconActionButton(
        onClick = onToggleBackgroundMusic,
        seed = 702,
        size = 42,
        threshold = 0.14f,
        backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
    ) {
        HangmanIcon(
            imageVector = icon,
            contentDescription = stringResource(Res.string.onboarding_cd_game_sound_play_pause),
            tint = HangmanTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ThemePaletteDropdown(
    selectedPaletteId: ThemePaletteId,
    expanded: Boolean,
    onExpandRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    onPaletteChanged: (ThemePaletteId) -> Unit,
) {
    val selectedPalette = ThemePalettes.byId(selectedPaletteId)

    Box {
        HangmanIconActionButton(
            onClick = onExpandRequest,
            seed = 703,
            size = 42,
            threshold = 0.14f,
            backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(selectedPalette.previewColor, CircleShape)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            containerColor = HangmanTheme.colorScheme.surfaceContainer,
        ) {
            ThemePalettes.all.forEach { palette ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(palette.previewColor, CircleShape)
                        )
                    },
                    onClick = { onPaletteChanged(palette.id) },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun OnBoardingActionButton(
    text: String,
    onClick: () -> Unit,
    creepinessThreshold: Float = 0.14f,
    icon: @Composable (() -> Unit)? = null
) {
    HangmanPrimaryButton(
        onClick = onClick,
        seed = text.hashCode(),
        threshold = creepinessThreshold,
        modifier = Modifier.width(180.dp),
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
