package com.developersbreach.hangman.utils

import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

actual class PlatformAudioPlayer {
    private var clip: Clip? = null

    actual val isPlaying: Boolean
        get() = clip?.isRunning ?: false

    actual fun play(resource: String) {
        val stream = this::class.java.classLoader.getResourceAsStream(resource) ?: return
        val audioIn = AudioSystem.getAudioInputStream(BufferedInputStream(stream))
        clip = AudioSystem.getClip().apply {
            open(audioIn)
            start()
        }
    }

    actual fun release() {
        clip?.close()
    }
}
