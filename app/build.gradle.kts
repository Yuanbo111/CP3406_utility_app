import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

// The personal NASA API key lives in local.properties, which is gitignored —
// secrets must never be committed. Anyone without a key still builds and runs
// on NASA's shared DEMO_KEY (just with a much lower hourly rate limit).
val nasaApiKey: String = rootProject.file("local.properties")
    .takeIf { it.exists() }
    ?.inputStream()
    ?.use { stream -> Properties().apply { load(stream) } }
    ?.getProperty("nasa.apiKey")
    ?: "DEMO_KEY"

android {
    namespace = "com.example.issac"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.issac"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "NASA_API_KEY", "\"$nasaApiKey\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}