package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.logging.AuditSpec

sealed interface GameEvent {
    data object BackPressed : GameEvent
    data object ExitConfirmed : GameEvent
    data object ExitDismissed : GameEvent
    data object ToggleGameGuideOverlay : GameEvent
    data class HintSelected(val hintType: HintType) : GameEvent
    data object DismissHintFeedbackDialog : GameEvent
    data class AlphabetClicked(val alphabetId: Int) : GameEvent
    data object WinDialogDismissed : GameEvent
    data object LostDialogDismissed : GameEvent
}

internal fun GameEvent.auditSpec(current: GameUiState): AuditSpec? = when (this) {
    GameEvent.ToggleGameGuideOverlay -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "game",
            "visible_after_toggle" to (!current.showGameGuideOverlay).toString(),
            "level" to current.displayedLevel.toString(),
        ),
    )
    is GameEvent.HintSelected -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "game",
            "hint_type" to hintType.name,
            "hints_remaining" to current.hintsRemaining.toString(),
            "attempts_left" to current.attemptsLeftToGuess.toString(),
            "word_length" to current.wordToGuess.count { !it.isWhitespace() }.toString(),
        ),
    )
    GameEvent.WinDialogDismissed -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "game",
            "points" to current.pointsScoredOverall.toString(),
            "level" to current.displayedLevel.toString(),
        ),
    )
    GameEvent.LostDialogDismissed -> AuditSpec(
        eventType = "$this",
        parameters = mapOf(
            "screen" to "game",
            "points" to current.pointsScoredOverall.toString(),
            "level" to current.displayedLevel.toString(),
            "word_length" to current.wordToGuess.count { !it.isWhitespace() }.toString(),
        ),
    )
    GameEvent.BackPressed,
    GameEvent.ExitConfirmed,
    is GameEvent.AlphabetClicked,
    GameEvent.ExitDismissed,
    GameEvent.DismissHintFeedbackDialog, -> null
}
