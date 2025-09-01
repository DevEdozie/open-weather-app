import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.openweather"
    compileSdk = 36

    defaultConfig {

        // Read from local.properties first, then fall back to env var
        val localProps = Properties().apply {
            val f = rootProject.file("local.properties")
            if (f.exists()) f.inputStream().use { load(it) }
        }
        val owKeyFromLocal = localProps.getProperty("OPENWEATHER_API_KEY") ?: ""
        val owKeyFromEnv = providers.environmentVariable("OPENWEATHER_API_KEY").orNull ?: ""
        val owKey = if (owKeyFromLocal.isNotBlank()) owKeyFromLocal else owKeyFromEnv

        // Expose to BuildConfig
        buildConfigField("String", "OPENWEATHER_API_KEY", "\"$owKey\"")



        applicationId = "com.example.openweather"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true // for Robolectric/DataStore tests
    }
}

dependencies {

    implementation("androidx.compose.material:material-icons-extended")

    // Datastore
//    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Hilt for dependency injection
    implementation("com.google.dagger:hilt-android:2.57.1")
    kapt("com.google.dagger:hilt-compiler:2.57.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // --- Unit testing core ---
//    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.4.4")

    // AndroidX Test Core (lets us use ApplicationProvider in JVM tests)
    testImplementation("androidx.test:core:1.7.0")
    testImplementation("androidx.test:core-ktx:1.7.0")

// --- Coroutines & Flow testing ---
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("app.cash.turbine:turbine:1.2.1")

// --- Mockito (Kotlin) ---
    testImplementation("org.mockito:mockito-core:5.19.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.0.0")

// --- HTTP layer ---
    testImplementation("com.squareup.okhttp3:mockwebserver:5.1.0")

// --- Robolectric (for DataStore tests on JVM) ---
    testImplementation("org.robolectric:robolectric:4.16")

}