package com.developersbreach.hangman.ui.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.achievements.generated.resources.Res
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_summary_coins
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_summary_locked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_summary_progress
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AchievementsSummaryCard(
    summary: AchievementsSummary,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .creepyOutline(
                seed = 2201,
                threshold = 0.06f,
                forceRectangular = true,
                fillColor = HangmanTheme.colorScheme.surfaceContainer.copy(alpha = 0.55f),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.35f),
                strokeWidthFactor = 0.03f,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        TitleMediumText(
            text = stringResource(
                Res.string.achievements_summary_progress,
                summary.unlockedCount,
                summary.totalCount,
            ),
            color = HangmanTheme.colorScheme.primary,
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            SummaryChip(
                text = stringResource(Res.string.achievements_summary_locked, summary.lockedCount),
                fillColor = HangmanTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.62f),
            )
            SummaryChip(
                text = stringResource(Res.string.achievements_summary_coins, summary.totalCoins),
                fillColor = HangmanTheme.colorScheme.tertiary.copy(alpha = 0.16f),
            )
        }
    }
}

@Composable
private fun SummaryChip(
    text: String,
    fillColor: Color,
) {
    BodyLargeText(
        text = text,
        color = HangmanTheme.colorScheme.onSurface,
        modifier = Modifier
            .creepyOutline(
                seed = text.hashCode(),
                threshold = 0.04f,
                forceRectangular = true,
                fillColor = fillColor,
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.26f),
                strokeWidthFactor = 0.025f,
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
    )
}
