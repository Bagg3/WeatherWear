package com.mad.weatherwear.screens.weather

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.mad.weatherwear.BuildConfig
import com.mad.weatherwear.shared.location.LocationService
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.roundToInt

@SuppressLint("ContextCastToActivity")
@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    // Get the local context and create location service
    val context = LocalContext.current
    val activity = context as androidx.activity.ComponentActivity
    val mapsClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationService = remember { LocationService(activity, mapsClient) }

    // Access the API key from BuildConfig
    val weatherService = remember {
        WeatherService(apiKey = BuildConfig.OPENWEATHER_API_KEY)
    }

    var locationError by remember { mutableStateOf<String?>(null) }
    var weatherInfo by remember { mutableStateOf<WeatherData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var hasPermission by remember { mutableStateOf(locationService.checkPermission()) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
        if (isGranted) {
            locationError = null
            coroutineScope.launch {
                isLoading = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    try {
                        val location = locationService.getCurrentLocation()
                        try {
                            weatherInfo =
                                weatherService.getWeatherData(location.latitude, location.longitude)
                        } catch (e: Exception) {
                            locationError = "Error fetching weather: ${e.message}"
                        }
                    } catch (e: Exception) {
                        locationError = "Error fetching location: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                } else {
                    locationError = "Location API requires Android S or higher"
                    isLoading = false
                }
            }
        } else {
            locationError = "Location permission denied"
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            locationError = null
            isLoading = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                try {
                    val location = locationService.getCurrentLocation()
                    try {
                        weatherInfo =
                            weatherService.getWeatherData(location.latitude, location.longitude)
                    } catch (e: Exception) {
                        locationError = "Error fetching weather: ${e.message}"
                    }
                } catch (e: Exception) {
                    locationError = "Error fetching location: ${e.message}"
                } finally {
                    isLoading = false
                }
            } else {
                locationError = "Location API requires Android S or higher for this method."
                isLoading = false
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Only render error text when there is an error
            locationError?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (!hasPermission) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Location Permission Required",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We need your location to show you the current weather",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                        modifier = Modifier.fillMaxWidth(0.7f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Grant Permission")
                    }
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Fetching weather data...",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                weatherInfo?.let { weather ->
                    WeatherDisplay(weather = weather)
                } ?: run {
                    if (hasPermission && locationError == null) {
                        Text(
                            text = "Could not fetch weather data.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDisplay(weather: WeatherData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.cityName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            
            Text(
                text = weather.description.capitalize(Locale.ROOT),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${weather.temperature.roundToInt()}°C",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Feels like ${weather.feelsLike.roundToInt()}°C",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Updated just now",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}