package com.example.openweather.navigation

import android.net.Uri


sealed class Screen(val base: String) {

    object Splash : Screen("splash")
    object Home : Screen("home")

    object WeatherDetail : Screen("weather") {
        const val ARG_CITY_NAME = "cityName"
        val route = "$base/{$ARG_CITY_NAME}"
        fun createRoute(cityName: String) = "$base/${Uri.encode(cityName)}"
    }


}