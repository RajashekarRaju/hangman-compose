package com.developersbreach.hangman.utils

import android.content.Context

private const val PREFERENCE_KEY_GAME_CATEGORY = "preference_game_difficulty_key"
private const val PREFERENCE_RESULT_GAME_CATEGORY = "game_category_result"

class GameCategoryPref(
    context: Context
) {
    private val sharedPref = context.getSharedPreferences(
        PREFERENCE_KEY_GAME_CATEGORY, Context.MODE_PRIVATE
    )

    fun getGameCategoryPref(): GameCategory {
        with(sharedPref.getInt(PREFERENCE_RESULT_GAME_CATEGORY, GameCategory.COUNTRIES.ordinal)) {
            return when (this) {
                GameCategory.COUNTRIES.ordinal -> GameCategory.COUNTRIES
                GameCategory.LANGUAGES.ordinal -> GameCategory.LANGUAGES
                GameCategory.COMPANIES.ordinal -> GameCategory.COMPANIES
                else -> GameCategory.COUNTRIES
            }
        }
    }

    fun updateGameCategoryPref(
        gameCategory: Int
    ) {
        with(
            sharedPref.edit()
        ) {
            putInt(
                PREFERENCE_RESULT_GAME_CATEGORY,
                gameCategory
            )
            commit()
        }
    }
}