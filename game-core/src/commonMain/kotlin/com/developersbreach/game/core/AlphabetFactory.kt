package com.developersbreach.game.core

fun alphabetsList(): List<Alphabet> {
    val alphabet = ('A'..'Z').toList()
    val alphabetId = (1..26).toList()
    val zipAlphabets = alphabetId zip alphabet

    val alphabetList: MutableList<Alphabet> = mutableListOf()

    zipAlphabets.forEach { pair ->
        alphabetList.add(
            Alphabet(
                alphabetId = pair.first,
                alphabet = pair.second.toString()
            )
        )
    }

    return alphabetList
}
