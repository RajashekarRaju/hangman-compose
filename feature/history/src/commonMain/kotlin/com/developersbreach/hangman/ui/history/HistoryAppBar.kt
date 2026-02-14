package com.developersbreach.hangman.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.feature.history.generated.resources.Res
import com.developersbreach.hangman.feature.history.generated.resources.history_app_bar_title
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_delete_all
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_navigate_up
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryAppBar(
    navigateUp: () -> Unit,
    showDeleteIconInAppBar: Boolean,
    deleteAllGameHistoryData: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HangmanTheme.colorScheme.background,
            titleContentColor = HangmanTheme.colorScheme.onBackground,
            navigationIconContentColor = HangmanTheme.colorScheme.primary,
            actionIconContentColor = HangmanTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth(),
        title = {
            TitleMediumText(
                text = stringResource(Res.string.history_app_bar_title),
                color = HangmanTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(Res.string.history_cd_navigate_up)
                )
            }
        },
        actions = {
            if (showDeleteIconInAppBar) {
                IconButton(onClick = deleteAllGameHistoryData) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(Res.string.history_cd_delete_all)
                    )
                }
            }
        }
    )
}
