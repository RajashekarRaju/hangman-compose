package com.developersbreach.game.core

import kotlin.test.Test
import kotlin.test.assertFailsWith

class WordCatalogDslTest {

    @Test
    fun `build succeeds when required difficulty coverage exists without very hard`() {
        val words = minimumCoverageWords()

        wordCatalog {
            countries(words)
            languages(words)
            companies(words)
            animals(words)
        }
    }

    @Test
    fun `duplicate words in category are rejected`() {
        val exception = assertFailsWith<IllegalArgumentException> {
            wordCatalog {
                countries("bear", "bear", "wolf", "lion", "deer", "raven", "falcon", "walrus", "jaguar")
                languages(minimumCoverageWords())
                companies(minimumCoverageWords())
                animals(minimumCoverageWords())
            }
        }

        kotlin.test.assertTrue(exception.message.orEmpty().contains("Duplicate words"))
    }

    @Test
    fun `invalid characters are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            wordCatalog {
                countries(minimumCoverageWords() + "fox123")
                languages(minimumCoverageWords())
                companies(minimumCoverageWords())
                animals(minimumCoverageWords())
            }
        }
    }

    @Test
    fun `missing required hard coverage fails`() {
        val wordsWithoutHard = listOf(
            "bear", "wolf", "lion", "deer", "goat",
            "falcon", "otters", "walrus", "lemurs", "monkey",
        )

        assertFailsWith<IllegalArgumentException> {
            wordCatalog {
                countries(wordsWithoutHard)
                languages(wordsWithoutHard)
                companies(wordsWithoutHard)
                animals(wordsWithoutHard)
            }
        }
    }

    private fun minimumCoverageWords(): List<String> {
        return listOf(
            "bear", "wolf", "lion", "deer", "goat",
            "falcon", "jaguar", "walrus", "parrot", "bobcat",
            "elephant", "kangaroo", "alligator", "chameleon", "salamander",
        )
    }
}
