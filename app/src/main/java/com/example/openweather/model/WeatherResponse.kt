package com.example.openweather.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val clouds: Clouds,
    val name: String,
    @SerializedName("message") val message: String? = null, // <-- add
    @SerializedName("cod") val cod: String? = null
)

data class Weather(val description: String)

data class Main(val temp: Double, val feels_like: Double, val humidity: Int)

data class Wind(val speed: Double, val deg: Int)

data class Clouds(val all: Int)
