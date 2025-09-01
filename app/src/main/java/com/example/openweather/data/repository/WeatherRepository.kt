package com.example.openweather.data.repository



import com.example.openweather.data.remote.WeatherService
import com.example.openweather.model.WeatherResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val weatherService: WeatherService) {
    suspend fun getWeatherForCity(city: String, apiKey: String): WeatherResponse {
        return weatherService.getWeather(city, apiKey)
    }
}
