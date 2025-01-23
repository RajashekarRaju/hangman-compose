package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.developersbreach.hangman.R

// Dialog for adjusting game difficulty from OnBoarding screen.
@Composable
fun AdjustGameDifficultyDialog(
    viewModel: OnBoardingViewModel,
    openGameDifficultyDialog: MutableState<Boolean>
) {
    var sliderDifficultyPosition by rememberSaveable {
        val ordinal = viewModel.gamePreferences.getGameDifficultyPref().ordinal
        mutableFloatStateOf((ordinal.plus(1)).toFloat())
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
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .padding(horizontal = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.game_difficulty_dialog_title),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = sliderDifficultyPosition,
                onValueChange = { sliderDifficultyPosition = it },
                valueRange = 1.0f..3.0f,
                steps = 1,
                onValueChangeFinished = {
                    // Immediately make changes to preferences with updated slider position.
                    viewModel.updatePlayerChosenDifficulty(sliderDifficultyPosition)
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colors.primary,
                    activeTrackColor = MaterialTheme.colors.primary
                ),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Updated the text value from previously player chosen game difficulty preferences.
            Text(
                text = viewModel.gameDifficulty.name,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}