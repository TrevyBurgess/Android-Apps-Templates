package com.cyberfeedforward.trevygames.repository

import com.cyberfeedforward.trevygames.api.WeatherApiClient
import com.cyberfeedforward.trevygames.data.WeatherDay
import com.cyberfeedforward.trevygames.data.WeatherResponse
import java.text.SimpleDateFormat
import java.util.*

class WeatherRepository {
    private val weatherApiService = WeatherApiClient.weatherApiService
    
    // Replace with your actual OpenWeatherMap API key
    private val apiKey = "YOUR_API_KEY_HERE"
    
    suspend fun getWeatherForecast(latitude: Double, longitude: Double): Result<List<WeatherDay>> {
        return try {
            val response = weatherApiService.getWeatherForecast(
                latitude = latitude,
                longitude = longitude,
                apiKey = apiKey
            )
            
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                if (weatherResponse != null) {
                    val weatherDays = processWeatherData(weatherResponse)
                    Result.success(weatherDays)
                } else {
                    Result.failure(Exception("Empty response body"))
                }
            } else {
                Result.failure(Exception("API call failed: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun processWeatherData(weatherResponse: WeatherResponse): List<WeatherDay> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        
        // Group forecast items by date
        val dailyForecasts = weatherResponse.forecast.groupBy { forecast ->
            dateFormat.format(Date(forecast.timestamp * 1000))
        }
        
        return dailyForecasts.map { (date, forecasts) ->
            val firstForecast = forecasts.first()
            val maxTemp = forecasts.maxOf { it.main.maxTemperature }
            val minTemp = forecasts.minOf { it.main.minTemperature }
            val avgHumidity = forecasts.map { it.main.humidity }.average().toInt()
            val avgWindSpeed = forecasts.map { it.wind.speed }.average()
            
            WeatherDay(
                date = date,
                dayOfWeek = dayFormat.format(Date(firstForecast.timestamp * 1000)),
                minTemp = minTemp,
                maxTemp = maxTemp,
                description = firstForecast.weather.first().description,
                icon = firstForecast.weather.first().icon,
                humidity = avgHumidity,
                windSpeed = avgWindSpeed
            )
        }.take(7) // Take only 7 days
    }
}

