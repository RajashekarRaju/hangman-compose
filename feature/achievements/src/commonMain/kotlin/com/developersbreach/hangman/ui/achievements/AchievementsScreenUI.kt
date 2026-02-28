package com.developersbreach.hangman.ui.achievements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.achievements.AchievementGroup
import com.developersbreach.hangman.feature.achievements.generated.resources.Res
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_back
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_collapse_group
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_expand_group
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_empty
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_title
import com.developersbreach.hangman.ui.components.AnimatedEnter
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanScaffold
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AchievementsUiState.AchievementsScreenUI(
    onEvent: (AchievementsEvent) -> Unit,
) {
    HangmanScaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                HangmanIconActionButton(
                    onClick = { onEvent(AchievementsEvent.NavigateUpClicked) },
                    seed = 1201,
                    threshold = 0.13f,
                    backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.08f),
                ) {
                    HangmanIcon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(Res.string.achievements_cd_back),
                        tint = HangmanTheme.colorScheme.primary,
                    )
                }
                TitleLargeText(
                    text = stringResource(Res.string.achievements_title),
                    color = HangmanTheme.colorScheme.primary,
                )
            }
        },
        containerColor = HangmanTheme.colorScheme.background,
        contentColor = HangmanTheme.colorScheme.onBackground,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            HangmanTheme.colorScheme.surface,
                            HangmanTheme.colorScheme.background,
                        )
                    )
                )
                .padding(paddingValues),
        ) {
            if (items.isEmpty()) {
                NoAchievementsAvailable()
                return@Box
            }

            AchievementsGridUI(onEvent = onEvent)
        }
    }

    selectedAchievement?.let { details ->
        AchievementDetailsDialog(
            details = details,
            onDismissRequest = { onEvent(AchievementsEvent.AchievementDetailsDismissed) },
        )
    }
}

@Composable
private fun AchievementsUiState.AchievementsGridUI(
    onEvent: (AchievementsEvent) -> Unit,
) {
    val groupedItems = AchievementGroup.entries.mapNotNull { group ->
        val groupItems = items.filter { it.group == group }
        groupItems.takeIf { it.isNotEmpty() }?.let {
            AchievementSection(group, it)
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 260.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(
            key = "summary",
            span = { GridItemSpan(maxLineSpan) },
        ) {
            AchievementsSummaryCard(summary = summary)
        }

        groupedItems.forEach { section ->
            val isCollapsed = collapsedGroups.contains(section.group)
            item(
                key = "section-${section.group.name}",
                span = { GridItemSpan(maxLineSpan) },
            ) {
                GroupHeader(
                    group = section.group,
                    style = section.group.style(),
                    isCollapsed = isCollapsed,
                    onToggle = {
                        onEvent(AchievementsEvent.GroupToggleClicked(section.group))
                    },
                )
            }
            itemsIndexed(
                items = section.items,
                key = { _, item -> item.id.name },
            ) { index, item ->
                AnimatedVisibility(
                    visible = !isCollapsed,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    AnimatedEnter(offsetY = 10.dp) {
                        item.AchievementItem(
                            style = section.group.style(),
                            showPaleBackground = index % 2 == 0,
                            onClick = { onEvent(AchievementsEvent.AchievementClicked(item.id)) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.NoAchievementsAvailable() {
    TitleMediumText(
        text = stringResource(Res.string.achievements_empty),
        textAlign = TextAlign.Center,
        color = HangmanTheme.colorScheme.onBackground.copy(alpha = 0.8f),
        modifier = Modifier
            .align(Alignment.Center)
            .padding(horizontal = 24.dp),
    )
    return
}

@Composable
private fun GroupHeader(
    group: AchievementGroup,
    style: AchievementGroupStyle,
    isCollapsed: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(style.headerBackground)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val groupTitle = stringResource(group.titleRes())
        TitleMediumText(
            text = groupTitle,
            color = style.accent,
        )
        HangmanIcon(
            imageVector = if (isCollapsed) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
            contentDescription = when {
                isCollapsed -> stringResource(Res.string.achievements_cd_expand_group, groupTitle)
                else -> stringResource(Res.string.achievements_cd_collapse_group, groupTitle)
            },
            tint = style.accent,
            modifier = Modifier.clickable(onClick = onToggle).padding(4.dp).size(28.dp),
        )
    }
}

@Composable
private fun AchievementGroup.style(): AchievementGroupStyle {
    val scheme = HangmanTheme.colorScheme
    return when (this) {
        AchievementGroup.PROGRESS -> AchievementGroupStyle(
            accent = scheme.primary,
            background = scheme.surfaceContainer.copy(alpha = 0.28f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.42f),
            headerBackground = scheme.primary.copy(alpha = 0.08f),
        )
        AchievementGroup.SKILL -> AchievementGroupStyle(
            accent = scheme.secondary,
            background = scheme.surfaceContainer.copy(alpha = 0.24f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.34f),
            headerBackground = scheme.secondary.copy(alpha = 0.08f),
        )
        AchievementGroup.COLLECTION -> AchievementGroupStyle(
            accent = scheme.tertiary,
            background = scheme.surfaceContainer.copy(alpha = 0.22f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.30f),
            headerBackground = scheme.tertiary.copy(alpha = 0.08f),
        )
        AchievementGroup.ENDURANCE -> AchievementGroupStyle(
            accent = scheme.primary.copy(alpha = 0.86f),
            background = scheme.surfaceContainer.copy(alpha = 0.2f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.28f),
            headerBackground = scheme.primary.copy(alpha = 0.06f),
        )
        AchievementGroup.HINT_DISCIPLINE -> AchievementGroupStyle(
            accent = scheme.secondary.copy(alpha = 0.9f),
            background = scheme.surfaceContainer.copy(alpha = 0.2f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.28f),
            headerBackground = scheme.secondary.copy(alpha = 0.06f),
        )
        AchievementGroup.TIME_CONTROL -> AchievementGroupStyle(
            accent = scheme.tertiary.copy(alpha = 0.9f),
            background = scheme.surfaceContainer.copy(alpha = 0.2f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.28f),
            headerBackground = scheme.tertiary.copy(alpha = 0.06f),
        )
        AchievementGroup.META -> AchievementGroupStyle(
            accent = scheme.onSurface,
            background = scheme.surfaceContainer.copy(alpha = 0.2f),
            altBackground = scheme.surfaceContainerHigh.copy(alpha = 0.28f),
            headerBackground = scheme.onSurface.copy(alpha = 0.06f),
        )
    }
}
