package com.example.openweather.viewmodel


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openweather.data.local.AppManager
import com.example.openweather.core.Resource
import com.example.openweather.model.WeatherResponse
import com.example.openweather.domain.GetWeatherUseCase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val context: Application // To access DataStore
) : ViewModel() {

    private val _weather = MutableStateFlow<Resource<WeatherResponse>>(Resource.Idle())
    val weather = _weather.asStateFlow()

    private val _favoriteCities = MutableStateFlow<List<String>>(emptyList())
    val favoriteCities = _favoriteCities.asStateFlow()

    private var apiKey: String? = null

    init {
        // Retrieve API key from DataStore
        viewModelScope.launch {
            apiKey = AppManager.getApiKey()
            AppManager.favoriteCitiesFlow(context).collect { _favoriteCities.value = it }
        }
    }

    fun getWeather(city: String) {
        _weather.value = Resource.Loading()
        viewModelScope.launch {
            try {
                val key = apiKey ?: AppManager.getApiKey().also { apiKey = it }
                if (key.isNullOrBlank()) {
                    _weather.value = Resource.Error("Missing API key. Add it in settings.")
                    return@launch
                }
                val response = getWeatherUseCase.execute(city, key)
                _weather.value = Resource.Success(response)
            } catch (e: HttpException) {
                val raw = e.response()?.errorBody()?.string() // ⚠️ read ONCE
                val parsed = runCatching {
                    Gson().fromJson(raw, WeatherResponse::class.java)
                }.getOrNull()

                val msg = parsed?.message?.takeIf { it.isNotBlank() } ?: "Something went wrong"

                _weather.value = Resource.Error(
                    message = msg,
                )
            } catch (e: Exception) {
                _weather.value =
                    Resource.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }

    fun saveFavorite(city: String) {
        viewModelScope.launch { AppManager.addFavoriteCity(context, city) }
    }

    fun removeFavorite(city: String) {
        viewModelScope.launch { AppManager.removeFavoriteCity(context, city) }
    }

}
