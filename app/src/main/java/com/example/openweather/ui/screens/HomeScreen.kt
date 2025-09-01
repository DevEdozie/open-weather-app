package com.example.openweather.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.openweather.navigation.Screen
import com.example.openweather.ui.theme.AppBlack
import com.example.openweather.core.NetworkObserver
import com.example.openweather.viewmodel.WeatherViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    networkObserver: NetworkObserver,
    weatherViewModel: WeatherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var city by remember { mutableStateOf("") }
    val favorites by weatherViewModel.favoriteCities.collectAsState()

    Column(Modifier.padding(16.dp)) {

        if (favorites.isNotEmpty()) {
            Text("Your favorites", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(favorites.size) { idx ->
                    val fav = favorites[idx]
                    OutlinedButton(
                        onClick = { city = fav },
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, AppBlack)
                    ) { Text(fav) }
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City (e.g., Lagos)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                val q = city.trim()
                if (q.isEmpty()) {
                    Toast.makeText(context, "Please enter a city", Toast.LENGTH_SHORT).show()
                } else {
                    // Connectivity check
                    if (!networkObserver.isOnline()) {
                        Toast.makeText(
                            context,
                            "No internet connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }
                    navController.navigate(Screen.WeatherDetail.createRoute(q))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !city.isEmpty(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Get Weather")
        }
    }
}
