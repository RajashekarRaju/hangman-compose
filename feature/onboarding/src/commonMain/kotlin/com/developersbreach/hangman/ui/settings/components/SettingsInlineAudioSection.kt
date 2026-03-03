package com.developersbreach.hangman.ui.settings.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_audio_background_music
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_audio_off
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_audio_on
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_audio_sound_effects
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineAudioSection(
    isBackgroundMusicEnabled: Boolean,
    isSoundEffectsEnabled: Boolean,
    onBackgroundMusicChanged: (Boolean) -> Unit,
    onSoundEffectsChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        SettingsToggleGroup(
            title = Res.string.settings_audio_background_music,
            isEnabled = isBackgroundMusicEnabled,
            onChanged = onBackgroundMusicChanged,
        )
        SettingsToggleGroup(
            title = Res.string.settings_audio_sound_effects,
            isEnabled = isSoundEffectsEnabled,
            onChanged = onSoundEffectsChanged,
        )
    }
}

@Composable
private fun SettingsToggleGroup(
    title: StringResource,
    isEnabled: Boolean,
    onChanged: (Boolean) -> Unit,
) {
    BodyLargeText(
        text = stringResource(title),
        color = HangmanTheme.colorScheme.secondary,
        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .selectableGroup(),
    ) {
        SettingsToggleRow(
            label = Res.string.settings_audio_on,
            selected = isEnabled,
            onClick = { onChanged(true) },
            seed = 3701,
        )
        SettingsToggleRow(
            label = Res.string.settings_audio_off,
            selected = !isEnabled,
            onClick = { onChanged(false) },
            seed = 3702,
        )
    }
}

@Composable
private fun SettingsToggleRow(
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
