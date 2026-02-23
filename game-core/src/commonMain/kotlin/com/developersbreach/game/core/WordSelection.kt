package com.developersbreach.game.core

fun getFilteredWordsByGameDifficulty(
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory
): List<Words> {
    val wordsList = ArrayList<Words>()
    with(
        when (gameCategory) {
            GameCategory.COUNTRIES -> countryData()
            GameCategory.LANGUAGES -> languageData()
            GameCategory.COMPANIES -> companyData()
        }
    ) {
        when (gameDifficulty) {
            GameDifficulty.EASY -> this.filterWordsByLength(4..5)
            GameDifficulty.MEDIUM -> this.filterWordsByLength(6..7)
            GameDifficulty.HARD -> this.filterWordsByLength(8..10)
        }.forEach { word ->
            wordsList.add(
                Words(wordName = word)
            )
        }
    }

    return wordsList
}

private fun List<String>.filterWordsByLength(
    range: IntRange,
    numberOfWords: Int = 5
): List<String> {
    return this.filter { it.playableLetterCount() in range }.shuffled().take(numberOfWords)
}

private fun String.playableLetterCount(): Int {
    return count { character -> character.isLetter() }
}
