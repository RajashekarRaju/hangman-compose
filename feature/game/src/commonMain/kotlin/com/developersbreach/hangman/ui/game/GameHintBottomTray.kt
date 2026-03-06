package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_cooldown
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_counter
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_eliminate_letters
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_reveal_letter
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_status_value
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_title
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HangmanTextActionButton
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HintBottomTray(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val canUseHints = uiState.hintsRemaining > 0 &&
        !uiState.isHintOnCooldown &&
        !uiState.gameOverByWinning &&
        !uiState.revealGuessingWord &&
        !uiState.isInteractionLocked
    val hintStatusText = when {
        uiState.isHintOnCooldown -> stringResource(Res.string.game_hint_cooldown)
        else -> stringResource(Res.string.game_hint_counter, uiState.hintsRemaining)
    }

    val feedbackMessage = uiState.hintFeedback?.error?.let { error ->
        stringResource(error.messageRes())
    }

    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        val density = LocalDensity.current
        var trayContentHeightPx by remember { mutableIntStateOf(0) }
        HangmanDivider(
            modifier = Modifier.fillMaxWidth(),
            seed = 801,
            threshold = 0.45f,
            thickness = 6.dp,
            fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
            outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.05f),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0f),
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val backgroundImageAlpha = when {
                HangmanTheme.colorScheme.background.luminance() > 0.6f -> 0.75f
                else -> 0.1f
            }
            Image(
                painter = painterResource(DesignRes.drawable.game_background),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(density) { trayContentHeightPx.toDp() }),
                contentScale = ContentScale.Crop,
                alpha = backgroundImageAlpha,
            )

            BottomTrayContent(
                modifier = Modifier.onSizeChanged { trayContentHeightPx = it.height },
                hintStatusText = hintStatusText,
                canUseHints = canUseHints,
                onEvent = onEvent,
                uiState = uiState,
                feedbackMessage = feedbackMessage
            )
        }
    }
}

@Composable
private fun BottomTrayContent(
    modifier: Modifier,
    hintStatusText: String,
    canUseHints: Boolean,
    onEvent: (GameEvent) -> Unit,
    uiState: GameUiState,
    feedbackMessage: String?
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BodyLargeText(
                text = stringResource(Res.string.game_hint_title),
                color = HangmanTheme.colorScheme.primary.copy(alpha = 0.60f),
            )
            BodyLargeText(
                text = stringResource(
                    resource = Res.string.game_hint_status_value,
                    hintStatusText,
                ),
                color = HangmanTheme.colorScheme.primary,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            HintActionButton(
                text = stringResource(Res.string.game_hint_reveal_letter),
                enabled = canUseHints,
                modifier = Modifier,
                onClick = { onEvent(GameEvent.HintSelected(HintType.REVEAL_LETTER)) },
            )

            HintActionButton(
                text = stringResource(Res.string.game_hint_eliminate_letters),
                enabled = canUseHints,
                modifier = Modifier,
                onClick = { onEvent(GameEvent.HintSelected(HintType.ELIMINATE_LETTERS)) },
            )
        }

        if (uiState.showHintFeedbackDialog && feedbackMessage != null) {
            BodyLargeText(
                text = feedbackMessage,
                color = HangmanTheme.colorScheme.error.copy(alpha = 0.86f),
            )
        }
    }
}

@Composable
private fun HintActionButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    HangmanTextActionButton(
        modifier = modifier
            .alpha(if (enabled) 1f else 0.46f)
            .creepyOutline(
                seed = text.hashCode(),
                threshold = 0.08f,
                fillColor = if (enabled) {
                    Color.Transparent
                } else {
                    HangmanTheme.colorScheme.onSurface.copy(alpha = 0.03f)
                },
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = if (enabled) 0.42f else 0.24f),
                forceRectangular = true,
            ),
        onClick = { if (enabled) onClick() },
    ) {
        BodyLargeText(
            text = text,
            color = when {
                enabled -> HangmanTheme.colorScheme.primary
                else -> HangmanTheme.colorScheme.onSurface.copy(alpha = 0.56f)
            },
        )
    }
}