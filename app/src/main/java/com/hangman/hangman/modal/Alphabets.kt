package com.hangman.hangman.modal

import androidx.compose.runtime.Stable

data class Alphabets(
    @Stable val alphabetId: Int,
    val alphabet: String,
    var isAlphabetGuessed: Boolean = false
)
