package com.developersbreach.hangman.ui.game

sealed interface GameEffect {
    data object NavigateUp : GameEffect
}