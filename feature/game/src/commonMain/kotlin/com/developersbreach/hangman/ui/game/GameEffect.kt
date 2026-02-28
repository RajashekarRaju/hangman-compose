package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.AchievementCatalog
import com.developersbreach.game.core.AchievementId

sealed interface GameEffect {
    data object NavigateUp : GameEffect
    data class ShowAchievementBanner(val payload: AchievementBannerPayload) : GameEffect
    data object HideAchievementBanner : GameEffect
}

data class AchievementBannerPayload(
    val id: AchievementId,
    val title: String,
    val subtitle: String,
    val seed: Int,
    val colors: AchievementBannerColors = AchievementBannerColors(),
)

data class AchievementBannerColors(
    val fillAlpha: Float = 0.96f,
    val outlineAlpha: Float = 0.72f,
    val subtitleAlpha: Float = 0.86f,
)

internal fun AchievementId.toBannerPayload(): AchievementBannerPayload {
    val definition = AchievementCatalog.definitionFor(this)
    return AchievementBannerPayload(
        id = this,
        title = definition.title,
        subtitle = "Achievement Unlocked",
        seed = definition.id.name.hashCode(),
    )
}
