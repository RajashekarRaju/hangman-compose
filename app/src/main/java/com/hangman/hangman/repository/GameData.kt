package com.hangman.hangman.repository

import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.repository.database.entity.HistoryEntity


object GameData {

    val easyGuessingWords = listOf(
        "india", "china", "sudan", "iran", "libya", "chad", "peru", "egypt", "mali", "chile",
        "france", "kenya", "yemen", "spain", "iraq", "norway", "japan", "congo", "italy", "oman",
        "ghana", "laos", "syria", "nepal", "cuba", "togo", "haiti", "fiji", "qatar", "tonga",
        "malta", "nauru"
    )

    val mediumGuessingWords = listOf(
        "russia", "canada", "brazil", "algeria", "mexico", "angola", "bolivia", "nigeria", "turkey",
        "myanmar", "somalia", "ukraine", "morocco", "germany", "finland", "poland", "vietnam",
        "guinea", "uganda", "romania", "guyana", "belarus", "uruguay", "tunisia", "iceland",
        "jordan", "serbia", "austria", "ireland", "georgia", "panama", "ireland", "croatia",
        "denmark", "bhutan", "taiwan", "latvia"
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

val gameHistory = listOf(
    HistoryEntity(1, 3, 1, 2, true),
    HistoryEntity(2, 0, 1, 1, false),
    HistoryEntity(3, 12, 4, 2, true),
    HistoryEntity(4, 4, 2, 3, true),
    HistoryEntity(5, 3, 1, 2, true),
    HistoryEntity(6, 6, 2, 1, true),
    HistoryEntity(7, 15, 5, 1, true),
)