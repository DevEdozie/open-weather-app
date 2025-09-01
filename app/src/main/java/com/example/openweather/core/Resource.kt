package com.example.openweather.core

sealed class Resource<T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val message: String, val errors: List<String>? = null) : Resource<T>()
    class Loading<T> : Resource<T>()
    class Idle<T> : Resource<T>()
}