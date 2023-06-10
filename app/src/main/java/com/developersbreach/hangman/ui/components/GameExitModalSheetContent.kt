package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Modal sheet shows up when hits up navigation button.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowExitGameModalSheet(
    navigateUp: () -> Unit,
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    Column(
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.modal_sheet_exit_game_title),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary.copy(0.75f)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    modalSheetState.hide()
                }
                navigateUp()
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.primary.copy(0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.modal_sheet_positive_title),
                letterSpacing = 4.sp,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            onClick = {
                coroutineScope.launch {
                    modalSheetState.hide()
                }
            }
        ) {
            Text(
                text = stringResource(id = R.string.modal_sheet_negative_title),
                letterSpacing = 2.sp,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}