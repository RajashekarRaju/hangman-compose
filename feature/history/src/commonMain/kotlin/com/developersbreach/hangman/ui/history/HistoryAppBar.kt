package com.developersbreach.hangman.ui.history

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.feature.history.generated.resources.Res
import com.developersbreach.hangman.feature.history.generated.resources.history_app_bar_title
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_delete_all
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_navigate_up
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanTopAppBar
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryAppBar(
    navigateUp: () -> Unit,
    showDeleteIconInAppBar: Boolean,
    deleteAllGameHistoryData: () -> Unit
) {
    HangmanTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            TitleMediumText(
                text = stringResource(Res.string.history_app_bar_title),
                color = HangmanTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            HangmanIconActionButton(
                onClick = navigateUp,
                seed = 1001,
                threshold = 0.12f,
            ) {
                HangmanIcon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(Res.string.history_cd_navigate_up)
                )
            }
        },
        actions = {
            if (showDeleteIconInAppBar) {
                HangmanIconActionButton(
                    onClick = deleteAllGameHistoryData,
                    seed = 1002,
                    threshold = 0.16f,
                ) {
                    HangmanIcon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(Res.string.history_cd_delete_all)
                    )
                }
            }
        }
    )
}
