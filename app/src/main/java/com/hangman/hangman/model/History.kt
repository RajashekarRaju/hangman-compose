package com.hangman.hangman.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
data class History(
    @Stable val gameId: Int,
    val gameScore: Int,
    val gameDifficulty: Int,
    val gameSummary: Boolean
)
