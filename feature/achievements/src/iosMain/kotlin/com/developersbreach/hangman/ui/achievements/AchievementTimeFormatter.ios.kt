package com.developersbreach.hangman.ui.achievements

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.dateWithTimeIntervalSince1970

private val formatter = NSDateFormatter().apply {
    dateFormat = "dd MMM yyyy, hh:mm a"
}

internal actual fun formatEpochMillis(epochMillis: Long): String {
    val date = NSDate.dateWithTimeIntervalSince1970(epochMillis.toDouble() / 1000.0)
    return formatter.stringFromDate(date)
}
