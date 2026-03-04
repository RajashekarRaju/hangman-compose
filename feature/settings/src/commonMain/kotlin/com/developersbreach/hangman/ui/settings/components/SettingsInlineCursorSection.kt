package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res as CommonUiRes
import com.developersbreach.hangman.feature.common.ui.generated.resources.cursor_bone
import com.developersbreach.hangman.feature.common.ui.generated.resources.cursor_demon
import com.developersbreach.hangman.feature.common.ui.generated.resources.cursor_hand
import com.developersbreach.hangman.feature.common.ui.generated.resources.cursor_hand_bones
import com.developersbreach.hangman.feature.common.ui.generated.resources.cursor_skull
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.settings.labelRes
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineCursorSection(
    availableCursorStyles: List<CursorStyle>,
    selectedCursorStyle: CursorStyle,
    onCursorStyleChanged: (CursorStyle) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
    ) {
        availableCursorStyles.forEach { cursorStyle ->
            SettingsCursorRow(
                cursorStyle = cursorStyle,
                selectedCursorStyle = selectedCursorStyle,
                onCursorStyleChanged = onCursorStyleChanged,
            )
        }
    }
}

@Composable
private fun SettingsCursorRow(
    cursorStyle: CursorStyle,
    selectedCursorStyle: CursorStyle,
    onCursorStyleChanged: (CursorStyle) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isSelected = selectedCursorStyle == cursorStyle
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = { onCursorStyleChanged(cursorStyle) },
            ),
    ) {
        CreepyRadioButton(
            selected = isSelected,
            seed = cursorStyle.ordinal * 67,
        )
        LabelLargeText(
            text = stringResource(cursorStyle.labelRes()),
            color = HangmanTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        CursorPreviewTile(
            cursorStyle = cursorStyle,
            isSelected = isSelected,
        )
    }
}

@Composable
private fun CursorPreviewTile(
    cursorStyle: CursorStyle,
    isSelected: Boolean,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .creepyOutline(
                seed = 1800 + cursorStyle.ordinal,
                threshold = 0.14f,
                fillColor = HangmanTheme.colorScheme.surface.copy(alpha = 0.24f),
                outlineColor = if (isSelected) {
                    HangmanTheme.colorScheme.secondary
                } else {
                    HangmanTheme.colorScheme.primary.copy(alpha = 0.46f)
                },
                strokeWidthFactor = 0.12f,
            ),
    ) {
        when (cursorStyle) {
            CursorStyle.DEFAULT -> {
                BodyLargeText(
                    text = "↖",
                    color = HangmanTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 2.dp),
                )
            }

            CursorStyle.SKULL -> {
                Image(
                    painter = painterResource(CommonUiRes.drawable.cursor_skull),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }

            CursorStyle.DEMON -> {
                Image(
                    painter = painterResource(CommonUiRes.drawable.cursor_demon),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }

            CursorStyle.HAND -> {
                Image(
                    painter = painterResource(CommonUiRes.drawable.cursor_hand),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }

            CursorStyle.HAND_BONES -> {
                Image(
                    painter = painterResource(CommonUiRes.drawable.cursor_hand_bones),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }

            CursorStyle.BONE -> {
                Image(
                    painter = painterResource(CommonUiRes.drawable.cursor_bone),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }
        }
    }
}
