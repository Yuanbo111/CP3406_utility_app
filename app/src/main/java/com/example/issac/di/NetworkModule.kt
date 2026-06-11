package com.example.issac.di

import com.example.issac.data.apod.ApodApi
import com.example.issac.data.horoscope.HoroscopeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Tells Hilt how to build the networking stack. Everything here lives in the
 * application-wide [SingletonComponent], so one shared OkHttp / Json instance is
 * reused across both APIs.
 *
 * The app talks to two hosts — freehoroscopeapi.com and api.nasa.gov — so each
 * API gets its own Retrofit (built from the shared OkHttp + Json via
 * [buildRetrofit]). We use @Provides rather than constructor injection because
 * these are third-party types we don't own.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val HOROSCOPE_BASE_URL = "https://freehoroscopeapi.com/"
    private const val NASA_BASE_URL = "https://api.nasa.gov/"

    @Provides
    @Singleton
    fun provideJson(): Json = Json { ignoreUnknownKeys = true }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideHoroscopeApi(json: Json, client: OkHttpClient): HoroscopeApi =
        buildRetrofit(HOROSCOPE_BASE_URL, json, client).create(HoroscopeApi::class.java)

    @Provides
    @Singleton
    fun provideApodApi(json: Json, client: OkHttpClient): ApodApi =
        buildRetrofit(NASA_BASE_URL, json, client).create(ApodApi::class.java)

    private fun buildRetrofit(baseUrl: String, json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
