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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.feature.history.generated.resources.Res
import com.developersbreach.hangman.feature.history.generated.resources.history_cd_delete_item
import com.developersbreach.hangman.feature.history.generated.resources.history_empty_state
import com.developersbreach.hangman.feature.history.generated.resources.history_summary_lost
import com.developersbreach.hangman.feature.history.generated.resources.history_summary_won
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.BodyMediumText
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryScreenUI(
    uiState: HistoryUiState,
    onEvent: (HistoryEvent) -> Unit
) {
    Surface(color = HangmanTheme.colorScheme.background) {
        Scaffold(
            topBar = {
                HistoryAppBar(
                    navigateUp = { onEvent(HistoryEvent.NavigateUpClicked) },
                    showDeleteIconInAppBar = uiState.showDeleteIconInAppBar,
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
                HistoryScreenContent(
                    gameHistoryList = uiState.gameHistoryList,
                    onClickDeleteSelectedGameHistory = { history ->
                        onEvent(HistoryEvent.DeleteHistoryItemClicked(history))
                    }
                )

                if (uiState.gameHistoryList.isEmpty()) {
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
    gameHistoryList: List<HistoryRecord>,
    onClickDeleteSelectedGameHistory: (history: HistoryRecord) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = gameHistoryList.reversed(),
            key = { it.gameId }
        ) { history ->
            ItemGameHistory(
                history = history,
                onDeleteClick = { onClickDeleteSelectedGameHistory(history) }
            )
        }
    }
}

@Composable
private fun ItemGameHistory(
    history: HistoryRecord,
    onDeleteClick: () -> Unit
) {
    val summary = if (history.gameSummary) {
        stringResource(Res.string.history_summary_won)
    } else {
        stringResource(Res.string.history_summary_lost)
    }
    val levelProgress = historyLevelProgress(history.gameLevel)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = HangmanTheme.colorScheme.surfaceContainer,
            contentColor = HangmanTheme.colorScheme.onSurface
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
                            text = history.gameDifficulty.name,
                            color = HangmanTheme.colorScheme.onSurface
                        )
                    }
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(Res.string.history_cd_delete_item),
                        tint = HangmanTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier
                    .clip(HangmanTheme.shapes.small)
                    .fillMaxWidth(),
                thickness = DividerDefaults.Thickness,
                color = HangmanTheme.colorScheme.outlineVariant,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BodyMediumText(
                    text = history.gameCategory.name,
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
}

@Composable
private fun LevelProgress(level: Int, progress: Float) {
    Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.22f),
            strokeWidth = 2.dp,
            trackColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.10f),
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = HangmanTheme.colorScheme.primary,
            strokeWidth = 2.dp,
            trackColor = androidx.compose.ui.graphics.Color.Transparent,
            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
        )

        TitleMediumText(
            text = level.toString(),
            color = HangmanTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

private fun historyLevelProgress(level: Int): Float {
    if (level <= 0) return 0f
    return (level.coerceAtMost(5)) / 5f
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

@Preview
@Composable
private fun HistoryScreenUIPreview() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.ORIGINAL)
    ) {
        HistoryScreenUI(
            uiState = HistoryUiState(
                gameHistoryList = listOf(
                    HistoryRecord(
                        gameId = "atomorum",
                        gameScore = 2913,
                        gameLevel = 4,
                        gameDifficulty = GameDifficulty.HARD,
                        gameCategory = GameCategory.LANGUAGES,
                        gameSummary = false,
                        gamePlayedTime = "09:41 PM",
                        gamePlayedDate = "14 Feb"
                    )
                )
            ),
            onEvent = {}
        )
    }
}
