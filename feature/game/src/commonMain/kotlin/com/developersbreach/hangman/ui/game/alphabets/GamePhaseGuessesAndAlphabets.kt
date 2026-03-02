package com.developersbreach.hangman.ui.game.alphabets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import com.developersbreach.hangman.ui.game.GameEvent
import com.developersbreach.hangman.ui.game.GameUiState
import com.developersbreach.hangman.ui.game.LevelTransitionPhase

@Composable
internal fun GamePhaseGuessesAndAlphabets(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    sizing: GameLayoutSizing,
    modifier: Modifier,
) {
    val chipCentersInRoot = remember { mutableStateMapOf<GuessChipIdentity, Offset>() }
    val tileCentersByIdInRoot = remember { mutableStateMapOf<Int, Offset>() }
    val tileCentersBySymbolInRoot = remember { mutableStateMapOf<String, Offset>() }
    var overlayRootInRoot by remember { mutableStateOf(Offset.Zero) }

    val returnOverlayProgress by animateFloatAsState(
        targetValue = if (uiState.levelTransitionPhase == LevelTransitionPhase.SUCCESS_RETURN) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = LinearEasing),
        label = "LetterReturnOverlayProgress",
    )

    val chipCentersSnapshot = chipCentersInRoot.toMap()
    val tileCentersByIdSnapshot = tileCentersByIdInRoot.toMap()
    val tileCentersBySymbolSnapshot = tileCentersBySymbolInRoot.toMap()
    val returnSnapshot = remember(
        uiState.levelTransitionPhase,
        uiState.playerGuesses,
        uiState.wordToGuess,
        uiState.alphabetsList,
        overlayRootInRoot,
        chipCentersSnapshot,
        tileCentersByIdSnapshot,
        tileCentersBySymbolSnapshot,
    ) {
        if (uiState.levelTransitionPhase == LevelTransitionPhase.SUCCESS_RETURN) {
            buildLetterReturnSnapshot(
                playerGuesses = uiState.playerGuesses,
                wordToGuess = uiState.wordToGuess,
                alphabetsList = uiState.alphabetsList,
                chipCentersInRoot = chipCentersSnapshot,
                tileCentersByAlphabetIdInRoot = tileCentersByIdSnapshot,
                tileCentersBySymbolInRoot = tileCentersBySymbolSnapshot,
                overlayRootInRoot = overlayRootInRoot,
            )
        } else {
            null
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                overlayRootInRoot = coordinates.positionInRoot()
            },
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            GuessedAlphabetsContainer(
                playerGuesses = uiState.playerGuesses,
                levelTransitionPhase = uiState.levelTransitionPhase,
                onChipCenterChanged = { identity, centerInRoot ->
                    chipCentersInRoot[identity] = centerInRoot
                },
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
                onTileCenterChanged = { alphabetId, symbol, centerInRoot ->
                    tileCentersByIdInRoot[alphabetId] = centerInRoot
                    tileCentersBySymbolInRoot[symbol] = centerInRoot
                },
                creepinessThreshold = sizing.alphabetCreepiness,
                tileSize = sizing.alphabetTileSize,
                spacing = sizing.alphabetSpacing,
                contentPadding = sizing.alphabetPadding,
                onAlphabetClicked = { onEvent(GameEvent.AlphabetClicked(it)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        if (returnSnapshot != null && uiState.levelTransitionPhase == LevelTransitionPhase.SUCCESS_RETURN) {
            LetterReturnGhostOverlay(
                snapshot = returnSnapshot,
                progress = returnOverlayProgress,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}
