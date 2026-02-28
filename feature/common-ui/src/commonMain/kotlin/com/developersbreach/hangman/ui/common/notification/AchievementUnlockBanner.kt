package com.developersbreach.hangman.ui.common.notification

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.TitleSmallText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun AchievementUnlockBanner(
    state: AchievementBannerUiState,
    modifier: Modifier = Modifier,
) {
    val payload = state.payload
    AnimatedVisibility(
        visible = payload != null && state.isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { fullHeight -> -fullHeight / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { fullHeight -> -fullHeight / 3 }),
        modifier = modifier,
    ) {
        val bannerPayload = payload ?: return@AnimatedVisibility

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .creepyOutline(
                    threshold = 0.09f,
                    seed = bannerPayload.seed,
                    fillColor = HangmanTheme.colorScheme.background.copy(alpha = bannerPayload.colors.fillAlpha),
                    outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = bannerPayload.colors.outlineAlpha),
                    forceRectangular = true,
                )
                .padding(horizontal = 18.dp, vertical = 12.dp),
        ) {
            TitleSmallText(
                text = stringResource(bannerPayload.subtitleRes),
                color = HangmanTheme.colorScheme.primary.copy(alpha = bannerPayload.colors.subtitleAlpha),
            )
            BodyLargeText(
                text = bannerPayload.title,
                color = HangmanTheme.colorScheme.onBackground,
            )
        }
    }
}
