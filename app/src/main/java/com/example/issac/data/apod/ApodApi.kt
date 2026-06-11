package com.example.issac.data.apod

import com.example.issac.BuildConfig
import com.example.issac.data.apod.dto.ApodResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit description of NASA's Astronomy Picture of the Day endpoint on
 * api.nasa.gov. Retrofit generates the HTTP implementation from these
 * annotations at runtime.
 */
interface ApodApi {

    /**
     * `GET planetary/apod?count=N&api_key=...`
     *
     * The `count` parameter asks APOD for N *random* pictures, so every call
     * returns a different set — that's what lets us shuffle the background.
     *
     * The key comes from `nasa.apiKey` in local.properties via [BuildConfig]
     * (falling back to NASA's shared, heavily rate-limited DEMO_KEY), so a
     * personal key never appears in version control.
     */
    @GET("planetary/apod")
    suspend fun getRandomPictures(
        @Query("count") count: Int = 5,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
    ): List<ApodResponse>
}
