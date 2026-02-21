package com.developersbreach.hangman.ui.components

import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun HangmanDivider(
    modifier: Modifier = Modifier,
    seed: Int = 911,
    threshold: Float = 0.05f,
    useCreepyOutline: Boolean = true,
    fillColor: Color = Color.Transparent,
    outlineColor: Color = HangmanTheme.colorScheme.primary.copy(alpha = 0.24f),
    color: Color = HangmanTheme.colorScheme.outlineVariant,
    thickness: Dp = DividerDefaults.Thickness,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 3800)
    val decoratedModifier = if (useCreepyOutline) {
        modifier.creepyOutline(
            seed = seed,
            threshold = threshold,
            fillColor = fillColor,
            outlineColor = outlineColor,
            phase = creepyPhase,
            forceRectangular = true,
        )
    } else {
        modifier
    }

    HorizontalDivider(
        modifier = decoratedModifier,
        color = color,
        thickness = thickness,
    )
}
