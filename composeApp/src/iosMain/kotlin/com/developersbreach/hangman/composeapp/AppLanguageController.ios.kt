package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.repository.AppLanguage
import platform.Foundation.NSUserDefaults

internal actual fun applyAppLanguage(language: AppLanguage) {
    NSUserDefaults.standardUserDefaults.setObject(
        listOf(language.languageTag),
        forKey = "AppleLanguages",
    )
}
