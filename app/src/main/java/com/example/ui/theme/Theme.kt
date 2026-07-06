package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = Asphalt,
    primaryContainer = NeonGreen,
    onPrimaryContainer = Asphalt,
    secondary = SoftGrey,
    onSecondary = Asphalt,
    tertiary = ChalkWhite,
    onTertiary = Asphalt,
    background = Asphalt,
    onBackground = SoftGrey,
    surface = DarkGrey,
    onSurface = SoftGrey,
    surfaceVariant = Bleachers,
    onSurfaceVariant = TextGrey,
    outline = BorderGrey,
    outlineVariant = BorderGrey,
    error = DangerRed,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000a),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
