package com.developersbreach.hangman.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes

@Composable
fun ThemePaletteDropdown(
    selectedPaletteId: ThemePaletteId,
    expanded: Boolean,
    onExpandRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    onPaletteChanged: (ThemePaletteId) -> Unit,
) {
    val selectedPalette = ThemePalettes.byId(selectedPaletteId)

    Box {
        HangmanIconActionButton(
            onClick = onExpandRequest,
            seed = 703,
            size = 42,
            threshold = 0.14f,
            backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(selectedPalette.previewColor, CircleShape)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            containerColor = HangmanTheme.colorScheme.surfaceContainer,
        ) {
            ThemePalettes.all.forEach { palette ->
                DropdownMenuItem(
                    text = {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(palette.previewColor, CircleShape)
                        )
                    },
                    onClick = { onPaletteChanged(palette.id) },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        }
    }
}
