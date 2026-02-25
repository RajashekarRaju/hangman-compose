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

@Composable
internal actual fun PlatformGameMainLayout(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
    modifier: Modifier,
) {
    BoxWithConstraints(modifier = modifier) {
        val isShortHeight = maxHeight < 760.dp
        val useWideLayout = maxWidth >= 840.dp && maxWidth > maxHeight
        val sizing = if (useWideLayout) wideGameLayoutSizing(isShortHeight) else compactGameLayoutSizing(isShortHeight)

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
                    modifier = Modifier.widthIn(min = 280.dp, max = 430.dp),
                )

                GamePhaseGuessesAndAlphabets(
                    uiState = uiState,
                    onEvent = onEvent,
                    sizing = sizing,
                    modifier = Modifier.widthIn(max = 920.dp),
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

