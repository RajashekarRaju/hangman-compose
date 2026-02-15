package com.developersbreach.hangman.audio

import android.app.Application
import android.media.MediaPlayer
import com.developersbreach.hangman.core.data.R

class AndroidGameSoundEffectPlayer(
    private val application: Application,
) : GameSoundEffectPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(soundEffect: GameSoundEffect) {
        val audioRes = when (soundEffect) {
            GameSoundEffect.LEVEL_WON -> R.raw.level_won
            GameSoundEffect.GAME_WON -> R.raw.game_won
            GameSoundEffect.GAME_LOST -> R.raw.game_lost
            GameSoundEffect.ALPHABET_TAP -> R.raw.alphabet_tap
        }

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(application.applicationContext, audioRes).also { player ->
            if (!player.isPlaying) {
                player.start()
            }
        }
    }
}
