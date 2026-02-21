package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_difficulty_dialog_title
import com.developersbreach.hangman.ui.components.CreepySlider
import com.developersbreach.hangman.ui.components.HeadlineSmallText
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdjustGameDifficultyDialog(
    selectedDifficulty: GameDifficulty,
    sliderDifficultyPosition: Float,
    onSliderPositionChanged: (Float) -> Unit,
    onDifficultyConfirmed: (GameDifficulty) -> Unit,
    onDismissRequest: () -> Unit,
) {
    HangmanDialog(
        onDismissRequest = onDismissRequest,
        seed = 811,
        threshold = 0.10f,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 40.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        TitleMediumText(
            text = stringResource(Res.string.onboarding_difficulty_dialog_title),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        CreepySlider(
            value = sliderDifficultyPosition,
            onValueChange = onSliderPositionChanged,
            valueRange = 1.0f..3.0f,
            steps = 1,
            onValueChangeFinished = { onDifficultyConfirmed(selectedDifficulty) },
            seed = 1201,
            trackCreepiness = 1f,
            activeTrackCreepiness = 0.9f,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        HeadlineSmallText(
            text = gameDifficultyName(selectedDifficulty),
            color = HangmanTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}
