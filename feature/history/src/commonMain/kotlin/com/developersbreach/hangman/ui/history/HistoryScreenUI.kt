package com.developersbreach.hangman.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.history.generated.resources.Res
import com.developersbreach.hangman.feature.history.generated.resources.history_category_companies
import com.developersbreach.hangman.feature.history.generated.resources.history_category_countries
import com.developersbreach.hangman.feature.history.generated.resources.history_category_languages
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_delete_item
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_easy
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_hard
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_medium
import com.developersbreach.hangman.feature.history.generated.resources.history_empty_state
import com.developersbreach.hangman.feature.history.generated.resources.history_summary_lost
import com.developersbreach.hangman.feature.history.generated.resources.history_summary_won
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.BodyMediumText
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.HangmanCircularProgress
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanScaffold
import com.developersbreach.hangman.ui.components.HangmanSwipeToDismissItem
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.components.rememberCreepyPhase
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryUiState.HistoryScreenUI(
    onEvent: (HistoryEvent) -> Unit
) {
    HangmanScaffold(
        topBar = {
            HistoryAppBar(
                navigateUp = { onEvent(HistoryEvent.NavigateUpClicked) },
                showDeleteIconInAppBar = showDeleteIconInAppBar,
                deleteAllGameHistoryData = { onEvent(HistoryEvent.DeleteAllClicked) }
            )
        },
        containerColor = HangmanTheme.colorScheme.background,
        contentColor = HangmanTheme.colorScheme.onBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(historyBackgroundGradient())
                .padding(paddingValues)
        ) {
            AnimatedEnter {
                HistoryScreenContent(
                    gameHistoryList = gameHistoryList,
                    onClickDeleteSelectedGameHistory = { history ->
                        onEvent(HistoryEvent.DeleteHistoryItemClicked(history))
                    }
                )
            }

            if (gameHistoryList.isEmpty()) {
                AnimatedEnter(offsetY = 20.dp) {
                    ShowEmptyHistoryMessage(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 24.dp, vertical = 48.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun historyBackgroundGradient(): Brush {
    return Brush.verticalGradient(
        colors = listOf(
            HangmanTheme.colorScheme.surface,
            HangmanTheme.colorScheme.background,
            HangmanTheme.colorScheme.surfaceContainerLow
        )
    )
}

@Composable
private fun HistoryScreenContent(
    gameHistoryList: List<HistoryListItemUiState>,
    onClickDeleteSelectedGameHistory: (history: HistoryRecord) -> Unit
) {
    val listCreepyPhase = rememberCreepyPhase(durationMillis = 3900)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = gameHistoryList,
            key = { it.history.gameId }
        ) { item ->
            val history = item.history
            HangmanSwipeToDismissItem(
                dismissKey = history.gameId,
                onDismissed = { onClickDeleteSelectedGameHistory(history) },
            ) {
                AnimatedEnter(offsetY = 12.dp) {
                    ItemGameHistory(
                        history = history,
                        levelProgress = item.levelProgress,
                        onDeleteClick = { onClickDeleteSelectedGameHistory(history) },
                        phase = listCreepyPhase,
                    )
                }
            }
        }
    }
}

@Composable
private fun ItemGameHistory(
    history: HistoryRecord,
    levelProgress: Float,
    onDeleteClick: () -> Unit,
    phase: Float,
) {
    val summary = if (history.gameSummary) {
        stringResource(Res.string.history_summary_won)
    } else {
        stringResource(Res.string.history_summary_lost)
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .background(HangmanTheme.colorScheme.surfaceContainer)
            .creepyOutline(
                seed = history.gameId.hashCode(),
                threshold = 0.09f,
                fillColor = Color.Transparent,
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.32f),
                phase = phase + 0.35f,
            )
            .padding(28.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LevelProgress(level = history.gameLevel, progress = levelProgress)
                Column {
                    TitleLargeText(
                        text = summary,
                        color = HangmanTheme.colorScheme.primary
                    )
                    BodyLargeText(
                        text = stringResource(history.gameDifficulty.labelRes()),
                        color = HangmanTheme.colorScheme.onSurface
                    )
                }
            }

            HangmanIconActionButton(
                onClick = onDeleteClick,
                seed = history.gameId.hashCode() + 11,
                threshold = 0.16f,
                fillColor = Color.Transparent,
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.42f),
            ) {
                HangmanIcon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(Res.string.history_cd_delete_item),
                    tint = HangmanTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HangmanDivider(
            modifier = Modifier
                .clip(HangmanTheme.shapes.small)
                .fillMaxWidth(),
            seed = history.gameId.hashCode() + 12,
            threshold = 0.06f,
            outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.24f),
            thickness = 1.dp,
            color = HangmanTheme.colorScheme.outlineVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            BodyMediumText(
                text = stringResource(history.gameCategory.labelRes()),
                letterSpacing = 2.sp,
                color = HangmanTheme.colorScheme.secondary
            )

            Column(horizontalAlignment = Alignment.End) {
                BodySmallText(
                    text = history.gamePlayedTime,
                    color = HangmanTheme.colorScheme.onSurfaceVariant
                )
                BodySmallText(
                    text = history.gamePlayedDate,
                    color = HangmanTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LevelProgress(level: Int, progress: Float) {
    Box(
        modifier = Modifier.size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        HangmanCircularProgress(
            progress = 1f,
            modifier = Modifier.fillMaxSize(),
            indicatorColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.22f),
            strokeWidth = 2.dp,
            trackColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.10f),
            strokeCap = StrokeCap.Round,
        )

        HangmanCircularProgress(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
            indicatorColor = HangmanTheme.colorScheme.primary,
            strokeWidth = 2.dp,
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Round,
        )

        TitleMediumText(
            text = level.toString(),
            color = HangmanTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ShowEmptyHistoryMessage(modifier: Modifier = Modifier) {
    TitleLargeText(
        text = stringResource(Res.string.history_empty_state),
        textAlign = TextAlign.Center,
        color = HangmanTheme.colorScheme.onBackground.copy(alpha = 0.75f),
        modifier = modifier
    )
}

private fun GameDifficulty.labelRes(): StringResource {
    return when (this) {
        GameDifficulty.EASY -> Res.string.history_difficulty_easy
        GameDifficulty.MEDIUM -> Res.string.history_difficulty_medium
        GameDifficulty.HARD -> Res.string.history_difficulty_hard
    }
}

private fun GameCategory.labelRes(): StringResource {
    return when (this) {
        GameCategory.COUNTRIES -> Res.string.history_category_countries
        GameCategory.LANGUAGES -> Res.string.history_category_languages
        GameCategory.COMPANIES -> Res.string.history_category_companies
    }
}