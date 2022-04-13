package com.hangman.hangman.modal

import androidx.compose.runtime.Stable

/**
 * @param isAlphabetGuessed keeps track of all 26 alphabets boolean values whether any
 * alphabet is being already guessed or not.
 */
data class Alphabets(
    @Stable val alphabetId: Int,
    val alphabet: String,
    var isAlphabetGuessed: Boolean = false
)
