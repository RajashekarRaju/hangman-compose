package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.developersbreach.game.core.GameCategory
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_category_dialog_title
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChooseGameCategoryDialog(
    openGameCategoryDialog: MutableState<Boolean>,
    gameCategory: GameCategory,
    updatePlayerChosenCategory: (Int) -> Unit
) {
    val options = categoryOptions()
    val (selectedCategory, onCategorySelected) = remember {
        mutableIntStateOf(options[gameCategory.ordinal].categoryId)
    }

    Dialog(
        onDismissRequest = {
            openGameCategoryDialog.value = !openGameCategoryDialog.value
        }
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = HangmanTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(40.dp)
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
                        selectedGameCategory = selectedCategory,
                        onGameCategorySelected = onCategorySelected,
                        updatePlayerChosenCategory = updatePlayerChosenCategory
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemCategoryRow(
    gameCategory: Category,
    selectedGameCategory: Int,
    onGameCategorySelected: (Int) -> Unit,
    updatePlayerChosenCategory: (Int) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = gameCategory.categoryId == selectedGameCategory,
                role = Role.RadioButton,
                indication = null,
                interactionSource = interactionSource,
                onClick = {
                    onGameCategorySelected(gameCategory.categoryId)
                    updatePlayerChosenCategory(gameCategory.categoryId)
                }
            )
    ) {
        RadioButton(
            selected = gameCategory.categoryId == selectedGameCategory,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                unselectedColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.50f),
                selectedColor = HangmanTheme.colorScheme.primary
            )
        )

        BodyLargeText(
            text = gameCategory.categoryName,
            modifier = Modifier.padding(start = 20.dp),
            color = HangmanTheme.colorScheme.onSurface
        )
    }
}