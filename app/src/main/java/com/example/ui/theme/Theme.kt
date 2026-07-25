package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FrostedGlassColorScheme = darkColorScheme(
    primary = IndigoPrimary,
    secondary = SkyAccent,
    tertiary = EmeraldSuccess,
    background = SlateBackground,
    surface = SlateCard,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = RoseError,
    outline = GlassBorder
)

@Composable
fun WebStudioTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FrostedGlassColorScheme,
        typography = Typography,
        content = content
    )
}
