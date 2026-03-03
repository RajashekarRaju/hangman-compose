package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_language_dialog_title
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChooseLanguageDialog(
    languages: List<AppLanguage>,
    selectedLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit,
    onDismissRequest: () -> Unit,
) {
    HangmanDialog(
        onDismissRequest = onDismissRequest,
        seed = 813,
        threshold = 0.10f,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(60.dp),
    ) {
        TitleMediumText(
            text = stringResource(Res.string.onboarding_language_dialog_title),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.selectableGroup(),
            horizontalAlignment = Alignment.Start,
        ) {
            languages.forEach { language ->
                LanguageRow(
                    language = language,
                    selectedLanguage = selectedLanguage,
                    onLanguageChanged = onLanguageChanged,
                )
            }
        }
    }
}

@Composable
private fun LanguageRow(
    language: AppLanguage,
    selectedLanguage: AppLanguage,
    onLanguageChanged: (AppLanguage) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = language == selectedLanguage,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = { onLanguageChanged(language) },
            ),
    ) {
        CreepyRadioButton(
            selected = language == selectedLanguage,
            seed = language.ordinal * 53,
        )

        BodyLargeText(
            text = language.nativeName,
            modifier = Modifier.padding(start = 20.dp),
            color = HangmanTheme.colorScheme.onSurface,
        )
    }
}
