package com.example.openweather.domain



import com.example.openweather.model.WeatherResponse
import com.example.openweather.data.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository) {
    suspend fun execute(city: String, apiKey: String): WeatherResponse {
        return weatherRepository.getWeatherForCity(city, apiKey)
    }
}
