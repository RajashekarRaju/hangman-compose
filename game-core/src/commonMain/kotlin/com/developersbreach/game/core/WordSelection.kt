package com.developersbreach.game.core

fun getFilteredWordsByGameDifficulty(
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory
): List<Words> {
    val words = WORD_CATALOG.wordsFor(gameCategory)
    val selectedRange = gameDifficulty.wordLengthRange()
    val selectedPool = words.filterWordsByLength(selectedRange)
    val finalPool = when {
        selectedPool.size == LEVELS_PER_GAME -> selectedPool
        gameDifficulty == GameDifficulty.VERY_HARD ->
            words.filterWordsByLength(GameDifficulty.HARD.wordLengthRange())
        else -> selectedPool
    }

    return finalPool
        .map { word -> Words(wordName = word) }
}

private fun List<String>.filterWordsByLength(
    range: IntRange,
    numberOfWords: Int = 5
): List<String> {
    return this.filter { it.playableLetterCount() in range }.shuffled().take(numberOfWords)
}