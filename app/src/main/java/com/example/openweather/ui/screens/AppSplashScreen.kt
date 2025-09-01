package com.example.openweather.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.openweather.R
import com.example.openweather.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun AppSplashScreen(navController: NavController) {
    LaunchedEffect(true) {
        delay(3000) // Show splash screen for 3 seconds
        navController.navigate(Screen.Home.base) {
            popUpTo(Screen.Splash.base) { inclusive = true }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.weather_map_app_logo_ic),
                contentDescription = "Splash Screen Logo",
                modifier = Modifier
                    .size(64.dp)
            )
        }
    }
}
