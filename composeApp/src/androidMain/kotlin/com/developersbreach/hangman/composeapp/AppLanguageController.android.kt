package com.developersbreach.hangman.composeapp

import android.os.Build
import android.os.LocaleList
import com.developersbreach.hangman.repository.AppLanguage
import java.util.Locale

internal actual fun applyAppLanguage(language: AppLanguage) {
    val locale = Locale.forLanguageTag(language.languageTag)
    Locale.setDefault(locale)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        LocaleList.setDefault(LocaleList(locale))
    }
}
