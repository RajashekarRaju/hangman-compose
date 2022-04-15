package com.hangman.hangman.utils

import com.hangman.hangman.repository.*

/**
 * [EASY] -> Filters words of length 4 & 5.
 * [MEDIUM] -> Filters words of length 6 & 7
 * [HARD] -> Filters words of length 8 to 10
 */
enum class GameDifficulty {
    EASY, MEDIUM, HARD
}

/**
 * [COUNTRIES]
 * [LANGUAGES]
 * [COMPANIES]
 */
enum class GameCategory {
    COUNTRIES, LANGUAGES, COMPANIES
}

// For adding list of 5 words to guessing list.
data class Words(
    val wordName: String
)

/**
 * @param gameDifficulty filter between three different guessing words lists based on [GameDifficulty].
 * @param gameCategory filter game category added to [GameCategory].
 *
 * Return only 5 words from the whole list of guessing words.
 * Since we will be having 5 levels per each game, one word per level.
 */
fun getFilteredWordsByGameDifficulty(
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory
): List<Words> {
    val wordsList = ArrayList<Words>()
    // First get the result list by filtering category.
    with(
        when (gameCategory) {
            GameCategory.COUNTRIES -> countryData()
            GameCategory.LANGUAGES -> languageData()
            GameCategory.COMPANIES -> companyData()
        }
    ) {
        // From the selected category filter the result list to difficulty.
        when (gameDifficulty) {
            GameDifficulty.EASY -> this.filterWordsByLength(4..5)
            GameDifficulty.MEDIUM -> this.filterWordsByLength(6..7)
            GameDifficulty.HARD -> this.filterWordsByLength(8..10)
        }.map { word ->
            wordsList.add(
                Words(wordName = word)
            )
        }
    }

    return wordsList
}

/**
 * Return only 5 words from the whole list of guessing words.
 * Since we will be having 5 levels per each game, one word per level.
 */
private fun List<String>.filterWordsByLength(
    range: IntRange,
    numberOfWords: Int = 5
): List<String> {
    return this.filter { it.length in range }.shuffled().take(numberOfWords)
}