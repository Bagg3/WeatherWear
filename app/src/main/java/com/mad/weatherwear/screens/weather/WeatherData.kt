package com.mad.weatherwear.screens.weather


data class WeatherData(
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val description: String,
    val icon: String
)

data class ForecastItem(
    val time: String,
    val temperature: Double,
    val description: String,
    val icon: String
)