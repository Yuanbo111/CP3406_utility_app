package com.example.issac.di

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
 * application-wide [SingletonComponent], so one shared Retrofit / OkHttp / Json
 * instance is reused across the app.
 *
 * We use @Provides (rather than constructor injection) because these are
 * third-party types we don't own — Hilt can't call their constructors itself.
 * [com.example.issac.data.horoscope.HoroscopeRepository], which we *do* own, is
 * wired through its own @Inject constructor instead.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://freehoroscopeapi.com/"

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
    fun provideRetrofit(json: Json, client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideHoroscopeApi(retrofit: Retrofit): HoroscopeApi =
        retrofit.create(HoroscopeApi::class.java)
}
