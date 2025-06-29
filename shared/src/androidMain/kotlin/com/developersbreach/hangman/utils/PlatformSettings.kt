package com.developersbreach.hangman.utils

import android.content.Context

actual class PlatformSettings(private val context: Context) {
    private val prefs = GetEncryptedSharedPreferences(context).invoke("game_prefs")

    actual fun getString(key: String, default: String?): String? =
        prefs.getString(key, default)

    actual fun putString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    actual fun getInt(key: String, default: Int): Int =
        prefs.getInt(key, default)

    actual fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }
}
