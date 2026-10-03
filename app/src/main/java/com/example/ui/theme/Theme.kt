package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val JioHotstarColorScheme = darkColorScheme(
    primary = HotstarBlue,
    onPrimary = Color.White,
    primaryContainer = HotstarSurfaceElevated,
    onPrimaryContainer = HotstarCyan,
    secondary = HotstarGold,
    onSecondary = Color.Black,
    secondaryContainer = HotstarSurfaceElevated,
    onSecondaryContainer = HotstarGold,
    tertiary = HotstarAccentRed,
    onTertiary = Color.White,
    background = HotstarNavyDark,
    onBackground = TextWhite,
    surface = HotstarSurface,
    onSurface = TextWhite,
    surfaceVariant = HotstarSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = HotstarSurfaceBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force cinematic dark theme for JioHotstar
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = JioHotstarColorScheme,
        typography = Typography,
        content = content
    )
}
