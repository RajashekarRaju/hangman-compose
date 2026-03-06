package com.developersbreach.hangman.ui.common.notification

import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res
import com.developersbreach.hangman.feature.common.ui.generated.resources.achievement_unlocked_subtitle
import com.developersbreach.hangman.ui.common.achievement.titleRes
import org.jetbrains.compose.resources.StringResource

data class AchievementBannerPayload(
    val id: AchievementId,
    val titleRes: StringResource,
    val subtitleRes: StringResource,
    val seed: Int,
    val colors: AchievementBannerColors = AchievementBannerColors(),
)

data class AchievementBannerColors(
    val fillAlpha: Float = 0.96f,
    val outlineAlpha: Float = 0.72f,
    val subtitleAlpha: Float = 0.86f,
)

data class AchievementBannerUiState(
    val payload: AchievementBannerPayload? = null,
    val isVisible: Boolean = false,
)

internal fun AchievementId.toBannerPayload(): AchievementBannerPayload {
    return AchievementBannerPayload(
        id = this,
        titleRes = titleRes(),
        subtitleRes = Res.string.achievement_unlocked_subtitle,
        seed = name.hashCode(),
    )
}
