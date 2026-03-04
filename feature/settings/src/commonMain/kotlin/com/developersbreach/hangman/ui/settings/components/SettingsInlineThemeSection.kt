package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes

@Composable
internal fun SettingsInlineThemeSection(
    selectedPaletteId: ThemePaletteId,
    onPaletteChanged: (ThemePaletteId) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        ThemePalettes.all.chunked(4).forEach { rowPalettes ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowPalettes.forEach { palette ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                color = palette.previewColor,
                                shape = CircleShape,
                            )
                            .selectable(
                                selected = palette.id == selectedPaletteId,
                                role = Role.RadioButton,
                                indication = null,
                                interactionSource = interactionSource,
                                onClick = { onPaletteChanged(palette.id) },
                            )
                            .creepyOutline(
                                seed = 980 + palette.id.ordinal,
                                threshold = if (palette.id == selectedPaletteId) 0.18f else 0.10f,
                                fillColor = Color.Transparent,
                                strokeWidthFactor = if (palette.id == selectedPaletteId) 0.16f else 0.12f,
                                outlineColor = when (palette.id) {
                                    selectedPaletteId -> HangmanTheme.colorScheme.secondary
                                    else -> HangmanTheme.colorScheme.primary.copy(alpha = 0.45f)
                                },
                            ),
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}