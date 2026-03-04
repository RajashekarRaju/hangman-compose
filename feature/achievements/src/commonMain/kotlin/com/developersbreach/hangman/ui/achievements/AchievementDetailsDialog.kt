package com.developersbreach.hangman.ui.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DsRes
import com.developersbreach.hangman.core.designsystem.generated.resources.dialog_close
import com.developersbreach.hangman.feature.achievements.generated.resources.Res
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_locked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_unlocked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_group
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_locked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_progress
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_status
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_unlocked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_details_unlocked_at
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.BodyMediumText
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.HangmanDialogFooterButton
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AchievementDetailsDialog(
    details: AchievementDetailsUiState,
    onDismissRequest: () -> Unit,
) {
    HangmanDialog(
        onDismissRequest = onDismissRequest,
        seed = details.id.name.hashCode(),
        headerTitle = stringResource(details.titleRes),
        headerIconContent = {
            HangmanIcon(
                imageVector = if (details.isUnlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
                contentDescription = stringResource(
                    when {
                        details.isUnlocked -> Res.string.achievements_cd_unlocked
                        else -> Res.string.achievements_cd_locked
                    },
                ),
                tint = HangmanTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(40.dp),
            )
        },
        threshold = 0.07f,
        modifier = Modifier.fillMaxWidth(0.88f),
        contentPadding = PaddingValues(36.dp),
        footerButtons = listOf(
            HangmanDialogFooterButton(
                text = stringResource(DsRes.string.dialog_close),
                onClick = onDismissRequest,
                color = HangmanTheme.colorScheme.primary,
            ),
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            BodyLargeText(
                text = stringResource(details.descriptionRes),
                color = HangmanTheme.colorScheme.onSurface,
            )

            BodyMediumText(
                text = stringResource(
                    Res.string.achievements_details_status,
                    if (details.isUnlocked) {
                        stringResource(Res.string.achievements_details_unlocked)
                    } else {
                        stringResource(Res.string.achievements_details_locked)
                    }
                ),
                color = HangmanTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            )

            BodyMediumText(
                text = stringResource(
                    Res.string.achievements_details_group,
                    stringResource(details.group.titleRes()),
                ),
                color = HangmanTheme.colorScheme.onSurface.copy(alpha = 0.88f),
            )

            when {
                details.isUnlocked -> {
                    details.unlockedAtLabel?.let { unlockedAt ->
                        BodyMediumText(
                            text = stringResource(
                                Res.string.achievements_details_unlocked_at,
                                unlockedAt,
                            ),
                            color = HangmanTheme.colorScheme.onSurface.copy(alpha = 0.88f),
                        )
                    }
                }

                else -> {
                    BodyMediumText(
                        text = stringResource(
                            Res.string.achievements_details_progress,
                            details.progressCurrent,
                            details.progressTarget,
                        ),
                        color = HangmanTheme.colorScheme.onSurface.copy(alpha = 0.88f),
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }
        }
    }
}
