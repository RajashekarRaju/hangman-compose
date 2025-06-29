package com.developersbreach.hangman.desktop.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameProgressInfo(
    modifier: Modifier,
    currentPlayerLevel: Int,
    attemptsLeft: Int,
    pointsScored: Int,
    maxLevel: Int
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        ProgressBars(currentPlayerLevel, attemptsLeft)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            ProgressText(pointsScored, currentPlayerLevel, maxLevel)
        }
    }
}

@Composable
private fun ProgressBars(currentPlayerLevel: Int, attemptsLeft: Int) {
    CircularProgress(
        progress = animateCurrentLevelProgress(currentPlayerLevel),
        size = 200.dp
    )
    CircularProgress(
        progress = 1f,
        progressColor = MaterialTheme.colors.primary.copy(0.25f),
        size = 200.dp
    )
    CircularProgress(
        progress = animateAttemptsLeftProgress(attemptsLeft),
        strokeWidth = 10.dp,
        size = 240.dp,
        progressColor = Color.Red.copy(0.95f)
    )
    CircularProgress(
        progress = 1f,
        strokeWidth = 10.dp,
        progressColor = Color.Green.copy(0.25f),
        size = 240.dp
    )
}

@Composable
private fun ProgressText(points: Int, currentLevel: Int, maxLevel: Int) {
    val levelDisplay = if (currentLevel < maxLevel) currentLevel + 1 else currentLevel
    Text(
        text = buildAnnotatedString {
            append("LEVEL ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 28.sp)) {
                append("$levelDisplay/$maxLevel")
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center
    )
    Divider(
        modifier = Modifier
            .width(100.dp)
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.small),
        color = MaterialTheme.colors.primary.copy(0.25f),
        thickness = 2.dp
    )
    Text(
        text = buildAnnotatedString {
            append("POINTS ")
            withStyle(SpanStyle(color = MaterialTheme.colors.primary, fontSize = 28.sp)) {
                append(points.toString())
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center
    )
}
