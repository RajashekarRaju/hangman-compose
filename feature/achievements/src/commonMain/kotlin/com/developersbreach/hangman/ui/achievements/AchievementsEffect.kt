package com.developersbreach.hangman.ui.achievements

sealed interface AchievementsEffect {
    data object NavigateUp : AchievementsEffect
}
