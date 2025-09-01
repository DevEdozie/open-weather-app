package com.example.openweather.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.openweather.ui.theme.AppBlack
import com.example.openweather.ui.theme.AppDim

@Composable
fun BWCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AppBlack),
        color = MaterialTheme.colorScheme.surface,
        content = { Column(Modifier.padding(16.dp), content = content) }
    )
}

@Composable
fun MetricChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, AppBlack),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("$label: ", style = MaterialTheme.typography.bodyMedium, color = AppDim)
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
        }
    }
}

@Composable
fun WeatherHero(
    city: String,
    description: String,
    tempC: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(city, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(description.replaceFirstChar { it.uppercase() }, color = AppDim)
        Spacer(Modifier.height(12.dp))
        Text(tempC, style = MaterialTheme.typography.headlineLarge)
    }
}


@Composable fun TempChip(v: String, m: Modifier = Modifier) =
    MetricChip(Icons.Outlined.Thermostat, "Feels", v, m)

@Composable fun HumidityChip(v: String, m: Modifier = Modifier) =
    MetricChip(Icons.Outlined.WaterDrop, "Humidity", v, m)

@Composable fun WindChip(v: String, m: Modifier = Modifier) =
    MetricChip(Icons.Outlined.Air, "Wind", v, m)

@Composable fun CloudChip(v: String, m: Modifier = Modifier) =
    MetricChip(Icons.Outlined.Cloud, "Cloud", v, m)
