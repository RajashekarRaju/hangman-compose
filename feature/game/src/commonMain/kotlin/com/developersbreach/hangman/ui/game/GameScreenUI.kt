package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_close_game
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_open_instructions
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_cooldown
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_counter
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_eliminate_letters
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_reveal_letter
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_title
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanScaffold
import com.developersbreach.hangman.ui.components.HangmanTextActionButton
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameUiState.GameScreenUI(
    onEvent: (GameEvent) -> Unit,
) {
    HangmanScaffold(
        topBar = {
            AnimatedEnter(offsetY = 16.dp) {
                GameNavigationActionIcons(
                    uiState = this,
                    onEvent = onEvent,
                )
            }
        },
        bottomBar = {
            AnimatedEnter(offsetY = 12.dp) {
                HintBottomTray(
                    uiState = this@GameScreenUI,
                    onEvent = onEvent,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.background,
        contentColor = HangmanTheme.colorScheme.onBackground,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {
            AnimatedEnter {
                PlatformGameMainLayout(
                    uiState = this@GameScreenUI,
                    onEvent = onEvent,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun GameNavigationActionIcons(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val showTopHint = supportsGameTopHint() && maxWidth >= 900.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
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

                HangmanIconActionButton(
                    onClick = { onEvent(GameEvent.ToggleInstructionsDialog) },
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
internal fun GamePhaseProgress(
    uiState: GameUiState,
    progressScale: Float,
    modifier: Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        LevelPointsAttemptsInformation(
            currentPlayerLevel = uiState.currentPlayerLevel,
            attemptsLeftToGuess = uiState.attemptsLeftToGuess,
            pointsScoredOverall = uiState.pointsScoredOverall,
            maxLevelReached = uiState.maxLevelReached,
            levelTimeProgress = uiState.levelTimeProgress,
            progressScale = progressScale,
            modifier = Modifier,
        )
    }
}

@Composable
internal fun GamePhaseGuessesAndAlphabets(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    sizing: GameLayoutSizing,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        GuessedAlphabetsContainer(
            playerGuesses = uiState.playerGuesses,
            creepinessThreshold = sizing.guessedCreepiness,
            chipSize = sizing.guessedChipSize,
            chipSpacing = sizing.guessedChipSpacing,
            horizontalPadding = sizing.guessedHorizontalPadding,
            innerPadding = sizing.guessedInnerPadding,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(sizing.compactSectionSpacing))

        AlphabetsList(
            alphabetsList = uiState.alphabetsList,
            creepinessThreshold = sizing.alphabetCreepiness,
            tileSize = sizing.alphabetTileSize,
            spacing = sizing.alphabetSpacing,
            contentPadding = sizing.alphabetPadding,
            onAlphabetClicked = { onEvent(GameEvent.AlphabetClicked(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ColumnScope.HintBottomTray(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val canUseHints = uiState.hintsRemaining > 0 &&
        !uiState.isHintOnCooldown &&
        !uiState.gameOverByWinning &&
        !uiState.revealGuessingWord
    val hintStatusText = when {
        uiState.isHintOnCooldown -> stringResource(Res.string.game_hint_cooldown)
        else -> stringResource(Res.string.game_hint_counter, uiState.hintsRemaining)
    }

    val feedbackMessage = uiState.hintFeedback?.error?.let { error ->
        stringResource(error.messageRes())
    }

    HangmanDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        seed = 801,
        threshold = 0.45f,
        thickness = 6.dp,
        fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.05f),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0f),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
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
                text = " : $hintStatusText",
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
                fillColor = if (enabled) Color.Transparent else HangmanTheme.colorScheme.onSurface.copy(alpha = 0.03f),
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