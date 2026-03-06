package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangmanDialog(
    onDismissRequest: () -> Unit,
    seed: Int,
    headerTitle: String? = null,
    modifier: Modifier = Modifier,
    threshold: Float = 0.10f,
    containerColor: Color = HangmanTheme.colorScheme.surfaceContainer,
    headerIconRes: DrawableResource? = null,
    headerIconContentDescription: String? = null,
    headerIconContent: (@Composable () -> Unit)? = null,
    headerTitleColor: Color = HangmanTheme.colorScheme.onSurfaceVariant,
    headerPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
    footerPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    footerButtons: List<HangmanDialogFooterButton> = emptyList(),
    footerContent: (@Composable RowScope.() -> Unit)? = null,
    content: (@Composable ColumnScope.() -> Unit)? = null,
) {
    val creepyPhase = rememberCreepyPhase(durationMillis = 4200)
    val hasHeader = headerTitle != null || headerIconRes != null || headerIconContent != null
    val hasMiddleContent = content != null
    val hasFooter = footerButtons.isNotEmpty() || footerContent != null

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .creepyOutline(
                    seed = seed,
                    threshold = threshold,
                    fillColor = containerColor,
                    outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
                    forceRectangular = true,
                    strokeWidthFactor = 0.026f,
                    phase = creepyPhase,
                ),
        ) {
            if (hasHeader) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(headerPadding),
                ) {
                    Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    when {
                        headerIconContent != null -> headerIconContent()
                        headerIconRes != null -> {
                            HangmanIcon(
                                painter = painterResource(headerIconRes),
                                contentDescription = headerIconContentDescription,
                                tint = headerTitleColor,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                    if (headerTitle != null && (headerIconRes != null || headerIconContent != null)) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    if (headerTitle != null) {
                        TitleLargeText(
                            text = headerTitle,
                            color = headerTitleColor,
                        )
                    }
                }
                DialogSectionSeparator(seed = seed + 101)
            }

            if (hasMiddleContent) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(contentPadding),
                    content = content,
                )
            }

            if (hasFooter) {
                if (hasMiddleContent) {
                    DialogSectionSeparator(
                        seed = seed + 211,
                        height = 1.dp,
                        alpha = 0.25f,
                        paddingHorizontal = 48.dp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(footerPadding),
                ) {
                    when {
                        footerContent != null -> footerContent()
                        footerButtons.isNotEmpty() -> {
                            footerButtons.forEach { button ->
                                HangmanTextActionButton(
                                    text = button.text,
                                    color = button.color,
                                    onClick = button.onClick,
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

@Composable
fun HangmanDialog(
    onDismissRequest: () -> Unit,
    seed: Int,
    contentText: String,
    headerTitle: String? = null,
    modifier: Modifier = Modifier,
    threshold: Float = 0.10f,
    containerColor: Color = HangmanTheme.colorScheme.surfaceContainer,
    headerIconRes: DrawableResource? = null,
    headerIconContentDescription: String? = null,
    headerIconContent: (@Composable () -> Unit)? = null,
    headerTitleColor: Color = HangmanTheme.colorScheme.primary,
    headerPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 18.dp),
    footerPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    footerButtons: List<HangmanDialogFooterButton> = emptyList(),
    footerContent: (@Composable RowScope.() -> Unit)? = null,
) {
    HangmanDialog(
        onDismissRequest = onDismissRequest,
        seed = seed,
        headerTitle = headerTitle,
        modifier = modifier,
        threshold = threshold,
        containerColor = containerColor,
        headerIconRes = headerIconRes,
        headerIconContentDescription = headerIconContentDescription,
        headerIconContent = headerIconContent,
        headerTitleColor = headerTitleColor,
        headerPadding = headerPadding,
        contentPadding = contentPadding,
        footerPadding = footerPadding,
        footerButtons = footerButtons,
        footerContent = footerContent,
    ) {
        BodyLargeText(
            text = contentText,
            color = HangmanTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun DialogSectionSeparator(
    seed: Int,
    height: Dp = 4.dp,
    alpha: Float = 0.75f,
    paddingHorizontal: Dp = 24.dp,
) {
    Box(
        modifier = Modifier
            .alpha(alpha)
            .fillMaxWidth()
            .padding(horizontal = paddingHorizontal)
            .height(height)
            .creepyOutline(
                seed = seed,
                threshold = 1.08f,
                fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.5f),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
                strokeWidthFactor = 1f,
            ),
    )
}

data class HangmanDialogFooterButton(
    val text: String,
    val onClick: () -> Unit,
    val color: Color,
)