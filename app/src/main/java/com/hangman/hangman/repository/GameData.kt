package com.hangman.hangman.repository


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

    val instructionsList = listOf(
        "1. Game has 5 levels, you will win only if you complete all of those.",
        "2. Points per level will be allotted based on bigger the word is you guess.",
        "3. You will be having 8 guessed by default for each level, will reset everytime.",
        "4. For each wrong letter you guess, a attempt will be reduced from 8 guesses.",
        "5. For each correct letter you guess, attempt will not be reduced.",
        "6. You can check all your previous game scoring history in history screen.",
        "7. You have 3 types of difficulties to play the game such as easy, medium, hard. You"
    )
}