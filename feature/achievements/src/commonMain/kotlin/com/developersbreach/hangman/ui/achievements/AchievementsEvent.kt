package com.developersbreach.hangman.ui.achievements

sealed interface AchievementsEvent {
    data object NavigateUpClicked : AchievementsEvent
}
