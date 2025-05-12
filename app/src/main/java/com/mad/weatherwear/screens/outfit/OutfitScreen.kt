package com.mad.weatherwear.screens.outfit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mad.weatherwear.screens.profile.ProfileViewModel
import com.mad.weatherwear.screens.weather.WeatherViewModel
import com.mad.weatherwear.shared.ui.ScreenLayout
import com.mad.weatherwear.ui.theme.TextPrimary

@Composable
fun OutfitScreen(
    weatherViewModel: WeatherViewModel,
    profileViewModel: ProfileViewModel,
    userId: String,
    modifier: Modifier = Modifier
) {
    val outfitViewModel = androidx.lifecycle.viewmodel.compose.viewModel<OutfitViewModel>()

    val weatherData by weatherViewModel.currentWeather.collectAsState()
    val preference by profileViewModel.preference.collectAsState()
    val recommendation by outfitViewModel.recommendation.collectAsState()
    val isWeatherLoading by weatherViewModel.isLoading.collectAsState()
    val isPreferenceLoading by profileViewModel.isLoading.collectAsState()

    LaunchedEffect(key1 = Unit) {
        weatherViewModel.fetchWeather()
        profileViewModel.loadPreference(userId)
    }

    LaunchedEffect(key1 = weatherData, key2 = preference) {
        if (weatherData != null && preference != null) {
            outfitViewModel.generateRecommendation(weatherData!!, preference!!)
        }
    }

    ScreenLayout(
        titleText = "Outfit",
        temperatureText = "Based on the current weather, I suggest:",
        modifier = modifier
    ) {
        if (isWeatherLoading || isPreferenceLoading || recommendation == null) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                RecommendationDisplayItem(
                    recommendation = recommendation!!.topRecommendation
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                RecommendationDisplayItem(
                    recommendation = recommendation!!.bottomRecommendation
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

                RecommendationDisplayItem(
                    recommendation = recommendation!!.extrasRecommendation
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            }
        }
    }
}

@Composable
fun RecommendationDisplayItem(recommendation: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = recommendation,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}