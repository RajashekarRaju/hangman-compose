package com.developersbreach.hangman.ui.achievements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.ui.preview.HangmanScreenPreviews
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AchievementsScreen(
    navigateUp: () -> Unit,
    viewModel: AchievementsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                AchievementsEffect.NavigateUp -> navigateUp()
            }
        }
    }

    uiState.AchievementsScreenUI(onEvent = viewModel::onEvent)
}

@HangmanScreenPreviews
@Composable
private fun AchievementsScreenPreview() {
    val items = AchievementCatalog.definitions.map { definition ->
        val progress = definition.initialProgress()
        AchievementItemUiState(
            id = definition.id,
            group = definition.group,
            title = definition.title,
            description = definition.description,
            isUnlocked = progress.isUnlocked,
            progressCurrent = progress.progressCurrent,
            progressTarget = progress.progressTarget,
            unlockedAtLabel = progress.unlockedAtEpochMillis?.let(::formatEpochMillis),
        )
    }

    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.INSANE_RED),
    ) {
        AchievementsUiState(items).AchievementsScreenUI(onEvent = {})
    }
}
