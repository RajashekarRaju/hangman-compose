package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.repository.AppLanguage

@Suppress("UNUSED_PARAMETER")
internal actual fun applyAppLanguage(language: AppLanguage) {
    // Compose/Wasm currently resolves locale from browser language.
    // In-app runtime override is not available here yet.
    Unit
}
