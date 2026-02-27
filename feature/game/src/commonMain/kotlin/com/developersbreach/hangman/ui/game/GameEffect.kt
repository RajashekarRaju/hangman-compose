package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.AchievementId

sealed interface GameEffect {
    data object NavigateUp : GameEffect
    data class AchievementsUnlocked(val ids: List<AchievementId>) : GameEffect
}
