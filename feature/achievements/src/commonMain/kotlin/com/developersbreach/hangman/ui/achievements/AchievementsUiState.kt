package com.developersbreach.hangman.ui.achievements

import androidx.compose.ui.graphics.Color
import com.developersbreach.game.core.achievements.AchievementGroup
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.hangman.feature.achievements.generated.resources.Res
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_collection
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_endurance
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_hint_discipline
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_meta
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_progress
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_skill
import com.developersbreach.hangman.feature.achievements.generated.resources.achievements_group_time_control

data class AchievementsUiState(
    val items: List<AchievementItemUiState> = emptyList(),
)

data class AchievementItemUiState(
    val id: AchievementId,
    val group: AchievementGroup,
    val title: String,
    val description: String,
    val isUnlocked: Boolean,
    val progressCurrent: Int,
    val progressTarget: Int,
    val unlockedAtLabel: String? = null,
)

data class AchievementSection(
    val group: AchievementGroup,
    val items: List<AchievementItemUiState>,
)

data class AchievementGroupStyle(
    val accent: Color,
    val background: Color,
    val altBackground: Color,
    val headerBackground: Color,
)

internal fun AchievementGroup.titleRes() = when (this) {
    AchievementGroup.PROGRESS -> Res.string.achievements_group_progress
    AchievementGroup.SKILL -> Res.string.achievements_group_skill
    AchievementGroup.COLLECTION -> Res.string.achievements_group_collection
    AchievementGroup.ENDURANCE -> Res.string.achievements_group_endurance
    AchievementGroup.HINT_DISCIPLINE -> Res.string.achievements_group_hint_discipline
    AchievementGroup.TIME_CONTROL -> Res.string.achievements_group_time_control
    AchievementGroup.META -> Res.string.achievements_group_meta
}