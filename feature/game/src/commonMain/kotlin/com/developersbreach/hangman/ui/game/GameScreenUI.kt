package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_close_game
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_open_instructions
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameUiState.GameScreenUI(
    onEvent: (GameEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedEnter {
            GameMainLayout(
                uiState = this@GameScreenUI,
                onEvent = onEvent,
            )
        }

        AnimatedEnter(offsetY = 16.dp) {
            GameNavigationActionIcons(onEvent)
        }
    }
}

@Composable
private fun GameNavigationActionIcons(onEvent: (GameEvent) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),
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
}

@Composable
private fun GameMainLayout(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val supportsWideLayout = if (LocalInspectionMode.current) true else isWideGameLayout()
        val wideLayout = supportsWideLayout && maxWidth >= 900.dp
        val sizing = calculateGameResponsiveSizing(
            maxWidth = maxWidth,
            maxHeight = maxHeight,
            wideLayout = wideLayout,
        )

        if (wideLayout) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(sizing.paneGap, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = sizing.rootTopPadding,
                        start = sizing.rootHorizontalPadding,
                        end = sizing.rootHorizontalPadding,
                        bottom = sizing.rootBottomPadding,
                    ),
            ) {
                GamePhaseProgress(
                    uiState = uiState,
                    progressScale = sizing.progressScale,
                    modifier = Modifier
                        .widthIn(max = sizing.progressPaneWidth)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                )

                GamePhaseGuessesAndAlphabets(
                    uiState = uiState,
                    onEvent = onEvent,
                    sizing = sizing,
                    modifier = Modifier
                        .widthIn(max = sizing.boardPaneWidth)
                        .wrapContentHeight(align = Alignment.CenterVertically),
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = sizing.rootTopPadding,
                        bottom = sizing.rootBottomPadding,
                        start = sizing.rootHorizontalPadding,
                        end = sizing.rootHorizontalPadding,
                    ),
            ) {
                GamePhaseProgress(
                    uiState = uiState,
                    progressScale = sizing.progressScale,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(sizing.compactSectionSpacing))

                GamePhaseGuessesAndAlphabets(
                    uiState = uiState,
                    onEvent = onEvent,
                    sizing = sizing,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun GamePhaseProgress(
    uiState: GameUiState,
    progressScale: Float,
    modifier: Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
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
private fun GamePhaseGuessesAndAlphabets(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    sizing: GameResponsiveSizing,
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
            maxGridHeight = sizing.alphabetMaxHeight,
            onAlphabetClicked = { onEvent(GameEvent.AlphabetClicked(it)) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
