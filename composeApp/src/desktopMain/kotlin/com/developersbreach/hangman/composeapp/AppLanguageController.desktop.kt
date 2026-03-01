package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.repository.AppLanguage
import java.util.Locale

internal actual fun applyAppLanguage(language: AppLanguage) {
    Locale.setDefault(Locale.forLanguageTag(language.languageTag))
}
