package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.developersbreach.hangman.ui.components.HeadlineLargeText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.components.rememberCreepyPhase
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun GuessedAlphabetsContainer(
    playerGuesses: List<String>,
    modifier: Modifier,
    chipSize: Dp,
    chipSpacing: Dp,
    horizontalPadding: Dp,
    innerPadding: Dp,
    creepinessThreshold: Float = 0.12f,
) {
    val guessedPhase = rememberCreepyPhase(durationMillis = 2800)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(chipSpacing, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(chipSpacing, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    ) {
        playerGuesses.forEachIndexed { index, validGuess ->
            when (validGuess) {
                " " -> Spacer(modifier = Modifier.width(chipSize * 0.55f))
                else ->
                    ItemGuessingAlphabetContainer(
                        validGuess = validGuess,
                        creepinessThreshold = creepinessThreshold,
                        seed = validGuess.hashCode() + index,
                        chipSize = chipSize,
                        innerPadding = innerPadding,
                        phase = guessedPhase + index * 0.17f,
                    )
            }
        }
    }
}

@Composable
private fun ItemGuessingAlphabetContainer(
    validGuess: String,
    creepinessThreshold: Float,
    seed: Int,
    chipSize: Dp,
    innerPadding: Dp,
    phase: Float,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(chipSize)
            .creepyOutline(
                seed = seed,
                threshold = creepinessThreshold,
                fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.10f),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.72f),
                phase = phase,
            )
            .padding(innerPadding),
    ) {
        HeadlineLargeText(
            text = validGuess,
            color = HangmanTheme.colorScheme.onBackground,
        )
    }
}
