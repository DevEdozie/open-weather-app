package com.example.openweather.data.local


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.robolectric.annotation.Config

@Config(sdk = [33])
class AppManagerFavoritesTest {

    @Test
    fun `add favorites de-dupes and sorts`() = runTest {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        AppManager.addFavoriteCity(ctx, "Lagos,NG")
        AppManager.addFavoriteCity(ctx, "Abuja,NG")
        AppManager.addFavoriteCity(ctx, "lagos,ng") // duplicate case-insensitive

        val list = AppManager.favoriteCitiesFlow(ctx).first()
        assertThat(list).containsExactly("Abuja,NG", "Lagos,NG").inOrder()
    }

    @Test
    fun `remove favorite updates list`() = runTest {
        val ctx: Context = ApplicationProvider.getApplicationContext()

        AppManager.addFavoriteCity(ctx, "Lagos,NG")
        AppManager.addFavoriteCity(ctx, "Abuja,NG")
        AppManager.removeFavoriteCity(ctx, "Lagos,NG")

        val list = AppManager.favoriteCitiesFlow(ctx).first()
        assertThat(list).containsExactly("Abuja,NG")
    }
}
