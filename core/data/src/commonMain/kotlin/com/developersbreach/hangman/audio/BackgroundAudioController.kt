package com.developersbreach.hangman.audio

interface BackgroundAudioController {
    fun playLoop()

    fun stop()

    fun isPlaying(): Boolean
}
