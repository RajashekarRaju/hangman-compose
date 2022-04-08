package com.hangman.hangman.utils

import android.content.Context

const val PREFERENCE_KEY_GAME_DIFFICULTY = "preference_game_difficulty_key"
const val PREFERENCE_RESULT_GAME_DIFFICULTY = "game_difficulty_result"

class GameDifficultyPref(
    context: Context
) {
    private val sharedPref = context.getSharedPreferences(
        PREFERENCE_KEY_GAME_DIFFICULTY, Context.MODE_PRIVATE
    )

    fun getGameDifficultyPref(): GameDifficulty {
        with(sharedPref.getString(PREFERENCE_RESULT_GAME_DIFFICULTY, GameDifficulty.EASY.name)) {
            return when (this) {
                GameDifficulty.EASY.name -> GameDifficulty.EASY
                GameDifficulty.MEDIUM.name -> GameDifficulty.MEDIUM
                GameDifficulty.HARD.name -> GameDifficulty.HARD
                else -> GameDifficulty.EASY
            }
        }
    }

    fun updateGameDifficultyPref(
        gameDifficulty: GameDifficulty
    ) {
        with(
            sharedPref.edit()
        ) {
            putString(
                PREFERENCE_RESULT_GAME_DIFFICULTY,
                gameDifficulty.name
            )
            commit()
        }
    }
}