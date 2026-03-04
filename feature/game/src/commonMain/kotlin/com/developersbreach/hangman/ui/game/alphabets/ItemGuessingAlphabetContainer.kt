package com.developersbreach.hangman.ui.game.alphabets

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import com.developersbreach.hangman.ui.components.HeadlineLargeText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.components.rememberCreepyPhase
import com.developersbreach.hangman.ui.game.LevelTransitionPhase
import com.developersbreach.hangman.ui.theme.HangmanTheme
import kotlin.math.PI
import kotlin.math.sin

@Composable
internal fun GuessedAlphabetsContainer(
    playerGuesses: List<String>,
    levelTransitionPhase: LevelTransitionPhase,
    onChipCenterChanged: (GuessChipIdentity, Offset) -> Unit,
    modifier: Modifier,
    chipSize: Dp,
    chipSpacing: Dp,
    horizontalPadding: Dp,
    innerPadding: Dp,
    creepinessThreshold: Float = 0.12f,
) {
    val guessIdentities = buildGuessChipIdentities(playerGuesses)
    val isShimmerPhase = levelTransitionPhase == LevelTransitionPhase.SUCCESS_SHIMMER
    val chipAlpha by animateFloatAsState(
        targetValue = if (levelTransitionPhase == LevelTransitionPhase.SUCCESS_RETURN) 0.42f else 1f,
        animationSpec = tween(durationMillis = 420),
        label = "GuessedChipAlpha",
    )
    val guessedPhase = rememberCreepyPhase(durationMillis = 2800)
    val shimmerTransition = rememberInfiniteTransition(label = "GuessedContainerShimmer")
    val shimmerPulse = shimmerTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1_250, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "GuessedContainerShimmerPulse",
    ).value
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(chipSpacing, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(chipSpacing, Alignment.CenterVertically),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
    ) {
        var identityIndex = 0
        playerGuesses.forEachIndexed { index, validGuess ->
            when (validGuess) {
                " " -> Spacer(modifier = Modifier.width(chipSize * 0.55f))
                else -> {
                    val identity = guessIdentities[identityIndex]
                    ItemGuessingAlphabetContainer(
                        validGuess = validGuess,
                        creepinessThreshold = creepinessThreshold,
                        seed = validGuess.hashCode() + index,
                        chipSize = chipSize,
                        innerPadding = innerPadding,
                        phase = guessedPhase + index * 0.17f,
                        alpha = chipAlpha,
                        shimmerPulse = if (isShimmerPhase) shimmerPulse else 0f,
                        shimmerIndex = identityIndex,
                        onPlaced = { center -> onChipCenterChanged(identity, center) },
                    )
                }
            }
            if (validGuess != " ") {
                identityIndex += 1
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
    alpha: Float,
    shimmerPulse: Float,
    shimmerIndex: Int,
    onPlaced: (Offset) -> Unit,
) {
    val localPulse = if (shimmerPulse <= 0f) {
        0f
    } else {
        val radians = ((shimmerPulse * 2f * PI) + (shimmerIndex * 0.45f)).toFloat()
        ((sin(radians) + 1f) * 0.5f)
    }
    val pulsedAlpha = alpha * if (shimmerPulse > 0f) (0.90f + (0.10f * localPulse)) else 1f
    val fillAlpha = 0.10f + (0.06f * localPulse)
    val outlineAlpha = 0.72f + (0.14f * localPulse)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .alpha(pulsedAlpha)
            .onGloballyPositioned { coordinates ->
                val position = coordinates.positionInRoot()
                onPlaced(
                    Offset(
                        x = position.x + (coordinates.size.width / 2f),
                        y = position.y + (coordinates.size.height / 2f),
                    ),
                )
            }
            .size(chipSize)
            .creepyOutline(
                seed = seed,
                threshold = creepinessThreshold,
                fillColor = HangmanTheme.colorScheme.primary.copy(alpha = fillAlpha),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = outlineAlpha),
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
