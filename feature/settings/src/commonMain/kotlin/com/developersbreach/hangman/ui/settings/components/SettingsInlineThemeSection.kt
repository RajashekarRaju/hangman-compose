package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.mainmenu.generated.resources.Res
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_theme_mode_dark
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_theme_mode_light
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_theme_mode_title
import com.developersbreach.hangman.repository.ThemeMode
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineThemeSection(
    selectedThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    selectedPaletteId: ThemePaletteId,
    onPaletteChanged: (ThemePaletteId) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        BodyLargeText(
            text = stringResource(Res.string.settings_theme_mode_title),
            color = HangmanTheme.colorScheme.secondary,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
        ) {
            ThemeModeRow(
                label = Res.string.settings_theme_mode_dark,
                selected = selectedThemeMode == ThemeMode.DARK,
                onClick = { onThemeModeChanged(ThemeMode.DARK) },
                seed = 3901,
            )
            ThemeModeRow(
                label = Res.string.settings_theme_mode_light,
                selected = selectedThemeMode == ThemeMode.LIGHT,
                onClick = { onThemeModeChanged(ThemeMode.LIGHT) },
                seed = 3902,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ThemePalettes.all.chunked(4).forEach { rowPalettes ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowPalettes.forEach { palette ->
                    val interactionSource = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .size(46.dp)
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
                                fillColor = palette.previewColor,
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

@Composable
private fun ThemeModeRow(
    label: StringResource,
    selected: Boolean,
    onClick: () -> Unit,
    seed: Int,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick,
            ),
    ) {
        CreepyRadioButton(selected = selected, seed = seed)
        BodyLargeText(
            text = stringResource(label),
            modifier = Modifier.padding(start = 20.dp),
            color = HangmanTheme.colorScheme.onSurface,
        )
    }
}
