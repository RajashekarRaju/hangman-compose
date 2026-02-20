package com.developersbreach.hangman.repository.metadata

import com.developersbreach.hangman.utils.getDateAndTime
import java.util.UUID

actual fun generateHistoryMetadata(historySize: Int): HistoryMetadata {
    val (date, time) = getDateAndTime()
    return HistoryMetadata(
        gameId = UUID.randomUUID().toString(),
        gamePlayedDate = date,
        gamePlayedTime = time,
    )
}
