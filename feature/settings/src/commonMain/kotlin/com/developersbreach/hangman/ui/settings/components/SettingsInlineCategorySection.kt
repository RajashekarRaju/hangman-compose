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
import com.developersbreach.game.core.GameCategory
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.settings.labelRes
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SettingsInlineCategorySection(
    categories: List<GameCategory>,
    selectedCategory: GameCategory,
    onCategoryChanged: (GameCategory) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalAlignment = Alignment.Start,
    ) {
        categories.forEach { category ->
            SettingsCategoryRow(
                category = category,
                selectedCategory = selectedCategory,
                onCategoryChanged = onCategoryChanged,
            )
        }
    }
}

@Composable
private fun SettingsCategoryRow(
    category: GameCategory,
    selectedCategory: GameCategory,
    onCategoryChanged: (GameCategory) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = category == selectedCategory,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = { onCategoryChanged(category) },
            ),
    ) {
        CreepyRadioButton(
            selected = category == selectedCategory,
            seed = category.ordinal * 41,
        )

        BodyLargeText(
            text = stringResource(category.labelRes()),
            modifier = Modifier.padding(start = 20.dp),
            color = HangmanTheme.colorScheme.onSurface,
        )
    }
}