package com.developersbreach.hangman.repository.storage

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.serialization.Serializable

@Serializable
data class StoredHistoryRecord(
    val gameId: String,
    val gameScore: Int,
    val gameLevel: Int,
    val gameDifficulty: String,
    val gameCategory: String,
    val gameSummary: Boolean,
    val gamePlayedTime: String,
    val gamePlayedDate: String,
    val hintsUsed: Int = 0,
    val hintTypesUsed: List<String> = emptyList(),
)

fun StoredHistoryRecord.toDomain(): HistoryRecord {
    return HistoryRecord(
        gameId = gameId,
        gameScore = gameScore,
        gameLevel = gameLevel,
        gameDifficulty = gameDifficulty.toGameDifficulty(),
        gameCategory = gameCategory.toGameCategory(),
        gameSummary = gameSummary,
        gamePlayedTime = gamePlayedTime,
        gamePlayedDate = gamePlayedDate,
        hintsUsed = hintsUsed,
        hintTypesUsed = hintTypesUsed.mapNotNull { value ->
            runCatching { HintType.valueOf(value) }.getOrNull()
        },
    )
}

fun HistoryRecord.toStored(): StoredHistoryRecord {
    return StoredHistoryRecord(
        gameId = gameId,
        gameScore = gameScore,
        gameLevel = gameLevel,
        gameDifficulty = gameDifficulty.name,
        gameCategory = gameCategory.name,
        gameSummary = gameSummary,
        gamePlayedTime = gamePlayedTime,
        gamePlayedDate = gamePlayedDate,
        hintsUsed = hintsUsed,
        hintTypesUsed = hintTypesUsed.map { it.name },
    )
}

@Serializable
data class StoredSettings(
    val gameDifficulty: String = GameDifficulty.EASY.name,
    val gameCategory: String = GameCategory.COUNTRIES.name,
    val themePaletteId: String = "INSANE_RED",
)

fun String.toGameDifficulty(): GameDifficulty {
    return runCatching { GameDifficulty.valueOf(this) }.getOrDefault(GameDifficulty.EASY)
}

fun String.toGameCategory(): GameCategory {
    return runCatching { GameCategory.valueOf(this) }.getOrDefault(GameCategory.COUNTRIES)
}
