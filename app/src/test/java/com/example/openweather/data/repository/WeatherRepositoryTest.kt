package com.example.openweather.data.repository


import com.example.openweather.data.remote.WeatherService
import com.example.openweather.model.WeatherResponse
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepositoryTest {

    private lateinit var server: MockWebServer
    private lateinit var service: WeatherService
    private lateinit var repo: WeatherRepository

    @Before
    fun setUp() {
        server = MockWebServer()
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/data/2.5/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(WeatherService::class.java)
        repo = WeatherRepository(service)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getWeatherForCity returns parsed WeatherResponse`() = runTest {
        val body = """
        {
          "name": "Lagos",
          "weather": [{"description":"overcast clouds"}],
          "main": {"temp": 22.85, "feels_like": 23.65, "humidity": 94},
          "wind": {"speed": 1.87, "deg": 207},
          "clouds": {"all": 92}
        }
    """.trimIndent()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val result: WeatherResponse = repo.getWeatherForCity("Lagos,NG", "TEST_KEY")

        val recorded = server.takeRequest()

        // endpoint path
        assertThat(recorded.requestUrl!!.encodedPath).isEqualTo("/data/2.5/weather")

        // decoded query params
        val url = recorded.requestUrl!!
        assertThat(url.queryParameter("q")).isEqualTo("Lagos,NG")
        assertThat(url.queryParameter("appid")).isEqualTo("TEST_KEY")
        assertThat(url.queryParameter("units")).isEqualTo("metric")

        // response parsing
        assertThat(result.name).isEqualTo("Lagos")
        assertThat(result.weather.first().description).isEqualTo("overcast clouds")
        assertThat(result.main.humidity).isEqualTo(94)
        assertThat(result.wind.deg).isEqualTo(207)
        assertThat(result.clouds.all).isEqualTo(92)
    }


    @Test(expected = HttpException::class)
    fun `getWeatherForCity throws on non-2xx`() = runTest {
        val err = """{"cod":"404","message":"city not found"}"""
        server.enqueue(MockResponse().setResponseCode(404).setBody(err))
        repo.getWeatherForCity("Nowhere", "TEST_KEY")
    }
}
