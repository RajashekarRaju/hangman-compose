package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_game_visual_simple
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_game_visual_traditional
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.game.visual.GameProgressVisualPreview
import com.developersbreach.hangman.ui.settings.GameProgressVisualOption
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineGameVisualSection(
    selectedGameProgressVisualPreference: GameProgressVisualPreference,
    onGameProgressVisualPreferenceChanged: (GameProgressVisualPreference) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = listOf(
        GameProgressVisualOption(
            gameProgressVisualPreference = GameProgressVisualPreference.TRADITIONAL_HANGMAN,
            labelRes = Res.string.settings_game_visual_traditional,
        ),
        GameProgressVisualOption(
            gameProgressVisualPreference = GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
            labelRes = Res.string.settings_game_visual_simple,
        ),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
    ) {
        options.forEachIndexed { index, option ->
            GameProgressVisualOptionRow(
                option = option,
                selected = selectedGameProgressVisualPreference == option.gameProgressVisualPreference,
                onClick = {
                    onGameProgressVisualPreferenceChanged(option.gameProgressVisualPreference)
                },
                seed = 4700 + index,
            )
        }
    }
}

@Composable
private fun GameProgressVisualOptionRow(
    option: GameProgressVisualOption,
    selected: Boolean,
    onClick: () -> Unit,
    seed: Int,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 10.dp, vertical = 10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            CreepyRadioButton(
                selected = selected,
                seed = seed + 310,
            )
            BodyLargeText(
                text = stringResource(option.labelRes),
                color = if (selected) {
                    HangmanTheme.colorScheme.secondary
                } else {
                    HangmanTheme.colorScheme.primary.copy(alpha = 0.88f)
                },
            )
        }
        GameProgressVisualPreview(
            gameProgressVisualPreference = option.gameProgressVisualPreference,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}