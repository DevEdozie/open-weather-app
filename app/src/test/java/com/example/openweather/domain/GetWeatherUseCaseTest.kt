package com.example.openweather.domain


import com.example.openweather.data.repository.WeatherRepository
import com.example.openweather.model.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

class GetWeatherUseCaseTest {

    @Test
    fun `execute returns repo data`() = runTest {
        val repo: WeatherRepository = mock()
        val expected = WeatherResponse(
            name = "Lagos",
            weather = listOf(Weather("cloudy")),
            main = Main(22.0, 23.0, 90),
            wind = Wind(2.0, 200),
            clouds = Clouds(90)
        )
        whenever(repo.getWeatherForCity(any(), any())).thenReturn(expected)

        val useCase = GetWeatherUseCase(repo)
        val actual = useCase.execute("Lagos", "KEY")
        assertThat(actual).isEqualTo(expected)
    }

    @Test(expected = HttpException::class)
    fun `execute propagates repo HttpException`() = runTest {
        val repo: WeatherRepository = mock()

        val errorBody = """{"cod":"404","message":"city not found"}"""
            .toResponseBody("application/json".toMediaType())
        whenever(repo.getWeatherForCity(any(), any()))
            .thenThrow(HttpException(Response.error<Any>(404, errorBody)))

        val useCase = GetWeatherUseCase(repo)
        useCase.execute("Nowhere", "KEY")
    }
}
