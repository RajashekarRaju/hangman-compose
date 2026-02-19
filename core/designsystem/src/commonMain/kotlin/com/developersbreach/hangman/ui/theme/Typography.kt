package com.developersbreach.hangman.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res
import com.developersbreach.hangman.core.designsystem.generated.resources.creepster_regular
import org.jetbrains.compose.resources.Font

@Composable
fun getFontFamily(): FontFamily {
    val resource = Res.font.creepster_regular
    return FontFamily(
        Font(
            resource = resource,
            weight = FontWeight.Bold
        ),
        Font(
            resource = resource,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = resource,
            weight = FontWeight.Medium
        ),
        Font(
            resource = resource,
            weight = FontWeight.Normal
        ),
        Font(
            resource = resource,
            weight = FontWeight.Light
        )
    )
}

@Composable
fun getTypography(): Typography {
    val primaryFontFamily = getFontFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 44.sp,
            letterSpacing = 8.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            letterSpacing = 4.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            letterSpacing = 4.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 4.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            letterSpacing = 6.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 28.sp,
            letterSpacing = 8.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 4.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 28.sp,
            letterSpacing = 6.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 28.sp,
            letterSpacing = 4.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 28.sp,
            letterSpacing = 4.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 4.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 4.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 4.sp,
        ),
    )
}
