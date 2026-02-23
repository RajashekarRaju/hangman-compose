package com.developersbreach.hangman.repository.metadata

data class HistoryMetadata(
    val gameId: String,
    val gamePlayedDate: String,
    val gamePlayedTime: String,
)

expect fun generateHistoryMetadata(historySize: Int = 0): HistoryMetadata
