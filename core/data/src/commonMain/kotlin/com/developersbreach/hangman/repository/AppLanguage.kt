package com.developersbreach.hangman.repository

enum class AppLanguage(
    val languageTag: String,
    val nativeName: String,
) {
    ENGLISH(languageTag = "en", nativeName = "English"),
    SPANISH(languageTag = "es", nativeName = "Español"),
    FRENCH(languageTag = "fr", nativeName = "Français"),
    HINDI(languageTag = "hi", nativeName = "हिन्दी");

    companion object {
        val default: AppLanguage = ENGLISH

        fun fromLanguageTag(languageTag: String): AppLanguage {
            return entries.firstOrNull { language ->
                language.languageTag.equals(languageTag, ignoreCase = true)
            } ?: default
        }
    }
}
