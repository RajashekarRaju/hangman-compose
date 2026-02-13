package com.developersbreach.hangman.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Returns time and date in required format as pair.
 * This will be used to record the time/date when the player game history saved.
 */
fun getDateAndTime(): Pair<String, String> {
    val dateAndTimeFormat = SimpleDateFormat("dd MMM,hh:mm a", Locale.getDefault())
    val dateAndTime: String = dateAndTimeFormat.format(Calendar.getInstance().time)
    val date = dateAndTime.substringBefore(delimiter = ",")
    val time = dateAndTime.substringAfter(delimiter = ",")
    return Pair(date, time)
}