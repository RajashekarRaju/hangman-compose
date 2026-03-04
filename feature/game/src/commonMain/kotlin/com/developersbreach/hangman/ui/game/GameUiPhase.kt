package com.developersbreach.hangman.ui.game

sealed interface GameUiPhase {
    data object Playing : GameUiPhase

    data class LevelSuccess(
        val step: Step,
    ) : GameUiPhase {
        enum class Step {
            SHIMMER,
            RETURN,
        }
    }

    data object FinalWinHold : GameUiPhase
    data object WinDialog : GameUiPhase
    data object LossHold : GameUiPhase
    data object LossDialog : GameUiPhase
}

internal fun resolvePhaseAfterStateSync(
    currentPhase: GameUiPhase,
    gameWon: Boolean,
    gameLost: Boolean,
): GameUiPhase {
    return when {
        gameWon -> when (currentPhase) {
            GameUiPhase.FinalWinHold,
            GameUiPhase.WinDialog -> currentPhase
            else -> GameUiPhase.Playing
        }

        gameLost -> when (currentPhase) {
            GameUiPhase.LossDialog -> GameUiPhase.LossDialog
            else -> GameUiPhase.LossHold
        }

        else -> when (currentPhase) {
            is GameUiPhase.LevelSuccess,
            GameUiPhase.FinalWinHold,
            GameUiPhase.WinDialog,
            GameUiPhase.LossHold,
            GameUiPhase.LossDialog -> GameUiPhase.Playing
            GameUiPhase.Playing -> GameUiPhase.Playing
        }
    }
}
