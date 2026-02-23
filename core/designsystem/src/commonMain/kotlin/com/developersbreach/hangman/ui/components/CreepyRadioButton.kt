package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun CreepyRadioButton(
    selected: Boolean,
    modifier: Modifier = Modifier,
    seed: Int = 0,
    enabled: Boolean = true,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 3000)
    val baseColor = HangmanTheme.colorScheme.primary
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .alpha(if (enabled) 1f else 0.5f)
            .size(26.dp)
            .creepyOutline(
                seed = seed,
                threshold = if (selected) 0.16f else 0.10f,
                fillColor = baseColor.copy(alpha = if (selected) 0.16f else 0.06f),
                outlineColor = baseColor.copy(alpha = if (selected) 0.85f else 0.55f),
                phase = creepyPhase + (seed % 7) * 0.2f,
            ),
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(baseColor, CircleShape),
            )
        }
    }
}
