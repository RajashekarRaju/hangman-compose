package com.developersbreach.hangman.ui.achievements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.achievements.generated.resources.Res
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_locked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_cd_unlocked
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_progress
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_unlocked_at
import com.developersbreach.hangman.ui.components.BodyMediumText
import com.developersbreach.hangman.ui.components.BodySmallText
import com.developersbreach.hangman.ui.components.HangmanIcon
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AchievementItemUiState.AchievementItem(
    style: AchievementGroupStyle,
    showPaleBackground: Boolean,
) {
    val statusColor = when {
        isUnlocked -> style.accent
        else -> HangmanTheme.colorScheme.onSurface.copy(alpha = 0.64f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                when {
                    showPaleBackground -> style.altBackground
                    else -> style.background
                }
            )
            .creepyOutline(
                seed = id.name.hashCode(),
                threshold = 0.08f,
                fillColor = Color.Transparent,
                outlineColor = style.accent.copy(alpha = 0.28f),
                strokeWidthFactor = 0.04f,
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TitleMediumText(
                text = title,
                color = HangmanTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f),
            )
            HangmanIcon(
                imageVector = if (isUnlocked) Icons.Filled.LockOpen else Icons.Filled.Lock,
                contentDescription = stringResource(
                    when {
                        isUnlocked -> Res.string.achievements_cd_unlocked
                        else -> Res.string.achievements_cd_locked
                    }
                ),
                tint = statusColor,
                modifier = Modifier.size(20.dp),
            )
        }

        BodyMediumText(
            text = description,
            color = HangmanTheme.colorScheme.onSurface,
        )

        when {
            isUnlocked -> unlockedAtLabel?.let { unlockedAt ->
                BodySmallText(
                    text = stringResource(Res.string.achievements_unlocked_at, unlockedAt),
                    color = statusColor.copy(alpha = 0.9f),
                )
            }

            else -> BodySmallText(
                text = stringResource(
                    Res.string.achievements_progress,
                    progressCurrent,
                    progressTarget,
                ),
                color = statusColor,
            )
        }
    }
}