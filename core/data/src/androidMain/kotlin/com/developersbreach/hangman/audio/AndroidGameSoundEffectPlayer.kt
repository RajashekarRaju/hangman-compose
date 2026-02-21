package com.developersbreach.hangman.audio

import android.app.Application
import android.media.MediaPlayer

class AndroidGameSoundEffectPlayer(
    private val application: Application,
) : GameSoundEffectPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(soundEffect: GameSoundEffect) {
        val audioRes = application.resources.getIdentifier(
            /* name = */ soundEffect.resourceKey,
            /* defType = */ "raw",
            /* defPackage = */ application.packageName,
        )
        if (audioRes == 0) return

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(application.applicationContext, audioRes).also { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }
}
