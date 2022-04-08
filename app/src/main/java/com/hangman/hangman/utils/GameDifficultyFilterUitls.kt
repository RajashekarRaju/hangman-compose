package com.hangman.hangman.utils

import com.hangman.hangman.repository.GameData
import com.hangman.hangman.repository.database.entity.WordsEntity


fun getFilteredWordsByGameDifficulty(
    gameDifficulty: GameDifficulty
): List<WordsEntity> {
    val wordsList = ArrayList<WordsEntity>()
    with(filterWordsByDifficulty(gameDifficulty)) {
        this.map { word ->
            wordsList.add(WordsEntity(wordName = word))
        }
    }

    return getFiveUniqueGuessingWords(wordsList)
}

private fun getFiveUniqueGuessingWords(
    wordsList: ArrayList<WordsEntity>
): List<WordsEntity> {
    return wordsList.shuffled().take(5)
}

private fun filterWordsByDifficulty(
    gameDifficulty: GameDifficulty,
    data: GameData = GameData
) = when (gameDifficulty) {
    GameDifficulty.EASY -> data.easyGuessingWords
    GameDifficulty.MEDIUM -> data.mediumGuessingWords
    GameDifficulty.HARD -> data.hardGuessingWords
}