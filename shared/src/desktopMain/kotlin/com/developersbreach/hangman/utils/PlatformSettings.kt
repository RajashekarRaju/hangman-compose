package com.developersbreach.hangman.utils

import java.util.prefs.Preferences

actual class PlatformSettings {
    private val prefs = Preferences.userRoot().node("hangman")

    actual fun getString(key: String, default: String?): String? =
        prefs.get(key, default)

    actual fun putString(key: String, value: String) {
        prefs.put(key, value)
    }

    actual fun getInt(key: String, default: Int): Int =
        prefs.getInt(key, default)

    actual fun putInt(key: String, value: Int) {
        prefs.putInt(key, value)
    }
}
