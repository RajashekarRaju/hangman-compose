package com.developersbreach.hangman.ui.theme

fun String.toThemePaletteId(): ThemePaletteId {
    return runCatching { ThemePaletteId.valueOf(this) }.getOrDefault(ThemePaletteId.INSANE_RED)
}
