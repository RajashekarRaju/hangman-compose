package com.developersbreach.hangman.ui.game

sealed interface GameProgressVisualType {
    data object LevelPointsAttemptsInformation : GameProgressVisualType

    data object TraditionalHangman : GameProgressVisualType
}