package com.hangman.hangman.utils

import androidx.compose.runtime.mutableStateOf
import com.hangman.hangman.modal.Alphabets
import java.text.SimpleDateFormat
import java.util.*


/**
 * The return type should be immutable since it's result is used in GameViewModel.
 * Creating a [mutableStateOf] object with a mutable collection type isn't possible, so
 * at the end instead of returning array object, transform those to list.
 */
fun alphabetsList(): List<Alphabets> {
    val alphabet = ('A'..'Z').toList()
    val alphabetId = (1..26).toList()
    val zipAlphabets = alphabetId zip alphabet

    val alphabetList = arrayListOf<Alphabets>()
    zipAlphabets.forEach { pair ->
        alphabetList.add(
            Alphabets(
                alphabetId = pair.first,
                alphabet = pair.second.toString()
            )
        )
    }

    return alphabetList.toList()
}

/**
 * Returns time and data in required format as pair.
 * This will be used to record the time/date when the player game history saved.
 */
fun getDateAndTime(): Pair<String, String> {
    val dateAndTimeFormat = SimpleDateFormat("dd MMM,hh:mm a", Locale.getDefault())
    val dateAndTime: String = dateAndTimeFormat.format(Calendar.getInstance().time)
    val date = dateAndTime.substringBefore(delimiter = ",")
    val time = dateAndTime.substringAfter(delimiter = ",")
    return Pair(date, time)
}