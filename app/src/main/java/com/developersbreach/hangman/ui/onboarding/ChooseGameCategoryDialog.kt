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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.developersbreach.hangman.R

/**
 * These categories will be shown in dialog, player can choose between any game category
 * from radio button.
 */
data class Category(
    val categoryId: Int,
    val categoryName: String
)

val categoryOptions = listOf(
    Category(0, "Countries"),
    Category(1, "Languages"),
    Category(2, "Companies")
)

// Dialog for adjusting game difficulty from OnBoarding screen.
@Composable
fun ChooseGameCategoryDialog(
    viewModel: OnBoardingViewModel,
    openGameCategoryDialog: MutableState<Boolean>
) {
    // Get last saved game category player preferences.
    val gameCategory = viewModel.gameCategoryPreferences.getGameCategoryPref()
    // From last preferences, if player does not choose category again, we will load the last saved
    // settings for existing games.
    val (selectedCategory, onCategorySelected) = remember {
        mutableStateOf(categoryOptions[gameCategory.ordinal].categoryId)
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
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .padding(40.dp)
        ) {
            Text(
                text = stringResource(R.string.game_category_dialog_title),
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier.selectableGroup(),
                horizontalAlignment = Alignment.Start
            ) {
                categoryOptions.forEach { categoryName ->
                    ItemCategoryRow(
                        gameCategory = categoryName,
                        selectedGameCategory = selectedCategory,
                        onGameCategorySelected = onCategorySelected,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

/**
 * Row item for each radio button for selecting game category.
 */
@Composable
private fun ItemCategoryRow(
    gameCategory: Category,
    selectedGameCategory: Int,
    onGameCategorySelected: (Int) -> Unit,
    viewModel: OnBoardingViewModel
) {
    // Removed default ripple effect.
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
                    // Selects player clicked radio button game category.
                    onGameCategorySelected(gameCategory.categoryId)
                    // Immediately updated the preferences too save player category later.
                    viewModel.updatePlayerChosenCategory(gameCategory.categoryId)
                }
            )
    ) {
        RadioButton(
            selected = (gameCategory.categoryId == selectedGameCategory),
            onClick = null,
            colors = RadioButtonDefaults.colors(
                unselectedColor = MaterialTheme.colors.primary.copy(0.50f),
                selectedColor = MaterialTheme.colors.primary
            )
        )

        Text(
            text = gameCategory.categoryName,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}