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
    val isShortHeight = maxHeight < 760.dp
    val rootHorizontalPadding = (maxWidth * 0.022f).coerceIn(16.dp, 28.dp)
    val rootTopPadding = when {
        isShortHeight -> (maxHeight * 0.04f).coerceIn(18.dp, 36.dp)
        else -> (maxHeight * 0.07f).coerceIn(52.dp, 72.dp)
    }
    val rootBottomPadding = when {
        isShortHeight -> (maxHeight * 0.018f).coerceIn(8.dp, 14.dp)
        else -> (maxHeight * 0.03f).coerceIn(16.dp, 28.dp)
    }
    val paneGap = (maxWidth * 0.032f).coerceIn(18.dp, 40.dp)
    val progressPaneWidth = (maxWidth * if (wideLayout) 0.32f else 0.28f).coerceIn(280.dp, 440.dp)
    val boardPaneWidth = (maxWidth * if (wideLayout) 0.56f else 0.92f).coerceIn(320.dp, 920.dp)
    val compactSectionSpacing = when {
        wideLayout && !isShortHeight -> (maxHeight * 0.016f).coerceIn(10.dp, 22.dp)
        isShortHeight -> (maxHeight * 0.014f).coerceIn(8.dp, 16.dp)
        else -> (maxHeight * 0.02f).coerceIn(12.dp, 28.dp)
    }

    val alphabetTileSize =
        (shortestSide * if (wideLayout) 0.086f else 0.095f).coerceIn(
            minimumValue = if (isShortHeight) 44.dp else 46.dp,
            maximumValue = if (wideLayout) 84.dp else 72.dp,
        )
    val alphabetSpacing = (alphabetTileSize * if (wideLayout) 0.16f else 0.23f).coerceIn(6.dp, 14.dp)
    val alphabetPadding = (alphabetTileSize * if (isShortHeight) 0.16f else if (wideLayout) 0.20f else 0.30f)
        .coerceIn(8.dp, 24.dp)
    val alphabetMaxHeight = when {
        wideLayout && isShortHeight -> (maxHeight * 0.56f).coerceIn(220.dp, 340.dp)
        wideLayout -> (maxHeight * 0.54f).coerceIn(260.dp, 580.dp)
        else -> (maxHeight * 0.48f).coerceIn(220.dp, 520.dp)
    }

    val guessedChipSize = (alphabetTileSize * if (wideLayout && !isShortHeight) 1.14f else if (isShortHeight) 1.0f else 1.08f)
        .coerceIn(42.dp, 84.dp)
    val guessedChipSpacing = (guessedChipSize * 0.14f).coerceIn(8.dp, 14.dp)
    val guessedHorizontalPadding = (guessedChipSize * 0.20f).coerceIn(10.dp, 22.dp)
    val guessedInnerPadding = (guessedChipSize * if (isShortHeight) 0.16f else 0.20f).coerceIn(8.dp, 16.dp)
    val progressScale = when {
        wideLayout && isShortHeight -> (shortestSide / 860.dp).coerceIn(0.80f, 0.98f)
        wideLayout -> (shortestSide / 710.dp).coerceIn(0.92f, 1.16f)
        else -> (shortestSide / 700.dp).coerceIn(0.84f, 1.0f)
    } * if (isShortHeight) 1.05f else if (wideLayout) 1.12f else 1.08f

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
