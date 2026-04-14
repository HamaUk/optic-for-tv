package com.optic.iptv.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Typography
import com.optic.iptv.R

private val Rabar21 = FontFamily(
    Font(R.font.rabar_21, FontWeight.Normal),
    Font(R.font.rabar_21, FontWeight.Medium),
    Font(R.font.rabar_21, FontWeight.Bold),
    Font(R.font.rabar_21, FontWeight.Black)
)

@OptIn(ExperimentalTvMaterial3Api::class)
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Rabar21,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Rabar21,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Rabar21,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Rabar21,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
)
