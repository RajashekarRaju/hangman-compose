package com.developersbreach.hangman.audio

enum class GameSoundEffect(
    val resourceKey: String,
) {
    LEVEL_WON(resourceKey = "level_won"),
    GAME_WON(resourceKey = "game_won"),
    GAME_LOST(resourceKey = "game_lost"),
    ALPHABET_TAP(resourceKey = "alphabet_tap"),
}

interface GameSoundEffectPlayer {
    fun play(soundEffect: GameSoundEffect)
}
