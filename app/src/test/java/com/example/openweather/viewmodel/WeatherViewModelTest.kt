package com.example.openweather.viewmodel


import android.app.Application
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.openweather.core.Resource
import com.example.openweather.domain.GetWeatherUseCase
import com.example.openweather.model.*
import com.example.openweather.testutil.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response

class WeatherViewModelTest {

    @get:Rule val mainRule = MainDispatcherRule()

    private fun buildVm(useCase: GetWeatherUseCase): WeatherViewModel {
        // Use a real Robolectric Application so AppManager.favoriteCitiesFlow(context) doesn't crash
        val app: Application = ApplicationProvider.getApplicationContext()
        return WeatherViewModel(getWeatherUseCase = useCase, context = app)
    }

    @Test
    fun `getWeather emits Idle - Loading - Success`() = runTest {
        val useCase: GetWeatherUseCase = mock()
        val fake = WeatherResponse(
            name = "Lagos",
            weather = listOf(Weather("overcast clouds")),
            main = Main(22.85, 23.65, 94),
            wind = Wind(1.87, 207),
            clouds = Clouds(92)
        )
        whenever(useCase.execute(any(), any())).doSuspendableAnswer { fake }

        val vm = buildVm(useCase)

        vm.weather.test {
            // initial Idle
            assertThat(awaitItem()).isInstanceOf(Resource.Idle::class.java)

            vm.getWeather("Lagos,NG")

            // Loading
            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            // Success
            val success = awaitItem() as Resource.Success<WeatherResponse>
            assertThat(success.data.name).isEqualTo("Lagos")

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `getWeather emits Idle - Loading - Error on HttpException`() = runTest {
        val useCase: GetWeatherUseCase = mock()
        val errorJson = """{"cod":"404","message":"city not found"}"""
        val errorBody = errorJson.toResponseBody("application/json".toMediaType())
        whenever(useCase.execute(any(), any()))
            .thenThrow(HttpException(Response.error<Any>(404, errorBody)))

        val vm = buildVm(useCase)

        vm.weather.test {
            assertThat(awaitItem()).isInstanceOf(Resource.Idle::class.java)

            vm.getWeather("NoCity")

            assertThat(awaitItem()).isInstanceOf(Resource.Loading::class.java)

            val err = awaitItem() as Resource.Error<*>
            assertThat(err.message.lowercase()).contains("city not found")

            cancelAndConsumeRemainingEvents()
        }
    }
}
