package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangmanDialog(
    onDismissRequest: () -> Unit,
    seed: Int,
    modifier: Modifier = Modifier,
    threshold: Float = 0.10f,
    containerColor: Color = HangmanTheme.colorScheme.surfaceContainer,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 4200)

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .creepyOutline(
                    seed = seed,
                    threshold = threshold,
                    fillColor = containerColor,
                    outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
                    forceRectangular = true,
                    strokeWidthFactor = 0.026f,
                    phase = creepyPhase,
                )
                .padding(contentPadding),
            content = content,
        )
    }
}