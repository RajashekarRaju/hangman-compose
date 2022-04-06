package com.hangman.hangman.modal

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class Alphabets(
    @Stable val alphabetId: Int,
    val alphabet: String
)
