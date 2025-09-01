package com.example.openweather.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.openweather.ui.screens.AppSplashScreen
import com.example.openweather.ui.screens.HomeScreen
import com.example.openweather.ui.screens.WeatherScreen
import com.example.openweather.core.NetworkObserver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    networkObserver: NetworkObserver
) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val userCurrentRoute = currentBackStackEntry?.destination?.route

    val isDetail = userCurrentRoute?.startsWith(Screen.WeatherDetail.base) == true

    val screenTitle = when {
        userCurrentRoute == Screen.Home.base -> "Home"
        isDetail -> "Weather Detail"
        else -> ""
    }

    val shouldShowBackArrow = isDetail
    val hideTopBar = (userCurrentRoute == Screen.Splash.base)

    Scaffold(
        topBar = {
            if (!hideTopBar) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White, // Background color
                    ),
                    navigationIcon = {
                        if (shouldShowBackArrow) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.Black
                                )
                            }
                        }
                    },
                    title = {
                        // --- Header ---
                        Text(
                            text = screenTitle,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        },
    ) { paddingValues ->
        NavHost(
            navController,
            startDestination = Screen.Splash.base,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.base) { AppSplashScreen(navController) }
            composable(Screen.Home.base) {
                HomeScreen(
                    navController = navController,
                    networkObserver = networkObserver
                )
            }
            composable(
                route = Screen.WeatherDetail.route,
                arguments = listOf(navArgument(Screen.WeatherDetail.ARG_CITY_NAME) {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                val cityName =
                    backStackEntry.arguments?.getString(Screen.WeatherDetail.ARG_CITY_NAME)!!
                WeatherScreen(
                    cityName = cityName,
                    networkObserver = networkObserver
                )
            }
        }
    }


}
