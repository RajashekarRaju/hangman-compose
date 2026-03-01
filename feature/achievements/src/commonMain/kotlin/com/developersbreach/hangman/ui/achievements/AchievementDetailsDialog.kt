package com.developersbreach.hangman.ui.achievements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.TitleMediumText
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
        threshold = 0.07f,
        modifier = Modifier.fillMaxWidth(0.88f),
        contentPadding = PaddingValues(40.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            HangmanIcon(
                imageVector = if (details.isUnlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
                contentDescription = stringResource(
                    when {
                        details.isUnlocked -> Res.string.achievements_cd_unlocked
                        else -> Res.string.achievements_cd_locked
                    }
                ),
                tint = HangmanTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleMediumText(
                text = details.title,
                color = HangmanTheme.colorScheme.primary,
            )

            BodyLargeText(
                text = details.description,
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
