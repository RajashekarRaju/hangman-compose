package com.developersbreach.hangman.ui.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res.string
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_dialog_body
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_dialog_step_number
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_dialog_title
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_intro_body
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_intro_title
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_title
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_timer_body
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_timer_title
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_title
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.game.traditional.TraditionalHangmanVisual
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
@OptIn(ExperimentalLayoutApi::class)
internal fun GameGuideContent(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        GameGuideTopBar(onClose = onClose)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            item { GuideRulesSection() }

            item { SectionDivider() }

            item {
                GuideTextSection(
                    title = stringResource(Res.string.game_guide_intro_title),
                    body = stringResource(Res.string.game_guide_intro_body),
                )
            }

            item { SectionDivider() }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    stageSnapshots.forEach { snapshot ->
                        GuideSnapshotBlock(snapshot = snapshot)
                    }
                }
            }

            item { SectionDivider() }

            item {
                GuideTextSection(
                    title = stringResource(Res.string.game_guide_timer_title),
                    body = stringResource(Res.string.game_guide_timer_body),
                )
            }

            item {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    GuideTimerBlock(levelTimeProgress = 1f)
                    GuideTimerBlock(levelTimeProgress = 0.5f)
                    GuideTimerBlock(levelTimeProgress = 0f)
                }
            }
        }
    }
}

@Composable
private fun GameGuideTopBar(
    onClose: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        TitleLargeText(
            text = stringResource(Res.string.game_guide_title).uppercase(),
            color = HangmanTheme.colorScheme.secondary,
        )
        HangmanIconActionButton(
            onClick = onClose,
            seed = 1192,
            size = 42,
            threshold = 0.12f,
            backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.07f),
        ) {
            HangmanIcon(
                imageVector = Icons.Filled.Close,
                contentDescription = null,
                tint = HangmanTheme.colorScheme.primary.copy(alpha = 0.82f),
            )
        }
    }
}

@Composable
private fun GuideRulesSection() {
    val instructions = stringResource(string.instructions_dialog_body)
        .split('\n')
        .map { text -> text.trim() }
        .filter { text -> text.isNotEmpty() }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        TitleMediumText(
            text = stringResource(string.instructions_dialog_title),
            color = HangmanTheme.colorScheme.secondary
        )
        instructions.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                BodyLargeText(
                    text = stringResource(
                        string.instructions_dialog_step_number,
                        index + 1,
                    ),
                    color = HangmanTheme.colorScheme.secondary,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.width(40.dp),
                )
                BodySmallText(
                    text = item,
                    color = HangmanTheme.colorScheme.primary.copy(alpha = 0.82f),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun GuideSnapshotBlock(
    snapshot: GuideSnapshot,
) {
    val dividerColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.35f)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(292.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            VerticalDivider(
                modifier = Modifier.height(240.dp),
                color = dividerColor,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                TraditionalHangmanVisual(
                    attemptsLeftToGuess = snapshot.attemptsLeft,
                    levelTimeProgress = 1f,
                    playStageIntroAnimation = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp),
                )
                LabelLargeText(
                    text = stringResource(
                        Res.string.game_guide_snapshot_title,
                        snapshot.stage,
                        MAX_ATTEMPTS_PER_LEVEL,
                    ),
                    color = HangmanTheme.colorScheme.secondary,
                )
                BodySmallText(
                    text = stringResource(snapshot.descriptionRes),
                    color = HangmanTheme.colorScheme.primary.copy(alpha = 0.82f),
                )
            }
        }
    }
}

@Composable
private fun GuideTimerBlock(
    levelTimeProgress: Float,
) {
    val timerPercent = (levelTimeProgress * 100).toInt().coerceIn(0, 100)
    val dividerColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.35f)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(292.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            VerticalDivider(
                modifier = Modifier.height(232.dp),
                color = dividerColor,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                TraditionalHangmanVisual(
                    attemptsLeftToGuess = MAX_ATTEMPTS_PER_LEVEL,
                    levelTimeProgress = levelTimeProgress,
                    playStageIntroAnimation = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(176.dp),
                )
                TitleMediumText(
                    text = "Timer $timerPercent%",
                    color = HangmanTheme.colorScheme.secondary,
                )
            }
        }
    }
}

@Composable
private fun SectionDivider() {
    HangmanDivider(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp),
        useCreepyOutline = false,
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.26f),
        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.30f),
    )
}

@Composable
private fun VerticalDivider(
    modifier: Modifier,
    color: Color,
) {
    Box(
        modifier = modifier
            .width(1.dp)
            .background(color),
    )
}

@Composable
private fun GuideTextSection(
    title: String,
    body: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        TitleMediumText(
            text = title,
            color = HangmanTheme.colorScheme.secondary,
        )
        BodySmallText(
            text = body,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.86f),
        )
    }
}