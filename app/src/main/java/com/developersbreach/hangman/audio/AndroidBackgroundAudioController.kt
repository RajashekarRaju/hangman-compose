package com.developersbreach.hangman.audio

import android.app.Application
import android.media.MediaPlayer
import com.developersbreach.hangman.R

class AndroidBackgroundAudioController(
    private val application: Application
) : BackgroundAudioController {
    private var mediaPlayer: MediaPlayer? = null

    override fun playLoop() {
        val player = mediaPlayer
            ?: MediaPlayer.create(application.applicationContext, R.raw.game_background_music)
                .also { created ->
                    created.isLooping = true
                    mediaPlayer = created
                }

        if (!player.isPlaying) {
            player.start()
        }
    }

    override fun stop() {
        mediaPlayer?.run {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
