package com.mad.weatherwear.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.mad.weatherwear.ui.theme.TextPrimary
import com.mad.weatherwear.ui.theme.Typography

import com.mad.weatherwear.shared.ui.ScreenLayout

@Composable
fun HomeScreen() {
    ScreenLayout(
        titleText = "Weather Wear",
        temperatureText = "15°C",
        weatherConditionText = "Sunny"
    ) {
        Text(
            text = "Welcome back!",
            style = Typography.bodyLarge,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}