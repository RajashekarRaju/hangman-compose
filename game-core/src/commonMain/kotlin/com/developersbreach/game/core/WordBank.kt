package com.developersbreach.game.core

import com.developersbreach.game.core.wordCategories.ANIMAL_WORDS
import com.developersbreach.game.core.wordCategories.COMPANY_WORDS
import com.developersbreach.game.core.wordCategories.COUNTRY_WORDS
import com.developersbreach.game.core.wordCategories.LANGUAGE_WORDS

val WORD_CATALOG = wordCatalog {
    animals(ANIMAL_WORDS)
    companies(COMPANY_WORDS)
    countries(COUNTRY_WORDS)
    languages(LANGUAGE_WORDS)
}
