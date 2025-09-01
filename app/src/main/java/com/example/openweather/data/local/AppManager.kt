package com.example.openweather.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.openweather.BuildConfig
import kotlinx.coroutines.flow.map

object AppManager {

    private const val DATA_STORE_NAME = "settings_data_store"
    private val FAVORITE_CITIES_PREF = stringSetPreferencesKey("favorite_cities")

    private val API_KEY_PREF = stringPreferencesKey("api_key")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)


    fun getApiKey(): String? {
        val stored = BuildConfig.OPENWEATHER_API_KEY.takeIf { it.isNotBlank() }
        return stored
    }

    // Observe all favorites as a *list* (sorted for stable UI)
    fun favoriteCitiesFlow(context: Context) =
        context.dataStore.data.map { prefs ->
            (prefs[FAVORITE_CITIES_PREF] ?: emptySet())
                .sortedBy { it.lowercase() }
        }

    // Add a favorite (case-insensitive de-dupe, simple cap optional)
    suspend fun addFavoriteCity(context: Context, city: String) {
        val normalized = city.trim()
        if (normalized.isEmpty()) return
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITE_CITIES_PREF] ?: emptySet()
            if (current.any { it.equals(normalized, ignoreCase = true) }) return@edit
            prefs[FAVORITE_CITIES_PREF] = current + normalized
        }
    }

    // Remove one favorite
    suspend fun removeFavoriteCity(context: Context, city: String) {
        val normalized = city.trim()
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITE_CITIES_PREF] ?: emptySet()
            prefs[FAVORITE_CITIES_PREF] =
                current.filterNot { it.equals(normalized, ignoreCase = true) }.toSet()
        }
    }
}