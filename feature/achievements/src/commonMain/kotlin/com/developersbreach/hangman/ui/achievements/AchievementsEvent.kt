package com.developersbreach.hangman.ui.achievements

import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementGroup
import com.developersbreach.hangman.logging.AuditSpec

sealed interface AchievementsEvent {
    data object NavigateUpClicked : AchievementsEvent
    data class AchievementClicked(val achievementId: AchievementId) : AchievementsEvent
    data object AchievementDetailsDismissed : AchievementsEvent
    data class GroupToggleClicked(val group: AchievementGroup) : AchievementsEvent
}

internal fun AchievementsEvent.auditSpec(current: AchievementsUiState): AuditSpec? = when (this) {
    AchievementsEvent.AchievementDetailsDismissed,
    is AchievementsEvent.GroupToggleClicked,
    AchievementsEvent.NavigateUpClicked -> null
    is AchievementsEvent.AchievementClicked -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "achievements",
            "achievement_id" to achievementId.name,
            "is_unlocked" to (current.items.firstOrNull {
                it.id == achievementId
            }?.isUnlocked?.toString() ?: "unknown"),
        ),
    )
}
