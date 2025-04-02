package com.fillthegapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF00A6FB), // Electric Blue
    secondary = Color(0xFF7209B7), // Neon Purple
    tertiary = Color(0xFFF72585), // Magenta Pink
    background = Color(0xFF121212), // Dark Navy
    surface = Color(0xFF1E1E1E), // Deep Gray
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFFE0E0E0), // Light Gray
    onSurface = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3A86FF), // Deep Blue
    secondary = Color(0xFF9D4EDD), // Soft Purple
    tertiary = Color(0xFFFF006E), // Vivid Pink
    background = Color(0xFFF5F5F5), // Off White
    surface = Color.White, // Pure White
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF333333), // Dark Gray
    onSurface = Color(0xFF333333),
)

@Composable
fun RickAndMortyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}