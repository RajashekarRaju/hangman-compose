package com.hangman.hangman.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun AdjustGameDifficultyDialog(
    viewModel: OnBoardingViewModel,
    openGameDifficultyDialog: MutableState<Boolean>
) {
    var sliderDifficultyPosition by rememberSaveable { mutableStateOf(1.0f) }

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
                text = "Difficulty",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Slider(
                value = sliderDifficultyPosition,
                onValueChange = { sliderDifficultyPosition = it },
                valueRange = 1.0f..3.0f,
                steps = 1,
                onValueChangeFinished = {
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

            Text(
                text = viewModel.difficultyValueText.name,
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}