package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.CreepySlider
import com.developersbreach.hangman.ui.components.HeadlineSmallText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineDifficultySection(
    pendingDifficultySliderPosition: Float,
    pendingDifficultyLabelRes: StringResource,
    onSliderPositionChanged: (Float) -> Unit,
    onDifficultyConfirmed: () -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Box(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CreepySlider(
                value = pendingDifficultySliderPosition,
                onValueChange = onSliderPositionChanged,
                valueRange = 1.0f..4.0f,
                steps = 2,
                onValueChangeFinished = onDifficultyConfirmed,
                seed = 1201,
                trackCreepiness = 1f,
                activeTrackCreepiness = 0.9f,
                modifier = Modifier.fillMaxWidth(),
            )

            HeadlineSmallText(
                text = stringResource(pendingDifficultyLabelRes),
                color = HangmanTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}