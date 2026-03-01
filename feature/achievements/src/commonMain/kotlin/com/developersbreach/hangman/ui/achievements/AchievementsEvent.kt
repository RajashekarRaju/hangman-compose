package com.developersbreach.hangman.ui.achievements

import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementGroup

sealed interface AchievementsEvent {
    data object NavigateUpClicked : AchievementsEvent
    data class AchievementClicked(val achievementId: AchievementId) : AchievementsEvent
    data object AchievementDetailsDismissed : AchievementsEvent
    data class GroupToggleClicked(val group: AchievementGroup) : AchievementsEvent
}
