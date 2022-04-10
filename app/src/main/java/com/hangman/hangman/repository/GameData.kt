package com.hangman.hangman.repository

import androidx.compose.runtime.mutableStateOf
import com.hangman.hangman.modal.Alphabets


object GameData {

    val easyGuessingWords = listOf(
        "india", "china", "sudan", "iran", "libya", "chad", "peru", "egypt", "mali", "chile",
        "kenya", "yemen", "spain", "iraq", "japan", "congo", "italy", "oman", "malta", "nauru",
        "ghana", "laos", "syria", "nepal", "cuba", "togo", "haiti", "fiji", "qatar", "tonga"
    )

    val mediumGuessingWords = listOf(
        "russia", "canada", "brazil", "algeria", "mexico", "angola", "bolivia", "nigeria", "turkey",
        "myanmar", "somalia", "ukraine", "morocco", "germany", "finland", "poland", "vietnam",
        "guinea", "uganda", "romania", "guyana", "belarus", "uruguay", "tunisia", "iceland",
        "jordan", "serbia", "austria", "ireland", "georgia", "panama", "ireland", "croatia",
        "denmark", "bhutan", "taiwan", "latvia", "norway", "france"
    )

    val hardGuessingWords = listOf(
        "antarctica", "australia", "argentina", "indonesia", "mongolia", "pakistan", "thailand",
        "cambodia", "bangladesh", "tajikistan", "guatemala", "portugal", "bulgaria", "azerbaijan",
        "lithuania", "palestine", "luxembourg", "dominica", "singapore", "zimbabwe", "botswana",
        "barbados", "maldives", "kiribati", "mauritius", "slovenia", "honduras", "nicaragua",
        "suriname", "kyrgyzstan"
    )

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
}