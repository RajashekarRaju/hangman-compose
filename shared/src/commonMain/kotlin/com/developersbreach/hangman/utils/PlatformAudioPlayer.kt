package com.developersbreach.hangman.utils

expect class PlatformAudioPlayer {
    fun play(resource: String)
    fun release()
    val isPlaying: Boolean
}
