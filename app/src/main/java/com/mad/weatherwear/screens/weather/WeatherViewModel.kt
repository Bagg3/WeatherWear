package com.mad.weatherwear.screens.weather

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mad.weatherwear.BuildConfig
import com.mad.weatherwear.shared.location.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    private val locationService = LocationService(application)
    private val weatherService = WeatherService(apiKey = BuildConfig.OPENWEATHER_API_KEY)

    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather.asStateFlow()

    private val _forecast = MutableStateFlow<List<ForecastItem>>(emptyList())
    val forecast: StateFlow<List<ForecastItem>> = _forecast.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _hasPermission = MutableStateFlow(locationService.checkPermission())
    val hasPermission: StateFlow<Boolean> = _hasPermission.asStateFlow()

    fun fetchWeather() {
        if (!hasPermission.value) {
            if (!locationService.checkPermission()) {
                _error.value = "Location permission not granted. Cannot fetch weather."
                _isLoading.value = false
            }
            return

        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val location = locationService.getCurrentLocation()
                updateWeatherData(location)
            } catch (e: Exception) {
                _error.value = "Error fetching location: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun updateWeatherData(location: Location) {
        try {
            _currentWeather.value =
                weatherService.getWeatherData(location.latitude, location.longitude)
            _forecast.value = weatherService.getForecast(location.latitude, location.longitude)
        } catch (e: Exception) {
            _error.value = "Error fetching weather: ${e.message}"
        }
    }

    fun updatePermissionStatus(granted: Boolean) {
        _hasPermission.value = granted
        if (granted) {
            fetchWeather()
        } else {
            _error.value = "Location permission is required to fetch weather."
            _currentWeather.value = null
            _forecast.value = emptyList()
        }
    }


    fun clearError() {
        _error.value = null
    }

}