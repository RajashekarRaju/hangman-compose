package com.developersbreach.game.core

class GameSessionEngine(
    private val guessingWordsForCurrentGame: List<Words>,
    private val maxAttempts: Int = MAX_ATTEMPTS_PER_LEVEL,
    private val levelsPerGame: Int = LEVELS_PER_GAME
) {
    private var attemptsLeftToGuess: Int = maxAttempts
    private var pointsScoredPerWord: Int = 0
    private var pointsScoredOverall: Int = 0
    private var currentPlayerLevel: Int = 0
    private var gameOverByWinning: Boolean = false
    private var gameOverByNoAttemptsLeft: Boolean = false
    private val pointsScoredInEachLevel = MutableList(levelsPerGame) { 0 }

    private var currentWord: String = ""
    private var alphabets: List<Alphabet> = alphabetsList()
    private val playerGuesses = mutableListOf<String>()

    init {
        require(levelsPerGame > 0) {
            "levelsPerGame must be greater than 0."
        }
        require(maxAttempts > 0) {
            "maxAttempts must be greater than 0."
        }
        require(guessingWordsForCurrentGame.size >= levelsPerGame) {
            "Expected at least $levelsPerGame words for a game session."
        }
        currentWord = guessingWordsForCurrentGame[currentPlayerLevel].wordName
        resetGuessesForCurrentWord()
    }

    fun snapshot(): GameSessionState {
        return GameSessionState(
            alphabets = alphabets,
            playerGuesses = playerGuesses.toList(),
            currentWord = currentWord,
            attemptsLeftToGuess = attemptsLeftToGuess,
            currentPlayerLevel = currentPlayerLevel,
            pointsScoredOverall = pointsScoredOverall,
            gameOverByWinning = gameOverByWinning,
            gameOverByNoAttemptsLeft = gameOverByNoAttemptsLeft,
            maxLevelReached = levelsPerGame
        )
    }

    fun guessAlphabet(alphabetId: Int): GameSessionUpdate {
        if (!canAcceptGuess()) {
            return noOpUpdate()
        }

        val alphabet = getAvailableAlphabet(alphabetId) ?: return noOpUpdate()
        markAlphabetAsGuessed(alphabetId)

        return if (isCorrectGuess(alphabet)) {
            handleCorrectGuess(alphabet)
        } else {
            handleIncorrectGuess()
        }
    }

    private fun resetGuessesForCurrentWord() {
        playerGuesses.clear()
        repeat(currentWord.length) {
            playerGuesses.add(" ")
        }
    }

    private fun canAcceptGuess(): Boolean {
        return !gameOverByWinning && !gameOverByNoAttemptsLeft
    }

    private fun noOpUpdate(): GameSessionUpdate {
        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = false,
            gameWon = false,
            gameLost = false
        )
    }

    private fun getAvailableAlphabet(alphabetId: Int): Alphabet? {
        val alphabet = alphabets.firstOrNull { it.alphabetId == alphabetId } ?: return null
        return if (alphabet.isAlphabetGuessed) null else alphabet
    }

    private fun markAlphabetAsGuessed(alphabetId: Int) {
        alphabets = alphabets.map {
            if (it.alphabetId == alphabetId) it.copy(isAlphabetGuessed = true) else it
        }
    }

    private fun isCorrectGuess(alphabet: Alphabet): Boolean {
        val currentAlphabet: String = alphabet.alphabet.lowercase()
        val currentGuessingWord: String = currentWord.lowercase()
        return currentGuessingWord.contains(currentAlphabet)
    }

    private fun handleCorrectGuess(alphabet: Alphabet): GameSessionUpdate {
        revealMatchedAlphabetPositions(alphabet)
        if (playerGuesses.contains(" ")) {
            return GameSessionUpdate(
                state = snapshot(),
                levelCompleted = false,
                gameWon = false,
                gameLost = false
            )
        }

        val gameWon = completeCurrentLevel()
        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = true,
            gameWon = gameWon,
            gameLost = false
        )
    }

    private fun revealMatchedAlphabetPositions(alphabet: Alphabet) {
        val currentAlphabet = alphabet.alphabet.lowercase()
        val currentGuessingWord = currentWord.lowercase()
        for (index in currentGuessingWord.indices) {
            if (currentGuessingWord[index].toString() == currentAlphabet) {
                playerGuesses[index] = currentAlphabet
            }
        }
    }

    private fun completeCurrentLevel(): Boolean {
        pointsScoredPerWord = currentWord.length
        calculateOverallPointsScoredEachLevel()
        attemptsLeftToGuess = maxAttempts

        if (currentPlayerLevel < levelsPerGame) {
            currentPlayerLevel += 1
        }

        if (currentPlayerLevel < levelsPerGame) {
            currentWord = guessingWordsForCurrentGame[currentPlayerLevel].wordName
            alphabets = alphabets.map { it.copy(isAlphabetGuessed = false) }
            resetGuessesForCurrentWord()
            return false
        }

        gameOverByWinning = true
        return true
    }

    private fun handleIncorrectGuess(): GameSessionUpdate {
        if (attemptsLeftToGuess > 0) {
            attemptsLeftToGuess -= 1
            gameOverByNoAttemptsLeft = attemptsLeftToGuess == 0
        }
        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = false,
            gameWon = false,
            gameLost = gameOverByNoAttemptsLeft
        )
    }

    private fun calculateOverallPointsScoredEachLevel() {
        val completedLevelIndex = currentPlayerLevel
        if (completedLevelIndex in pointsScoredInEachLevel.indices) {
            pointsScoredInEachLevel[completedLevelIndex] = pointsScoredPerWord
        }
        pointsScoredOverall = pointsScoredInEachLevel.sum()
    }
}
