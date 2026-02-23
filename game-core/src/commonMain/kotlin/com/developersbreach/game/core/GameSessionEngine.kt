package com.developersbreach.game.core

class GameSessionEngine(
    private val guessingWordsForCurrentGame: List<Words>,
    private val maxAttempts: Int = MAX_ATTEMPTS_PER_LEVEL,
    private val levelsPerGame: Int = LEVELS_PER_GAME,
    private val hintsPerLevel: Int = 1,
    private val hintEliminationCount: Int = DEFAULT_HINT_ELIMINATION_COUNT,
) {
    private var attemptsLeftToGuess: Int = maxAttempts
    private var pointsScoredPerWord: Int = 0
    private var pointsScoredOverall: Int = 0
    private var currentPlayerLevel: Int = 0
    private var gameOverByWinning: Boolean = false
    private var gameOverByNoAttemptsLeft: Boolean = false
    private val pointsScoredInEachLevel = MutableList(levelsPerGame) { 0 }
    private var hintsRemaining: Int = hintsPerLevel
    private var hintsUsedTotal: Int = 0
    private val hintTypesUsed = linkedSetOf<HintType>()

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
        require(hintsPerLevel >= 0) {
            "hintsPerLevel must be >= 0."
        }
        require(hintEliminationCount > 0) {
            "hintEliminationCount must be > 0."
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
            maxLevelReached = levelsPerGame,
            hintsRemaining = hintsRemaining,
            hintsUsedTotal = hintsUsedTotal,
            hintTypesUsed = hintTypesUsed.toSet(),
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

    fun applyHint(type: HintType): GameSessionUpdate {
        if (!canAcceptGuess()) {
            return hintErrorUpdate(type, HintError.GAME_ALREADY_FINISHED)
        }
        if (hintsRemaining <= 0) {
            return hintErrorUpdate(type, HintError.NO_HINTS_REMAINING)
        }

        return when (type) {
            HintType.REVEAL_LETTER -> applyRevealLetterHint()
            HintType.ELIMINATE_LETTERS -> applyEliminateLettersHint()
        }
    }

    private fun resetGuessesForCurrentWord() {
        playerGuesses.clear()
        currentWord.forEach { character ->
            when (character) {
                ' ' -> playerGuesses.add(WORD_SEPARATOR)
                else -> playerGuesses.add(UNGUESSED_SLOT)
            }
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

    private fun hintErrorUpdate(type: HintType, error: HintError): GameSessionUpdate {
        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = false,
            gameWon = false,
            gameLost = false,
            hintType = type,
            hintApplied = false,
            hintError = error,
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
        if (playerGuesses.contains(UNGUESSED_SLOT)) {
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
        pointsScoredPerWord = currentWord.playableLetterCount()
        calculateOverallPointsScoredEachLevel()
        attemptsLeftToGuess = maxAttempts

        if (currentPlayerLevel < levelsPerGame) {
            currentPlayerLevel += 1
        }

        if (currentPlayerLevel < levelsPerGame) {
            currentWord = guessingWordsForCurrentGame[currentPlayerLevel].wordName
            alphabets = alphabets.map { it.copy(isAlphabetGuessed = false) }
            hintsRemaining = hintsPerLevel
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

    private fun applyRevealLetterHint(): GameSessionUpdate {
        val unrevealedIndexes = playerGuesses
            .withIndex()
            .filter { it.value == UNGUESSED_SLOT }
            .map { it.index }

        if (unrevealedIndexes.isEmpty()) {
            return hintErrorUpdate(HintType.REVEAL_LETTER, HintError.NO_UNREVEALED_LETTERS)
        }

        val revealedIndex = unrevealedIndexes.first()
        val revealedCharacter = currentWord[revealedIndex].lowercaseChar().toString()
        playerGuesses[revealedIndex] = revealedCharacter
        markAlphabetAsGuessedForLetter(revealedCharacter)
        consumeHint(HintType.REVEAL_LETTER)

        val isLevelCompleted = !playerGuesses.contains(UNGUESSED_SLOT)
        val gameWon = if (isLevelCompleted) completeCurrentLevel() else false

        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = isLevelCompleted,
            gameWon = gameWon,
            gameLost = false,
            hintType = HintType.REVEAL_LETTER,
            hintApplied = true,
            revealedIndexes = listOf(revealedIndex),
        )
    }

    private fun applyEliminateLettersHint(): GameSessionUpdate {
        val currentWordLower = currentWord.lowercase()
        val candidates = alphabets.filter { alphabet ->
            !alphabet.isAlphabetGuessed && !currentWordLower.contains(alphabet.alphabet.lowercase())
        }

        if (candidates.isEmpty()) {
            return hintErrorUpdate(HintType.ELIMINATE_LETTERS, HintError.NO_ELIMINATION_CANDIDATES)
        }

        val eliminated = candidates.take(hintEliminationCount)
        val eliminatedIds = eliminated.map { it.alphabetId }
        alphabets = alphabets.map { alphabet ->
            when (alphabet.alphabetId) {
                in eliminatedIds -> alphabet.copy(isAlphabetGuessed = true)
                else -> alphabet
            }
        }
        consumeHint(HintType.ELIMINATE_LETTERS)

        return GameSessionUpdate(
            state = snapshot(),
            levelCompleted = false,
            gameWon = false,
            gameLost = false,
            hintType = HintType.ELIMINATE_LETTERS,
            hintApplied = true,
            eliminatedAlphabetIds = eliminatedIds,
        )
    }

    private fun consumeHint(type: HintType) {
        hintsRemaining = (hintsRemaining - 1).coerceAtLeast(0)
        hintsUsedTotal += 1
        hintTypesUsed += type
    }

    private fun markAlphabetAsGuessedForLetter(letter: String) {
        val alphabetId = alphabets.firstOrNull { it.alphabet.lowercase() == letter }?.alphabetId ?: return
        markAlphabetAsGuessed(alphabetId)
    }

    private companion object {
        const val UNGUESSED_SLOT = ""
        const val WORD_SEPARATOR = " "
    }
}
