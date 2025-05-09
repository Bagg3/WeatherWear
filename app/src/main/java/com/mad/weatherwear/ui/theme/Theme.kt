package com.mad.weatherwear.ui.theme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CustomColorScheme = lightColorScheme(
    primary = TextPrimary,
    background = BgPrimary,
    surface = BgSecondary,
    secondary = BgTertiary,
    tertiary = Accent
)

@Composable
fun WeatherWearTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CustomColorScheme,
        typography = Typography,
        content = content
    )
}