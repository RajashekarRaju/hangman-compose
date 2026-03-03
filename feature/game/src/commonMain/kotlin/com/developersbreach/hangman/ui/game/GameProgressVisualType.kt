package com.developersbreach.hangman.ui.game

import com.developersbreach.hangman.repository.GameProgressVisualPreference

sealed interface GameProgressVisualType {
    data object LevelPointsAttemptsInformation : GameProgressVisualType

    data object TraditionalHangman : GameProgressVisualType
}

internal fun GameProgressVisualPreference.toGameProgressVisualType(): GameProgressVisualType {
    return when (this) {
        GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS -> {
            GameProgressVisualType.LevelPointsAttemptsInformation
        }

        GameProgressVisualPreference.TRADITIONAL_HANGMAN -> {
            GameProgressVisualType.TraditionalHangman
        }
    }
}