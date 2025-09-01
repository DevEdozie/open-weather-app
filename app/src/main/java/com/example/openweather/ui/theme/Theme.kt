package com.example.openweather.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val BWColorScheme: ColorScheme = lightColorScheme(
    primary = AppBlack,
    onPrimary = AppWhite,
    secondary = AppBlack,
    onSecondary = AppWhite,
    background = AppWhite,
    onBackground = AppBlack,
    surface = AppWhite,
    onSurface = AppBlack,
    outline = AppBlack
)

private val AppTypography = Typography(
    headlineLarge = TextStyle(fontSize = 40.sp, fontWeight = FontWeight.Bold),
    headlineMedium = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontSize = 16.sp),
    bodyMedium = TextStyle(fontSize = 14.sp)
)

@Composable
fun OpenWeatherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BWColorScheme,
        typography = AppTypography,
        content = content
    )
}
