package com.hangman.hangman.repository

import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.repository.database.entity.WordsEntity

fun words(): List<WordsEntity> {

    val guessingWords = listOf("Blue", "Red", "Purple", "Pink")

    val wordsList = ArrayList<WordsEntity>()
    guessingWords.forEach {
        val wordsEntity = WordsEntity(wordName = it)
        wordsList.add(wordsEntity)
    }

    return wordsList
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