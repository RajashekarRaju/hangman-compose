package com.hangman.hangman.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowExitGameModalSheet(
    navigateUp: () -> Unit,
    modalSheetState: ModalBottomSheetState
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(horizontal = 40.dp, vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Exit Game ?",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary.copy(0.75f),
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
                text = "Exit",
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
            },
        ) {
            Text(
                text = "Keep Playing",
                letterSpacing = 2.sp,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}