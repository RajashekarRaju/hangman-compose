package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreepySlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier.Companion,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    enabled: Boolean = true,
    seed: Int = 0,
    thumbCreepiness: Float = 0.18f,
    trackCreepiness: Float = 0.26f,
    activeTrackCreepiness: Float = 0.20f,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 2600)

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeFinished,
        enabled = enabled,
        colors = SliderDefaults.colors(
            thumbColor = Color.Companion.Transparent,
            activeTrackColor = Color.Companion.Transparent,
            inactiveTrackColor = Color.Companion.Transparent,
            disabledThumbColor = Color.Companion.Transparent,
            disabledActiveTrackColor = Color.Companion.Transparent,
            disabledInactiveTrackColor = Color.Companion.Transparent,
        ),
        thumb = {
            Box(
                contentAlignment = Alignment.Companion.Center,
                modifier = Modifier.Companion
                    .size(26.dp)
                    .creepyOutline(
                        seed = seed + 17,
                        threshold = thumbCreepiness,
                        fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.16f),
                        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.85f),
                        phase = creepyPhase + 0.25f,
                    ),
            ) {
                Box(
                    modifier = Modifier.Companion
                        .size(8.dp)
                        .background(HangmanTheme.colorScheme.primary, CircleShape),
                )
            }
        },
        track = { sliderState ->
            val start = sliderState.valueRange.start
            val end = sliderState.valueRange.endInclusive
            val progress = if (end == start) 0f else {
                ((sliderState.value - start) / (end - start)).coerceIn(0f, 1f)
            }

            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .height(16.dp)
                    .creepyOutline(
                        seed = seed + 23,
                        threshold = trackCreepiness,
                        fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.12f),
                        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.48f),
                        forceRectangular = true,
                        phase = creepyPhase,
                    ),
            ) {
                Box(
                    modifier = Modifier.Companion
                        .fillMaxWidth(progress)
                        .height(16.dp)
                        .padding(horizontal = 2.dp, vertical = 2.dp)
                        .creepyOutline(
                            seed = seed + 31,
                            threshold = activeTrackCreepiness,
                            fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.72f),
                            outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.86f),
                            forceRectangular = true,
                            phase = creepyPhase + 0.55f,
                        ),
                )
            }
        },
        modifier = modifier,
    )
}