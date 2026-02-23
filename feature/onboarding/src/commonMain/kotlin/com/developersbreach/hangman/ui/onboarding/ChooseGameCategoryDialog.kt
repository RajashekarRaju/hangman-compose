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
import com.developersbreach.game.core.GameCategory
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_category_dialog_title
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.CreepyRadioButton
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChooseGameCategoryDialog(
    gameCategory: GameCategory,
    updatePlayerChosenCategory: (GameCategory) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val options = categoryOptions()

    HangmanDialog(
        onDismissRequest = onDismissRequest,
        seed = 811,
        threshold = 0.10f,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(60.dp)
    ) {
        TitleMediumText(
            text = stringResource(Res.string.onboarding_category_dialog_title),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.selectableGroup(),
            horizontalAlignment = Alignment.Start
        ) {
            options.forEach { category ->
                ItemCategoryRow(
                    gameCategory = category,
                    selectedGameCategory = gameCategory,
                    updatePlayerChosenCategory = updatePlayerChosenCategory
                )
            }
        }
    }
}

@Composable
private fun ItemCategoryRow(
    gameCategory: Category,
    selectedGameCategory: GameCategory,
    updatePlayerChosenCategory: (GameCategory) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = gameCategory.category == selectedGameCategory,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = { updatePlayerChosenCategory(gameCategory.category) }
            )
    ) {
        CreepyRadioButton(
            selected = gameCategory.category == selectedGameCategory,
            seed = gameCategory.category.ordinal * 41
        )

        BodyLargeText(
            text = gameCategory.categoryName,
            modifier = Modifier.padding(start = 20.dp),
            color = HangmanTheme.colorScheme.onSurface
        )
    }
}