package com.developersbreach.hangman.repository

enum class CursorStyle {
    DEFAULT,
    SKULL,
    DEMON,
    HAND,
    HAND_BONES,
    BONE;

    companion object {
        val default: CursorStyle = HAND_BONES

        fun fromStorage(value: String): CursorStyle {
            return entries.firstOrNull { style -> style.name == value } ?: default
        }
    }
}
