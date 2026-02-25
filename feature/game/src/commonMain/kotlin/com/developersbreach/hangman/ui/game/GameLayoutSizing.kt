package com.developersbreach.hangman.ui.game

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal data class GameLayoutSizing(
    val rootHorizontalPadding: Dp,
    val rootTopPadding: Dp,
    val rootBottomPadding: Dp,
    val paneGap: Dp,
    val compactSectionSpacing: Dp,
    val progressScale: Float,
    val guessedChipSize: Dp,
    val guessedChipSpacing: Dp,
    val guessedHorizontalPadding: Dp,
    val guessedInnerPadding: Dp,
    val guessedCreepiness: Float,
    val alphabetTileSize: Dp,
    val alphabetSpacing: Dp,
    val alphabetPadding: Dp,
    val alphabetCreepiness: Float,
)

internal fun compactGameLayoutSizing(isShortHeight: Boolean): GameLayoutSizing {
    return GameLayoutSizing(
        rootHorizontalPadding = 16.dp,
        rootTopPadding = if (isShortHeight) 8.dp else 16.dp,
        rootBottomPadding = if (isShortHeight) 10.dp else 18.dp,
        paneGap = 16.dp,
        compactSectionSpacing = if (isShortHeight) 8.dp else 12.dp,
        progressScale = if (isShortHeight) 0.96f else 1.02f,
        guessedChipSize = if (isShortHeight) 52.dp else 58.dp,
        guessedChipSpacing = 10.dp,
        guessedHorizontalPadding = 12.dp,
        guessedInnerPadding = 10.dp,
        guessedCreepiness = 0.18f,
        alphabetTileSize = if (isShortHeight) 50.dp else 56.dp,
        alphabetSpacing = if (isShortHeight) 7.dp else 8.dp,
        alphabetPadding = 10.dp,
        alphabetCreepiness = 0.08f,
    )
}

internal fun wideGameLayoutSizing(isShortHeight: Boolean): GameLayoutSizing {
    return GameLayoutSizing(
        rootHorizontalPadding = 160.dp,
        rootTopPadding = if (isShortHeight) 8.dp else 20.dp,
        rootBottomPadding = if (isShortHeight) 10.dp else 20.dp,
        paneGap = 24.dp,
        compactSectionSpacing = 12.dp,
        progressScale = if (isShortHeight) 1.03f else 1.10f,
        guessedChipSize = if (isShortHeight) 56.dp else 62.dp,
        guessedChipSpacing = 10.dp,
        guessedHorizontalPadding = 14.dp,
        guessedInnerPadding = 10.dp,
        guessedCreepiness = 0.18f,
        alphabetTileSize = if (isShortHeight) 52.dp else 58.dp,
        alphabetSpacing = if (isShortHeight) 7.dp else 8.dp,
        alphabetPadding = 10.dp,
        alphabetCreepiness = 0.08f,
    )
}
