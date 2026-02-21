package com.developersbreach.hangman.ui.game

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

internal data class GameResponsiveSizing(
    val rootHorizontalPadding: Dp,
    val rootTopPadding: Dp,
    val rootBottomPadding: Dp,
    val paneGap: Dp,
    val progressPaneWidth: Dp,
    val progressScale: Float,
    val boardPaneWidth: Dp,
    val compactSectionSpacing: Dp,
    val guessedChipSize: Dp,
    val guessedChipSpacing: Dp,
    val guessedHorizontalPadding: Dp,
    val guessedInnerPadding: Dp,
    val guessedCreepiness: Float,
    val alphabetTileSize: Dp,
    val alphabetSpacing: Dp,
    val alphabetPadding: Dp,
    val alphabetMaxHeight: Dp,
    val alphabetCreepiness: Float,
)

internal fun calculateGameResponsiveSizing(
    maxWidth: Dp,
    maxHeight: Dp,
    wideLayout: Boolean,
): GameResponsiveSizing {
    val shortestSide = if (maxWidth < maxHeight) maxWidth else maxHeight
    val rootHorizontalPadding = (maxWidth * 0.022f).coerceIn(16.dp, 28.dp)
    val rootTopPadding = (maxHeight * 0.07f).coerceIn(52.dp, 72.dp)
    val rootBottomPadding = (maxHeight * 0.03f).coerceIn(16.dp, 28.dp)
    val paneGap = (maxWidth * 0.032f).coerceIn(18.dp, 40.dp)
    val progressPaneWidth = (maxWidth * 0.28f).coerceIn(240.dp, 380.dp)
    val boardPaneWidth = (maxWidth * if (wideLayout) 0.52f else 0.92f).coerceIn(320.dp, 860.dp)
    val compactSectionSpacing = (maxHeight * 0.02f).coerceIn(12.dp, 28.dp)

    val alphabetTileSize =
        (shortestSide * if (wideLayout) 0.078f else 0.095f).coerceIn(46.dp, 72.dp)
    val alphabetSpacing = (alphabetTileSize * 0.23f).coerceIn(8.dp, 16.dp)
    val alphabetPadding = (alphabetTileSize * 0.30f).coerceIn(12.dp, 26.dp)
    val alphabetMaxHeight = (maxHeight * if (wideLayout) 0.54f else 0.48f).coerceIn(340.dp, 580.dp)

    val guessedChipSize = (alphabetTileSize * 1.08f).coerceIn(44.dp, 72.dp)
    val guessedChipSpacing = (guessedChipSize * 0.14f).coerceIn(8.dp, 14.dp)
    val guessedHorizontalPadding = (guessedChipSize * 0.20f).coerceIn(10.dp, 22.dp)
    val guessedInnerPadding = (guessedChipSize * 0.20f).coerceIn(10.dp, 16.dp)
    val progressScale = when {
        wideLayout -> (shortestSide / 760.dp).coerceIn(0.86f, 1.08f)
        else -> (shortestSide / 700.dp).coerceIn(0.84f, 1.0f)
    } * 1.08f

    return GameResponsiveSizing(
        rootHorizontalPadding = rootHorizontalPadding,
        rootTopPadding = rootTopPadding,
        rootBottomPadding = rootBottomPadding,
        paneGap = paneGap,
        progressPaneWidth = progressPaneWidth,
        progressScale = progressScale,
        boardPaneWidth = boardPaneWidth,
        compactSectionSpacing = compactSectionSpacing,
        guessedChipSize = guessedChipSize,
        guessedChipSpacing = guessedChipSpacing,
        guessedHorizontalPadding = guessedHorizontalPadding,
        guessedInnerPadding = guessedInnerPadding,
        guessedCreepiness = 0.18f,
        alphabetTileSize = alphabetTileSize,
        alphabetSpacing = alphabetSpacing,
        alphabetPadding = alphabetPadding,
        alphabetMaxHeight = alphabetMaxHeight,
        alphabetCreepiness = 0.08f,
    )
}
