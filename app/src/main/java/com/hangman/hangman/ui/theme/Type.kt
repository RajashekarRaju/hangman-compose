package com.hangman.hangman.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hangman.hangman.R

private val Creepster = FontFamily(
    Font(
        resId = R.font.creepster_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal
    )
)

val Typography = Typography(

    h3 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 44.sp,
        letterSpacing = 8.sp
    ),

    h4 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 4.sp
    ),

    h5 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 4.sp
    ),

    subtitle1 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        letterSpacing = 6.sp
    ),

    subtitle2 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 28.sp,
        letterSpacing = 8.sp
    ),

    body1 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 28.sp,
        letterSpacing = 6.sp
    ),

    body2 = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 28.sp,
        letterSpacing = 4.sp
    ),

    button = TextStyle(
        fontFamily = Creepster,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 4.sp
    )
)