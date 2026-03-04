package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.LEVELS_PER_GAME
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_close_game
import com.developersbreach.hangman.feature.game.generated.resources.game_current_level
import com.developersbreach.hangman.feature.game.generated.resources.game_current_points
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_open_instructions
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GameNavigationActionIcons(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isTraditionalVisual = uiState.progressVisualType == GameProgressVisualType.TraditionalHangman
        val useWideStatsBar = isTraditionalVisual && maxWidth >= 760.dp
        val showTopHint = supportsGameTopHint() && maxWidth >= 900.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
        ) {
            if (isTraditionalVisual) {
                val displayedLevel = resolveDisplayedLevel(
                    currentPlayerLevel = uiState.currentPlayerLevel,
                    maxLevelReached = uiState.maxLevelReached,
                )
                if (useWideStatsBar) {
                    TraditionalWideNavigationBar(
                        displayedLevel = displayedLevel,
                        attemptsLeftToGuess = uiState.attemptsLeftToGuess,
                        pointsScoredOverall = uiState.pointsScoredOverall,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                } else {
                    TraditionalCompactNavigationBar(
                        displayedLevel = displayedLevel,
                        attemptsLeftToGuess = uiState.attemptsLeftToGuess,
                        pointsScoredOverall = uiState.pointsScoredOverall,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                DefaultNavigationBar(
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (showTopHint) {
                GameCategoryHintText(
                    categoryHint = uiState.categoryHint,
                    alpha = 0.84f,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun DefaultNavigationBar(
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        BackNavigationButton(onEvent = onEvent)
        GameGuideNavigationButton(onEvent = onEvent)
    }
}

@Composable
private fun TraditionalWideNavigationBar(
    displayedLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        BackNavigationButton(onEvent = onEvent)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            TopStatBadge(
                text = stringResource(Res.string.game_current_level, displayedLevel, LEVELS_PER_GAME),
                seed = 931,
                showOutline = false,
            )
            TopStatBadge(
                text = "Attempts Left $attemptsLeftToGuess",
                seed = 932
            )
            TopStatBadge(
                text = stringResource(Res.string.game_current_points, pointsScoredOverall),
                seed = 933,
                showOutline = false,
            )
        }
        GameGuideNavigationButton(onEvent = onEvent)
    }
}

@Composable
private fun TraditionalCompactNavigationBar(
    displayedLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            BackNavigationButton(onEvent = onEvent)
            GameGuideNavigationButton(onEvent = onEvent)
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                TopStatBadge(
                    text = "L $displayedLevel",
                    seed = 941,
                    showOutline = false,
                )
                TopStatBadge(
                    text = "Pts $pointsScoredOverall",
                    seed = 943,
                    showOutline = false,
                )
            }
            TopStatBadge(
                text = "Att $attemptsLeftToGuess",
                seed = 942,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
private fun BackNavigationButton(onEvent: (GameEvent) -> Unit) {
    HangmanIconActionButton(
        onClick = { onEvent(GameEvent.BackPressed) },
        seed = 901,
        size = 42,
        threshold = 0.12f,
        backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
    ) {
        HangmanIcon(
            imageVector = Icons.Filled.Close,
            contentDescription = stringResource(Res.string.game_cd_close_game),
            tint = HangmanTheme.colorScheme.primary,
            modifier = Modifier.alpha(0.75f),
        )
    }
}

@Composable
private fun GameGuideNavigationButton(onEvent: (GameEvent) -> Unit) {
    HangmanIconActionButton(
        onClick = { onEvent(GameEvent.ToggleGameGuideOverlay) },
        seed = 902,
        size = 42,
        threshold = 0.12f,
        backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
    ) {
        HangmanIcon(
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(Res.string.game_cd_open_instructions),
            tint = HangmanTheme.colorScheme.primary,
            modifier = Modifier.alpha(0.75f),
        )
    }
}

@Composable
private fun TopStatBadge(
    text: String,
    seed: Int,
    showOutline: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val decoratedModifier = if (showOutline) {
        modifier.creepyOutline(
            seed = seed,
            threshold = 0.08f,
            fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.10f),
            outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.52f),
        )
    } else {
        modifier
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = decoratedModifier
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        BodyLargeText(
            text = text,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.88f),
        )
    }
}