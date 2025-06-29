package com.developersbreach.hangman.utils

import android.content.Context
import android.media.MediaPlayer

actual class PlatformAudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null

    actual val isPlaying: Boolean
        get() = mediaPlayer?.isPlaying ?: false

    actual fun play(resource: String) {
        val resId = context.resources.getIdentifier(resource.substringBeforeLast('.'), "raw", context.packageName)
        mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer?.start()
    }

    actual fun release() {
        mediaPlayer?.release()
    }
}
