package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.demon
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_achievements
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_exit
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_game_guide
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_history
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_play
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_report_issue
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_settings
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_exit_game
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_game_banner
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_image_screen_bg
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_play_game_button
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_game_tagline
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_highest_score_header
import com.developersbreach.hangman.feature.onboarding.generated.resources.rope_with_title
import com.developersbreach.hangman.feature.onboarding.generated.resources.skull
import com.developersbreach.hangman.ui.common.HangmanInstructionsDialog
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanPrimaryButton
import com.developersbreach.hangman.ui.components.HangmanTextActionButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.components.rememberInfiniteRotation
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnBoardingUiState.OnBoardingScreenUI(
    onEvent: (OnBoardingEvent) -> Unit
) {
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

        if (isInstructionsDialogOpen) {
            HangmanInstructionsDialog(
                difficultyValue = stringResource(gameDifficultyLabelRes),
                categoryValue = stringResource(gameCategoryLabelRes),
                onDismissRequest = { onEvent(OnBoardingEvent.DismissInstructionsDialog) },
            )
        }

        if (showBottomIssueAction) {
            ReportIssueTextButton(
                onClick = { onEvent(OnBoardingEvent.ReportIssueClicked) },
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

        item {
            AchievementsButton(
                hasUnread = uiState.hasUnreadAchievements,
                onClick = { onEvent(OnBoardingEvent.NavigateToAchievementsClicked) },
            )
        }

        item { SettingsButton { onEvent(OnBoardingEvent.NavigateToSettingsClicked) } }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            GameGuideButton(onClick = { onEvent(OnBoardingEvent.OpenInstructionsDialog) })

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
private fun AchievementsButton(
    hasUnread: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.width(200.dp)
    ) {
        OnBoardingActionButton(
            text = stringResource(Res.string.onboarding_button_achievements),
            onClick = onClick,
        )

        if (hasUnread) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 4.dp, end = 6.dp)
                    .size(20.dp)
                    .background(
                        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.2f),
                        shape = CircleShape,
                    )
                    .creepyOutline(
                        seed = 905,
                        threshold = 0.07f,
                        fillColor = Color.Transparent,
                        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.85f),
                        strokeWidthFactor = 0.06f,
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .background(
                            color = HangmanTheme.colorScheme.primary,
                            shape = CircleShape,
                        ),
                )
            }
        }
    }
}

@Composable
private fun SettingsButton(onClick: () -> Unit) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_settings),
        onClick = onClick,
    )
}

@Composable
private fun GameGuideButton(
    onClick: () -> Unit
) {
    OnBoardingActionButton(
        text = stringResource(Res.string.onboarding_button_game_guide),
        onClick = onClick,
    )
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
        modifier = Modifier.width(200.dp),
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(10.dp))
        }

        LabelLargeText(
            text = text,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}
