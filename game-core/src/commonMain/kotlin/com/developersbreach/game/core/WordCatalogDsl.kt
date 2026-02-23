package com.developersbreach.game.core

private const val WORDS_REQUIRED_PER_MATCH = LEVELS_PER_GAME

data class WordCatalog(
    private val wordsByCategory: Map<GameCategory, List<String>>,
) {
    fun wordsFor(category: GameCategory): List<String> {
        return wordsByCategory.getValue(category)
    }
}

class WordCatalogBuilder {

    private val rawWordsByCategory = mutableMapOf<GameCategory, MutableList<String>>()

    fun countries(vararg words: String) {
        addWords(GameCategory.COUNTRIES, words)
    }

    fun countries(words: Iterable<String>) {
        addWords(GameCategory.COUNTRIES, words)
    }

    fun languages(vararg words: String) {
        addWords(GameCategory.LANGUAGES, words)
    }

    fun languages(words: Iterable<String>) {
        addWords(GameCategory.LANGUAGES, words)
    }

    fun companies(vararg words: String) {
        addWords(GameCategory.COMPANIES, words)
    }

    fun companies(words: Iterable<String>) {
        addWords(GameCategory.COMPANIES, words)
    }

    fun animals(vararg words: String) {
        addWords(GameCategory.ANIMALS, words)
    }

    fun animals(words: Iterable<String>) {
        addWords(GameCategory.ANIMALS, words)
    }

    internal fun build(): WordCatalog {
        val normalizedByCategory = rawWordsByCategory.mapValues { (category, rawWords) ->
            normalizeAndValidateWords(category, rawWords)
        }

        GameCategory.entries.forEach { category ->
            require(normalizedByCategory[category]?.isNotEmpty() == true) {
                "Word catalog category '$category' must not be empty."
            }
        }

        validateDifficultyCoverage(normalizedByCategory)

        return WordCatalog(wordsByCategory = normalizedByCategory)
    }

    private fun addWords(category: GameCategory, words: Array<out String>) {
        addWords(category, words.asIterable())
    }

    private fun addWords(category: GameCategory, words: Iterable<String>) {
        val bucket = rawWordsByCategory.getOrPut(category) { mutableListOf() }
        bucket += words
    }

    private fun normalizeAndValidateWords(
        category: GameCategory,
        rawWords: List<String>,
    ): List<String> {
        val normalized = rawWords.mapIndexed { index, raw ->
            val value = raw
                .trim()
                .replace('-', ' ')
                .lowercase()
                .replace(Regex("\\s+"), " ")

            require(value.isNotBlank()) {
                "Empty word at index $index in category '$category'."
            }
            require(value.all { it.isLetter() || it == ' ' }) {
                "Invalid characters in word '$raw' in category '$category'. Use letters, spaces, or hyphens only."
            }
            value
        }

        val duplicates = normalized
            .groupingBy { it }
            .eachCount()
            .filterValues { it > 1 }
            .keys
            .sorted()

        require(duplicates.isEmpty()) {
            "Duplicate words found in category '$category': ${duplicates.joinToString()}"
        }

        return normalized
    }

    private fun validateDifficultyCoverage(wordsByCategory: Map<GameCategory, List<String>>) {
        wordsByCategory.forEach { (category, words) ->
            REQUIRED_DIFFICULTY_COVERAGE.forEach { difficulty ->
                val range = WORD_LENGTH_RANGE_BY_DIFFICULTY.getValue(difficulty)
                val count = words.count { word -> word.playableLetterCount() in range }
                require(count >= WORDS_REQUIRED_PER_MATCH) {
                    "Category '$category' does not have enough words for '$difficulty' " +
                        "(${count}/$WORDS_REQUIRED_PER_MATCH for letter range ${range.asReadableLabel()})."
                }
            }
        }
    }
}

private fun IntRange.asReadableLabel(): String {
    return if (last == Int.MAX_VALUE) "$first+" else "$first-$last"
}

fun wordCatalog(block: WordCatalogBuilder.() -> Unit): WordCatalog {
    val builder = WordCatalogBuilder()
    builder.block()
    return builder.build()
}
