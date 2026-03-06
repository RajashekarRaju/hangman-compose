package com.developersbreach.hangman.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.mainmenu.generated.resources.Res
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_title
import com.developersbreach.hangman.ui.settings.components.SettingsInlineAudioSection
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.settings.components.SettingsInlineCategorySection
import com.developersbreach.hangman.ui.settings.components.SettingsInlineCursorSection
import com.developersbreach.hangman.ui.settings.components.SettingsInlineDifficultySection
import com.developersbreach.hangman.ui.settings.components.SettingsInlineGameVisualSection
import com.developersbreach.hangman.ui.settings.components.SettingsInlineLanguageSection
import com.developersbreach.hangman.ui.settings.components.SettingsInlineThemeSection
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val WIDE_SETTINGS_BREAKPOINT_DP = 600

@Composable
fun SettingsUiState.SettingsScreenUI(
    onEvent: (SettingsEvent) -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val isWideLayout = maxWidth >= WIDE_SETTINGS_BREAKPOINT_DP.dp

        when {
            isWideLayout -> SettingsVerticalSplitLayout(
                uiState = this@SettingsScreenUI,
                visibleSections = visibleSettingsSections,
                selectedSection = selectedSettingsSection,
                onEvent = onEvent,
            )

            else -> SettingsHorizontalSplitLayout(
                uiState = this@SettingsScreenUI,
                visibleSections = visibleSettingsSections,
                selectedSection = selectedSettingsSection,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun SettingsVerticalSplitLayout(
    uiState: SettingsUiState,
    visibleSections: List<SettingsSection>,
    selectedSection: SettingsSection,
    onEvent: (SettingsEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsNavigationPane(
            visibleSections = visibleSections,
            selectedSection = selectedSection,
            onEvent = onEvent,
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
        )

        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
                .creepyOutline(
                    seed = 800,
                    threshold = 1.08f,
                    fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.5f),
                    outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
                    strokeWidthFactor = 1f,
                )
        )

        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxHeight()
                .padding(start = 24.dp, top = 84.dp),
        ) {
            SettingsDetailPane(
                uiState = uiState,
                selectedSection = selectedSection,
                onEvent = onEvent,
                centerContent = false,
                contentWidth = 460.dp,
            )
        }
    }
}

@Composable
private fun SettingsHorizontalSplitLayout(
    uiState: SettingsUiState,
    visibleSections: List<SettingsSection>,
    selectedSection: SettingsSection,
    onEvent: (SettingsEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SettingsNavigationPane(
            visibleSections = visibleSections,
            selectedSection = selectedSection,
            onEvent = onEvent,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .creepyOutline(
                    seed = 800,
                    threshold = 1.08f,
                    fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.5f),
                    outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
                    strokeWidthFactor = 1f,
                )
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            SettingsDetailPane(
                uiState = uiState,
                selectedSection = selectedSection,
                onEvent = onEvent,
                centerContent = true,
                contentWidth = 340.dp,
            )
        }
    }
}

@Composable
private fun SettingsNavigationPane(
    visibleSections: List<SettingsSection>,
    selectedSection: SettingsSection,
    onEvent: (SettingsEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val backgroundImageAlpha = when {
        HangmanTheme.colorScheme.background.luminance() > 0.6f -> 0.75f
        else -> 0.05f
    }
    Box(modifier = modifier) {
        Image(
            painter = painterResource(DesignRes.drawable.game_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = backgroundImageAlpha,
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .verticalScroll(scrollState),
        ) {
            SettingsTopBar(
                onBackClick = { onEvent(SettingsEvent.NavigateUpClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 12.dp),
            )

            visibleSections.forEachIndexed { index, section ->
                SettingsNavTextItem(
                    text = stringResource(section.labelRes),
                    isSelected = selectedSection == section,
                    onClick = {
                        onEvent(SettingsEvent.SettingsSectionSelected(section))
                    },
                )
                if (index != visibleSections.lastIndex) {
                    SettingsSectionDivider()
                }
            }
        }
    }
}

@Composable
private fun SettingsDetailPane(
    uiState: SettingsUiState,
    selectedSection: SettingsSection,
    onEvent: (SettingsEvent) -> Unit,
    centerContent: Boolean,
    contentWidth: Dp,
) {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        contentAlignment = if (centerContent) Alignment.Center else Alignment.TopStart,
    ) {
        AnimatedContent(
            targetState = selectedSection,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "settings_detail_content",
        ) { section ->
            when (section) {
                SettingsSection.Difficulty -> {
                    SettingsInlineDifficultySection(
                        pendingDifficultySliderPosition = uiState.pendingDifficultySliderPosition,
                        pendingDifficultyLabelRes = uiState.pendingDifficulty.labelRes(),
                        onSliderPositionChanged = {
                            onEvent(SettingsEvent.DifficultySliderPositionChanged(it))
                        },
                        onDifficultyConfirmed = {
                            onEvent(SettingsEvent.DifficultyChanged(uiState.pendingDifficulty))
                        },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.Category -> {
                    SettingsInlineCategorySection(
                        categories = uiState.availableCategories,
                        selectedCategory = uiState.gameCategory,
                        onCategoryChanged = { onEvent(SettingsEvent.CategoryChanged(it)) },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.Language -> {
                    SettingsInlineLanguageSection(
                        languages = uiState.availableLanguages,
                        selectedLanguage = uiState.selectedLanguage,
                        onLanguageChanged = { onEvent(SettingsEvent.LanguageChanged(it)) },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.Theme -> {
                    SettingsInlineThemeSection(
                        selectedThemeMode = uiState.themeMode,
                        onThemeModeChanged = { onEvent(SettingsEvent.ThemeModeChanged(it)) },
                        selectedPaletteId = uiState.themePaletteId,
                        onPaletteChanged = { onEvent(SettingsEvent.ThemePaletteChanged(it)) },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.GameVisual -> {
                    SettingsInlineGameVisualSection(
                        selectedGameProgressVisualPreference = uiState.selectedGameProgressVisualPreference,
                        onGameProgressVisualPreferenceChanged = {
                            onEvent(SettingsEvent.GameProgressVisualPreferenceChanged(it))
                        },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.Audio -> {
                    SettingsInlineAudioSection(
                        isBackgroundMusicEnabled = uiState.isBackgroundMusicEnabled,
                        isSoundEffectsEnabled = uiState.isSoundEffectsEnabled,
                        onBackgroundMusicChanged = {
                            onEvent(SettingsEvent.BackgroundMusicToggled(it))
                        },
                        onSoundEffectsChanged = {
                            onEvent(SettingsEvent.SoundEffectsToggled(it))
                        },
                        modifier = Modifier.width(contentWidth),
                    )
                }

                SettingsSection.Cursor -> {
                    SettingsInlineCursorSection(
                        availableCursorStyles = uiState.availableCursorStyles,
                        selectedCursorStyle = uiState.selectedCursorStyle,
                        onCursorStyleChanged = {
                            onEvent(SettingsEvent.CursorStyleChanged(it))
                        },
                        modifier = Modifier.width(contentWidth),
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(top = 8.dp, bottom = 8.dp),
    ) {
        HangmanIconActionButton(
            onClick = onBackClick,
            seed = 615,
            size = 42,
            threshold = 0.14f,
            backgroundColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.06f),
        ) {
            HangmanIcon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = HangmanTheme.colorScheme.primary,
            )
        }

        TitleMediumText(
            text = stringResource(Res.string.settings_title),
            color = HangmanTheme.colorScheme.secondary,
        )
    }
}

@Composable
private fun SettingsNavTextItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .selectable(
                selected = isSelected,
                role = Role.Tab,
                indication = null,
                interactionSource = interactionSource,
                onClick = onClick,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        LabelLargeText(
            text = text,
            color = if (isSelected) {
                HangmanTheme.colorScheme.secondary
            } else {
                HangmanTheme.colorScheme.primary.copy(alpha = 0.75f)
            },
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 6.dp),
        )
    }
}

@Composable
private fun SettingsSectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .height(2.dp)
            .creepyOutline(
                seed = 990,
                threshold = 0.15f,
                fillColor = Color.Transparent,
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.35f),
                strokeWidthFactor = 0.80f,
            ),
    )
}
