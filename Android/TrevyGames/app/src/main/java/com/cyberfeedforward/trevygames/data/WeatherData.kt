package com.cyberfeedforward.trevygames.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("city") val city: City,
    @SerializedName("list") val forecast: List<ForecastItem>
)

data class City(
    @SerializedName("name") val name: String,
    @SerializedName("country") val country: String
)

data class ForecastItem(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("main") val main: MainWeather,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("dt_txt") val dateText: String
)

data class MainWeather(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int
)

data class Weather(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class Wind(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val direction: Int
)

// UI Model for displaying weather data
data class WeatherDay(
    val date: String,
    val dayOfWeek: String,
    val minTemp: Double,
    val maxTemp: Double,
    val description: String,
    val icon: String,
    val humidity: Int,
    val windSpeed: Double
)

