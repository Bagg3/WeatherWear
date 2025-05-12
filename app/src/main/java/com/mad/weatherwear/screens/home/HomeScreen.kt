package com.mad.weatherwear.screens.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mad.weatherwear.screens.weather.WeatherViewModel
import com.mad.weatherwear.shared.ui.ScreenLayout
import com.mad.weatherwear.ui.theme.TextPrimary
import com.mad.weatherwear.ui.theme.Typography
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun HomeScreen(weatherViewModel: WeatherViewModel) {
    val weatherInfo by weatherViewModel.currentWeather.collectAsState()
    val isLoading by weatherViewModel.isLoading.collectAsState()
    val hasPermission by weatherViewModel.hasPermission.collectAsState()
    val error by weatherViewModel.error.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        weatherViewModel.updatePermissionStatus(isGranted)
        if (isGranted) {
            weatherViewModel.fetchWeather()
        }
    }

    LaunchedEffect(key1 = Unit) {
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    ScreenLayout(
        titleText = "Weather Wear",
        temperatureText = weatherInfo?.temperature?.let { "${it.roundToInt()} °C" }
            ?: "Loading weather",
        weatherConditionText = weatherInfo?.description?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
    ) {
        if (!hasPermission) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Location permission is required to show weather data.",
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (isLoading && weatherInfo == null) {
            // Only show loading indicator when there's no cached data
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            }
        } else if (error != null) {
            Text(
                text = error ?: "",
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = { weatherViewModel.clearError(); weatherViewModel.fetchWeather() }) {
                Text("Retry")
            }
        } else {
            Text(
                text = "Welcome back!",
                style = Typography.bodyLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}