package com.hangman.hangman.utils

import android.content.Context

const val PREFERENCE_KEY_GAME_HIGH_SCORE = "preference_game_high_score_key"
const val PREFERENCE_RESULT_GAME_HIGH_SCORE = "game_high_score_result"

class HighScorePref(
    context: Context
) {
    private val sharedPref = context.getSharedPreferences(
        PREFERENCE_KEY_GAME_HIGH_SCORE, Context.MODE_PRIVATE
    )

    fun getHighScorePref(): Int {
        return sharedPref.getInt(PREFERENCE_RESULT_GAME_HIGH_SCORE, 0)
    }

    fun updateHighScorePref(
        highScorePref: Int
    ) {
        with(
            sharedPref.edit()
        ) {
            putInt(
                PREFERENCE_RESULT_GAME_HIGH_SCORE,
                highScorePref
            )
            commit()
        }
    }
}