package com.developersbreach.hangman.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res
import com.developersbreach.hangman.core.designsystem.generated.resources.bubblegum_sans_regular
import com.developersbreach.hangman.core.designsystem.generated.resources.creepster_regular
import org.jetbrains.compose.resources.Font

@Composable
private fun getCreepsterFontFamily(): FontFamily {
    val resource = Res.font.creepster_regular
    return FontFamily(
        Font(
            resource = resource,
            weight = FontWeight.Bold,
        ),
        Font(
            resource = resource,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Medium,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Normal,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Light,
        ),
    )
}

@Composable
private fun getBubblegumFontFamily(): FontFamily {
    val resource = Res.font.bubblegum_sans_regular
    return FontFamily(
        Font(
            resource = resource,
            weight = FontWeight.Bold,
        ),
        Font(
            resource = resource,
            weight = FontWeight.SemiBold,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Medium,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Normal,
        ),
        Font(
            resource = resource,
            weight = FontWeight.Light,
        ),
    )
}

@Composable
fun getTypography(): Typography {
    val titleFontFamily = getCreepsterFontFamily()
    val bodyFontFamily = getBubblegumFontFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 44.sp,
            letterSpacing = 8.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            letterSpacing = 4.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            letterSpacing = 4.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            letterSpacing = 4.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 4.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            letterSpacing = 6.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 28.sp,
            letterSpacing = 8.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 4.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSynthesis = FontSynthesis.Weight,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.3.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSynthesis = FontSynthesis.Weight,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.2.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSynthesis = FontSynthesis.Weight,
            fontSize = 16.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.1.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = titleFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 4.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Medium,
            fontSynthesis = FontSynthesis.Weight,
            fontSize = 18.sp,
            letterSpacing = 0.2.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = bodyFontFamily,
            fontWeight = FontWeight.Normal,
            fontSynthesis = FontSynthesis.Weight,
            fontSize = 16.sp,
            letterSpacing = 0.1.sp,
        ),
    )
}
