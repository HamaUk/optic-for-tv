package com.optic.iptv.ui.theme

import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.compose.runtime.Composable

@OptIn(ExperimentalTvMaterial3Api::class)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGold,
    secondary = FocusedGlow,
    background = DeepCharcoal,
    surface = DarkSlateGrey,
    onPrimary = PureBlack,
    onSecondary = White,
    onBackground = White,
    onSurface = White,
)

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun OpticTVProTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
