package com.developersbreach.hangman.ui.settings

sealed interface SettingsEffect {
    data object NavigateUp : SettingsEffect
}
