package com.developersbreach.hangman.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_category
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_difficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_button_language
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_button_theme_palette
import com.developersbreach.hangman.feature.onboarding.generated.resources.settings_title
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.HangmanIconActionButton
import com.developersbreach.hangman.ui.components.HangmanPrimaryButton
import com.developersbreach.hangman.ui.components.LabelLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes

@Composable
fun SettingsUiState.SettingsScreenUI(
    onEvent: (SettingsEvent) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(DesignRes.drawable.game_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.1f,
        )

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            item {
                SettingsTopBar(onBackClick = { onEvent(SettingsEvent.NavigateUpClicked) })
            }

            item {
                SettingsActionButton(
                    text = stringResource(Res.string.onboarding_button_difficulty),
                    onClick = { onEvent(SettingsEvent.OpenDifficultyDialog) },
                )
            }

            item {
                SettingsActionButton(
                    text = stringResource(Res.string.onboarding_button_category),
                    onClick = { onEvent(SettingsEvent.OpenCategoryDialog) },
                )
            }

            item {
                SettingsActionButton(
                    text = stringResource(Res.string.onboarding_button_language),
                    onClick = { onEvent(SettingsEvent.OpenLanguageDialog) },
                )
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 6.dp),
                ) {
                    LabelLargeText(
                        text = stringResource(Res.string.settings_button_theme_palette),
                        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
                    )

                    ThemePaletteDropdown(
                        selectedPaletteId = themePaletteId,
                        expanded = isPaletteMenuExpanded,
                        onExpandRequest = { onEvent(SettingsEvent.OpenThemePaletteMenu) },
                        onDismissRequest = { onEvent(SettingsEvent.DismissThemePaletteMenu) },
                        onPaletteChanged = {
                            onEvent(SettingsEvent.ThemePaletteChanged(it))
                            onEvent(SettingsEvent.DismissThemePaletteMenu)
                        },
                    )
                }
            }
        }

        if (isDifficultyDialogOpen) {
            AdjustGameDifficultyDialog(
                selectedDifficulty = pendingDifficulty,
                sliderDifficultyPosition = pendingDifficultySliderPosition,
                onSliderPositionChanged = {
                    onEvent(SettingsEvent.DifficultySliderPositionChanged(it))
                },
                onDifficultyConfirmed = { onEvent(SettingsEvent.DifficultyChanged(it)) },
                onDismissRequest = { onEvent(SettingsEvent.DismissDifficultyDialog) },
            )
        }

        if (isCategoryDialogOpen) {
            ChooseGameCategoryDialog(
                categories = availableCategories,
                gameCategory = gameCategory,
                updatePlayerChosenCategory = { onEvent(SettingsEvent.CategoryChanged(it)) },
                onDismissRequest = { onEvent(SettingsEvent.DismissCategoryDialog) },
            )
        }

        if (isLanguageDialogOpen) {
            ChooseLanguageDialog(
                languages = availableLanguages,
                selectedLanguage = selectedLanguage,
                onLanguageChanged = { onEvent(SettingsEvent.LanguageChanged(it)) },
                onDismissRequest = { onEvent(SettingsEvent.DismissLanguageDialog) },
            )
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(top = 20.dp, bottom = 8.dp)
            .width(240.dp),
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
private fun SettingsActionButton(
    text: String,
    onClick: () -> Unit,
) {
    HangmanPrimaryButton(
        onClick = onClick,
        seed = text.hashCode(),
        threshold = 0.14f,
        modifier = Modifier.width(200.dp),
    ) {
        LabelLargeText(
            text = text,
            color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(vertical = 4.dp),
        )
    }
}
