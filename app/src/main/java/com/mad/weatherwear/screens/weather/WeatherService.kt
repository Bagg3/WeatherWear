package com.mad.weatherwear.screens.weather

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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
}