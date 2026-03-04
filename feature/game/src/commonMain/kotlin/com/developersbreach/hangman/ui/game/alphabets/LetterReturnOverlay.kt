package com.developersbreach.hangman.ui.game.alphabets

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.Alphabet
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

internal data class GuessChipIdentity(
    val symbol: String,
    val occurrence: Int,
)

internal data class LetterReturnPath(
    val symbol: String,
    val start: Offset,
    val end: Offset,
    val seed: Int,
)

internal data class LetterReturnSnapshot(
    val correctPaths: List<LetterReturnPath>,
    val incorrectPaths: List<LetterReturnPath>,
)

internal fun buildGuessChipIdentities(playerGuesses: List<String>): List<GuessChipIdentity> {
    val occurrenceBySymbol = mutableMapOf<String, Int>()
    return buildList {
        playerGuesses.forEach { guess ->
            if (guess == " ") {
                return@forEach
            }
            val normalized = guess.lowercase()
            val occurrence = occurrenceBySymbol[normalized] ?: 0
            occurrenceBySymbol[normalized] = occurrence + 1
            add(GuessChipIdentity(symbol = normalized, occurrence = occurrence))
        }
    }
}

@Composable
internal fun LetterReturnGhostOverlay(
    snapshot: LetterReturnSnapshot,
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val easedProgress = FastOutSlowInEasing.transform(progress.coerceIn(0f, 1f))
    Box(modifier = modifier) {
        snapshot.correctPaths.forEach { path ->
            GhostReturnChip(
                symbol = path.symbol,
                center = path.currentPoint(easedProgress),
                seed = path.seed,
                fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.14f),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.76f),
                textColor = HangmanTheme.colorScheme.onBackground.copy(alpha = 0.96f),
            )
        }
        snapshot.incorrectPaths.forEach { path ->
            GhostReturnChip(
                symbol = path.symbol,
                center = path.currentPoint(easedProgress),
                seed = path.seed,
                fillColor = HangmanTheme.colorScheme.error.copy(alpha = 0.14f),
                outlineColor = HangmanTheme.colorScheme.error.copy(alpha = 0.76f),
                textColor = HangmanTheme.colorScheme.error.copy(alpha = 0.96f),
            )
        }
    }
}

internal fun buildLetterReturnSnapshot(
    playerGuesses: List<String>,
    wordToGuess: String,
    alphabetsList: List<Alphabet>,
    chipCentersInRoot: Map<GuessChipIdentity, Offset>,
    tileCentersByAlphabetIdInRoot: Map<Int, Offset>,
    tileCentersBySymbolInRoot: Map<String, Offset>,
    overlayRootInRoot: Offset,
): LetterReturnSnapshot {
    val guessIdentities = buildGuessChipIdentities(playerGuesses)
    val correctPaths = guessIdentities.mapNotNull { identity ->
        val start = chipCentersInRoot[identity] ?: return@mapNotNull null
        val end = tileCentersBySymbolInRoot[identity.symbol] ?: return@mapNotNull null
        LetterReturnPath(
            symbol = identity.symbol.uppercase(),
            start = start - overlayRootInRoot,
            end = end - overlayRootInRoot,
            seed = identity.symbol.hashCode() * 37 + identity.occurrence,
        )
    }

    val lettersInWord = wordToGuess
        .filter { it.isLetter() }
        .map { it.lowercaseChar().toString() }
        .toSet()
    val incorrectPaths = alphabetsList
        .filter { alphabet ->
            alphabet.isAlphabetGuessed && alphabet.alphabet.lowercase() !in lettersInWord
        }
        .mapNotNull { alphabet ->
            val tileCenter = tileCentersByAlphabetIdInRoot[alphabet.alphabetId]
                ?: tileCentersBySymbolInRoot[alphabet.alphabet.lowercase()]
                ?: return@mapNotNull null
            val offset = incorrectStartOffset(alphabet.alphabetId)
            LetterReturnPath(
                symbol = alphabet.alphabet,
                start = tileCenter + offset - overlayRootInRoot,
                end = tileCenter - overlayRootInRoot,
                seed = alphabet.alphabetId,
            )
        }

    return LetterReturnSnapshot(
        correctPaths = correctPaths,
        incorrectPaths = incorrectPaths,
    )
}

private fun LetterReturnPath.currentPoint(progress: Float): Offset {
    val clamped = progress.coerceIn(0f, 1f)
    val wobble = sin((clamped * (2f * PI).toFloat()) + seed * 0.17f) * (1f - clamped) * 6f
    val linear = Offset(
        x = start.x + ((end.x - start.x) * clamped),
        y = start.y + ((end.y - start.y) * clamped),
    )
    val direction = end - start
    val magnitude = sqrt((direction.x * direction.x) + (direction.y * direction.y))
    if (magnitude == 0f) return linear
    val normal = Offset(-direction.y / magnitude, direction.x / magnitude)
    return linear + (normal * wobble)
}

private fun incorrectStartOffset(alphabetId: Int): Offset {
    val angleDegrees = ((alphabetId * 47) % 360).toFloat()
    val angle = (angleDegrees / 180f) * PI.toFloat()
    val radius = 26f + ((alphabetId % 5) * 6f)
    val x = cos(angle) * radius
    val y = (sin(angle) * radius) - 18f
    return Offset(x = x, y = y)
}

private operator fun Offset.times(scale: Float): Offset = Offset(x * scale, y * scale)

@Composable
private fun GhostReturnChip(
    symbol: String,
    center: Offset,
    seed: Int,
    fillColor: Color,
    outlineColor: Color,
    textColor: Color,
) {
    val tileSize = 40.dp
    val tileSizePx = with(LocalDensity.current) { tileSize.toPx() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .offsetToCenter(center = center, tileSizePx = tileSizePx)
            .size(tileSize)
            .creepyOutline(
                seed = seed,
                threshold = 0.14f,
                fillColor = fillColor,
                outlineColor = outlineColor,
            ),
    ) {
        TitleLargeText(
            text = symbol.uppercase(),
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

private fun Modifier.offsetToCenter(
    center: Offset,
    tileSizePx: Float,
): Modifier = this.then(
    Modifier.offset {
        IntOffset(
            x = (center.x - (tileSizePx / 2f)).roundToInt(),
            y = (center.y - (tileSizePx / 2f)).roundToInt(),
        )
    },
)
