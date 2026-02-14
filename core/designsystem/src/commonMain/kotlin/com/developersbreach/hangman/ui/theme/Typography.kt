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
    return FontFamily(
        Font(
            resource = Res.font.creepster_regular,
            weight = FontWeight.Bold
        ),
        Font(
            resource = Res.font.creepster_regular,
            weight = FontWeight.SemiBold
        ),
        Font(
            resource = Res.font.creepster_regular,
            weight = FontWeight.Medium
        ),
        Font(
            resource = Res.font.creepster_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resource = Res.font.creepster_regular,
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
            fontWeight = FontWeight.Bold,
            fontSize = 57.sp,
            lineHeight = 64.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 45.sp,
            lineHeight = 52.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 36.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            lineHeight = 32.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = primaryFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    )
}
