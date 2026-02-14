package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_difficulty_dialog_title
import com.developersbreach.hangman.ui.components.HeadlineSmallText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AdjustGameDifficultyDialog(
    gameDifficulty: GameDifficulty,
    openGameDifficultyDialog: MutableState<Boolean>,
    updatePlayerChosenDifficulty: (Float) -> Unit
) {
    var sliderDifficultyPosition by rememberSaveable {
        val ordinal = gameDifficulty.ordinal
        mutableFloatStateOf((ordinal + 1).toFloat())
    }

    Dialog(
        onDismissRequest = {
            openGameDifficultyDialog.value = !openGameDifficultyDialog.value
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HangmanTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            TitleMediumText(
                text = stringResource(Res.string.onboarding_difficulty_dialog_title),
                color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = sliderDifficultyPosition,
                onValueChange = { sliderDifficultyPosition = it },
                valueRange = 1.0f..3.0f,
                steps = 1,
                onValueChangeFinished = {
                    updatePlayerChosenDifficulty(sliderDifficultyPosition)
                },
                colors = SliderDefaults.colors(
                    thumbColor = HangmanTheme.colorScheme.primary,
                    activeTrackColor = HangmanTheme.colorScheme.primary,
                    inactiveTrackColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f)
                ),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            HeadlineSmallText(
                text = gameDifficultyName(gameDifficulty),
                color = HangmanTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}