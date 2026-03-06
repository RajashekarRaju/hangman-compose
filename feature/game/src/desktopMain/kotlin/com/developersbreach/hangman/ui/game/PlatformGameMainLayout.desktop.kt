package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.game.alphabets.GamePhaseGuessesAndAlphabets
import com.developersbreach.hangman.ui.game.alphabets.compactGameLayoutSizing
import com.developersbreach.hangman.ui.game.alphabets.wideGameLayoutSizing

@Composable
internal actual fun PlatformGameMainLayout(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val isShortHeight = maxHeight < 760.dp
        val useWideLayout = maxWidth >= 900.dp
        val sizing = when {
            useWideLayout -> wideGameLayoutSizing(isShortHeight)
            else -> compactGameLayoutSizing(isShortHeight)
        }
        val useLegacyProgressVisual =
            uiState.progressVisualType == GameProgressVisualType.LevelPointsAttemptsInformation
        val progressPaneMinWidth = if (useLegacyProgressVisual) 280.dp else 360.dp
        val progressPaneMaxWidth = if (useLegacyProgressVisual) 430.dp else 560.dp
        val guessesPaneMaxWidth = if (useLegacyProgressVisual) 920.dp else 860.dp

        if (useWideLayout) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(sizing.paneGap, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = sizing.rootTopPadding,
                        start = sizing.rootHorizontalPadding,
                        end = sizing.rootHorizontalPadding,
                    ),
            ) {
                GamePhaseProgress(
                    uiState = uiState,
                    progressScale = sizing.progressScale,
                    modifier = Modifier.widthIn(
                        min = progressPaneMinWidth,
                        max = progressPaneMaxWidth,
                    ),
                )
                GamePhaseGuessesAndAlphabets(
                    uiState = uiState,
                    onEvent = onEvent,
                    sizing = sizing,
                    modifier = Modifier.widthIn(max = guessesPaneMaxWidth),
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = sizing.rootTopPadding,
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
