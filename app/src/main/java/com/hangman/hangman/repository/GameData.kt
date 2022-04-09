package com.hangman.hangman.repository

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
        "lithuania", "netherlands", "switzerland", "palestine", "luxembourg", "dominica", "singapore",
        "barbados", "maldives", "kiribati", "mauritius", "slovenia", "honduras", "nicaragua",
        "suriname", "kyrgyzstan", "zimbabwe", "botswana"
    )
}

val alphabetsList = listOf(
    Alphabets(1, "A"),
    Alphabets(2, "B"),
    Alphabets(3, "C"),
    Alphabets(4, "D"),
    Alphabets(5, "E"),
    Alphabets(6, "F"),
    Alphabets(7, "G"),
    Alphabets(8, "H"),
    Alphabets(9, "I"),
    Alphabets(10, "J"),
    Alphabets(11, "K"),
    Alphabets(12, "L"),
    Alphabets(13, "M"),
    Alphabets(14, "N"),
    Alphabets(15, "O"),
    Alphabets(16, "P"),
    Alphabets(17, "Q"),
    Alphabets(18, "R"),
    Alphabets(19, "S"),
    Alphabets(20, "T"),
    Alphabets(21, "U"),
    Alphabets(22, "V"),
    Alphabets(23, "W"),
    Alphabets(24, "X"),
    Alphabets(25, "Y"),
    Alphabets(26, "Z")
)