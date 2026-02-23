package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun HangmanPrimaryButton(
    onClick: () -> Unit,
    seed: Int,
    modifier: Modifier = Modifier,
    threshold: Float = 0.14f,
    content: @Composable () -> Unit,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 3600)
    Button(
        onClick = onClick,
        shape = HangmanTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = HangmanTheme.colorScheme.primary,
        ),
        modifier = modifier.creepyOutline(
            seed = seed,
            threshold = threshold,
            fillColor = Color.Transparent,
            outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.62f),
            phase = creepyPhase + (seed % 7) * 0.22f,
        ),
        content = { content() },
    )
}

@Composable
fun HangmanTextActionButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        LabelLargeText(
            text = text,
            color = color,
        )
    }
}

@Composable
fun HangmanTextActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        content()
    }
}

@Composable
fun HangmanIconActionButton(
    onClick: () -> Unit,
    seed: Int,
    modifier: Modifier = Modifier,
    size: Int = 48,
    threshold: Float = 0.14f,
    backgroundColor: Color = Color.Transparent,
    fillColor: Color = HangmanTheme.colorScheme.primary.copy(alpha = 0.15f),
    outlineColor: Color = HangmanTheme.colorScheme.primary.copy(alpha = 0.55f),
    content: @Composable () -> Unit,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 3200)
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size.dp)
            .background(
                color = backgroundColor,
                shape = CircleShape,
            )
            .creepyOutline(
                seed = seed,
                threshold = threshold,
                fillColor = fillColor,
                outlineColor = outlineColor,
                phase = creepyPhase + (seed % 5) * 0.31f,
            ),
    ) {
        content()
    }
}
