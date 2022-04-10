package com.hangman.hangman.utils

import com.hangman.hangman.repository.GameData
import com.hangman.hangman.repository.database.entity.WordsEntity

/**
 * [EASY] -> Filters words of length 4 & 5.
 * [MEDIUM] -> Filters words of length 6 & 7
 * [HARD] -> Filters words of length 8 to 10
 */
enum class GameDifficulty {
    EASY, MEDIUM, HARD
}

/**
 * Filter between three different guessing words lists based on [GameDifficulty].
 */
fun getFilteredWordsByGameDifficulty(
    gameDifficulty: GameDifficulty
): List<WordsEntity> {
    val wordsList = ArrayList<WordsEntity>()
    // First get the result list from difficulty and map those to WordsEntity.
    with(filterWordsByDifficulty(gameDifficulty)) {
        this.map { word ->
            wordsList.add(
                WordsEntity(wordName = word)
            )
        }
    }

    // Make sure to return only 5 shuffled words instead of whole list.
    return getFiveUniqueGuessingWords(wordsList)
}

/**
 * Return only 5 words from the whole list of guessing words.
 * Since we will be having 5 levels per each game, one word per level.
 */
private fun getFiveUniqueGuessingWords(
    wordsList: ArrayList<WordsEntity>
): List<WordsEntity> {
    return wordsList.shuffled().take(5)
}

/**
 * Determine current difficulty to get correct list from the object [GameData].
 */
private fun filterWordsByDifficulty(
    gameDifficulty: GameDifficulty,
    data: GameData = GameData
) = when (gameDifficulty) {
    GameDifficulty.EASY -> data.easyGuessingWords
    GameDifficulty.MEDIUM -> data.mediumGuessingWords
    GameDifficulty.HARD -> data.hardGuessingWords
}