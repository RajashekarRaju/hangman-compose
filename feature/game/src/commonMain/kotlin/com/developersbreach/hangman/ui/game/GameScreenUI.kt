package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.alphabetsList
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_close_game
import com.developersbreach.hangman.feature.game.generated.resources.game_cd_open_instructions
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameScreenUI(
    uiState: GameUiState,
    onEvent: (GameEvent) -> Unit,
) {
    Surface(color = HangmanTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                LevelPointsAttemptsInformation(
                    currentPlayerLevel = uiState.currentPlayerLevel,
                    attemptsLeftToGuess = uiState.attemptsLeftToGuess,
                    pointsScoredOverall = uiState.pointsScoredOverall,
                    maxLevelReached = uiState.maxLevelReached,
                    modifier = Modifier,
                )

                Spacer(modifier = Modifier.height(16.dp))

                GuessedAlphabetsContainer(
                    playerGuesses = uiState.playerGuesses,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.weight(1f))

                AlphabetsList(
                    alphabetsList = uiState.alphabetsList,
                    onAlphabetClicked = { alphabetId ->
                        onEvent(GameEvent.AlphabetClicked(alphabetId))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
            ) {
                IconButton(
                    onClick = { onEvent(GameEvent.BackPressed) },
                    modifier = Modifier.background(
                        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
                        shape = CircleShape,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(Res.string.game_cd_close_game),
                        tint = HangmanTheme.colorScheme.primary,
                        modifier = Modifier.alpha(0.75f),
                    )
                }

                IconButton(
                    onClick = { onEvent(GameEvent.ToggleInstructionsDialog) },
                    modifier = Modifier.background(
                        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
                        shape = CircleShape,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = stringResource(Res.string.game_cd_open_instructions),
                        tint = HangmanTheme.colorScheme.primary,
                        modifier = Modifier.alpha(0.75f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun GameScreenUIContent() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.ORIGINAL),
    ) {
        GameScreenUI(
            uiState = GameUiState(
                currentPlayerLevel = 2,
                attemptsLeftToGuess = 5,
                pointsScoredOverall = 4,
                maxLevelReached = 5,
                alphabetsList = alphabetsList(),
                playerGuesses = listOf("H", "A", "N"),
            ),
            onEvent = {},
        )
    }
}
