package com.developersbreach.hangman.repository.metadata

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSUUID

actual fun generateHistoryMetadata(historySize: Int): HistoryMetadata {
    return HistoryMetadata(
        gameId = "ios-${NSUUID().UUIDString}",
        gamePlayedDate = dateNow(),
        gamePlayedTime = timeNow(),
    )
}

private fun dateNow(): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = 2u
    formatter.timeStyle = 0u
    return formatter.stringFromDate(NSDate())
}

private fun timeNow(): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = 0u
    formatter.timeStyle = 2u
    return formatter.stringFromDate(NSDate())
}
