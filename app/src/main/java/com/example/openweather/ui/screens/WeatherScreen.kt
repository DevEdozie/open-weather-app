package com.example.openweather.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.openweather.core.Resource
import com.example.openweather.ui.components.BWCard
import com.example.openweather.ui.components.CloudChip
import com.example.openweather.ui.components.HumidityChip
import com.example.openweather.ui.components.TempChip
import com.example.openweather.ui.components.WeatherHero
import com.example.openweather.ui.components.WindChip
import com.example.openweather.core.NetworkObserver
import com.example.openweather.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    cityName: String,
    networkObserver: NetworkObserver,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val state by weatherViewModel.weather.collectAsState()

    val favorites by weatherViewModel.favoriteCities.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(cityName) { weatherViewModel.getWeather(cityName) }

    when (val s = state) {
        is Resource.Loading -> {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Fetching weather for $cityName…")
            }
        }

        is Resource.Error -> {
            Column(Modifier.padding(16.dp)) {
                Text("Oops: ${s.message}", color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(12.dp))
                Button(onClick = {
                    // Connectivity check
                    if (!networkObserver.isOnline()) {
                        Toast.makeText(
                            context,
                            "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    weatherViewModel.getWeather(cityName)
                }) { Text("Retry") }
            }
        }

        is Resource.Success -> {
            val w = s.data
            val candidate = (w.name.ifBlank { cityName }).trim()
            val alreadyFavorite = favorites.any { it.equals(candidate, ignoreCase = true) }
            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // HERO
                WeatherHero(
                    city = w.name,
                    description = w.weather.firstOrNull()?.description.orEmpty(),
                    tempC = "${w.main.temp.toInt()}°C"
                )

                Spacer(Modifier.height(16.dp))

                // METRICS CARD
                BWCard(Modifier.fillMaxWidth()) {
                    Text("Details", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        maxItemsInEachRow = 2
                    ) {
                        TempChip("${w.main.feels_like.toInt()}°C")
                        HumidityChip("${w.main.humidity}%")
                        WindChip("${w.wind.speed} m/s")
                        CloudChip("${w.clouds.all}%")
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        // Display toast
                        weatherViewModel.saveFavorite(candidate)
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
                    },
                    enabled = !alreadyFavorite,
                    shape = RoundedCornerShape(12.dp)
                ) { Text(if (alreadyFavorite) "Saved ★" else "Save as favorite") }
            }
        }

        is Resource.Idle -> {
            Column(Modifier.padding(16.dp)) { Text("Preparing request…") }
        }
    }
}
