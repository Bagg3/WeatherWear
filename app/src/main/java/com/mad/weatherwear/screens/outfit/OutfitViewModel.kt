package com.mad.weatherwear.screens.outfit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad.weatherwear.screens.weather.WeatherData
import com.mad.weatherwear.shared.preference.ColdSensitivity
import com.mad.weatherwear.shared.preference.Preference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OutfitRecommendation(
    val topRecommendation: String = "",
    val bottomRecommendation: String = "",
    val extrasRecommendation: String = ""
)

class OutfitViewModel : ViewModel() {

    private val _recommendation = MutableStateFlow<OutfitRecommendation?>(null)
    val recommendation: StateFlow<OutfitRecommendation?> = _recommendation.asStateFlow()

    fun generateRecommendation(weather: WeatherData, preference: Preference) {
        viewModelScope.launch {
            val sensitivity = preference.sensitivity
            val temperature = weather.feelsLike
            val isRaining = weather.description.contains("rain", ignoreCase = true)
            val isSnowing = weather.description.contains("snow", ignoreCase = true)

            val adjustedTemp = when (sensitivity) {
                ColdSensitivity.LOW -> temperature + 3
                ColdSensitivity.MEDIUM -> temperature
                ColdSensitivity.HIGH -> temperature - 3
            }

            // Generate top recommendation
            val top = when {
                adjustedTemp > 25 -> "T-shirt"
                adjustedTemp > 20 -> "Short-sleeve shirt"
                adjustedTemp > 15 -> "Long-sleeve shirt"
                adjustedTemp > 10 -> "Light sweater"
                adjustedTemp > 5 -> "Warm sweater"
                else -> "Heavy winter coat"
            }

            // Generate bottom recommendation
            val bottom = when {
                adjustedTemp > 20 -> "Shorts"
                adjustedTemp > 15 -> "Light pants"
                adjustedTemp > 5 -> "Pants"
                else -> "Warm pants"
            }

            // Generate extras
            val extrasList = mutableListOf<String>()
            if (isRaining) extrasList.add("Rain jacket")
            if (isSnowing) extrasList.add("Snow boots")
            if (adjustedTemp < 10) extrasList.add("Hat")
            if (adjustedTemp < 5) extrasList.add("Gloves")
            if (adjustedTemp < 0) extrasList.add("Scarf")

            val extras =
                if (extrasList.isEmpty()) "No extras needed" else extrasList.joinToString(", ")

            _recommendation.value = OutfitRecommendation(top, bottom, extras)
        }
    }
}