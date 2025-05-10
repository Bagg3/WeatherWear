package com.mad.weatherwear.screens.weather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ForecastItem(
    val time: String,
    val temperature: Double,
    val description: String,
    val icon: String
)

class WeatherService(private val apiKey: String) {
    suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherData? {
        return withContext(Dispatchers.IO) {

            val urlString =
                "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric"
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 5000
                connection.readTimeout = 5000

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonResponse = JSONObject(response.toString())
                    val weatherArray = jsonResponse.getJSONArray("weather")
                    val weatherObject = weatherArray.getJSONObject(0)
                    val mainObject = jsonResponse.getJSONObject("main")

                    WeatherData(
                        cityName = jsonResponse.getString("name"),
                        temperature = mainObject.getDouble("temp"),
                        feelsLike = mainObject.getDouble("feels_like"),
                        description = weatherObject.getString("description"),
                        icon = weatherObject.getString("icon")

                    )
                } else {
                    // Handle error response
                    println("Error: ${connection.responseCode}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exception, e.g., log or return null
                null
            }
        }
    }

    suspend fun getForecast(latitude: Double, longitude: Double): List<ForecastItem> {
        return withContext(Dispatchers.IO) {
            val urlString =
                "https://api.openweathermap.org/data/2.5/forecast?lat=$latitude&lon=$longitude&appid=$apiKey&units=metric&cnt=4"
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    val jsonResponse = JSONObject(response.toString())
                    val forecastList = jsonResponse.getJSONArray("list")
                    val forecastItems = mutableListOf<ForecastItem>()

                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                    for (i in 0 until forecastList.length()) {
                        val item = forecastList.getJSONObject(i)
                        val dt = item.getLong("dt") * 1000 // Convert to milliseconds
                        val main = item.getJSONObject("main")
                        val weather = item.getJSONArray("weather").getJSONObject(0)

                        forecastItems.add(
                            ForecastItem(
                                time = timeFormat.format(Date(dt)),
                                temperature = main.getDouble("temp"),
                                description = weather.getString("description"),
                                icon = weather.getString("icon")
                            )
                        )
                    }

                    forecastItems
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}