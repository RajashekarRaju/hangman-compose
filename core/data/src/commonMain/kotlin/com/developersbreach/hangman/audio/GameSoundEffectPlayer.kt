package com.developersbreach.hangman.audio

enum class GameSoundEffect {
    LEVEL_WON,
    GAME_WON,
    GAME_LOST,
    ALPHABET_TAP,
}

interface GameSoundEffectPlayer {
    fun play(soundEffect: GameSoundEffect)
}
