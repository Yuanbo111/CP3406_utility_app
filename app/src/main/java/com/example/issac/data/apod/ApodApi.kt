package com.example.issac.data.apod

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
     * `GET planetary/apod?count=N&api_key=DEMO_KEY`
     *
     * The `count` parameter asks APOD for N *random* pictures, so every call
     * returns a different set — that's what lets us shuffle the background.
     */
    @GET("planetary/apod")
    suspend fun getRandomPictures(
        @Query("count") count: Int = 5,
        @Query("api_key") apiKey: String = DEMO_KEY,
    ): List<ApodResponse>

    companion object {
        /** NASA's public demo key. Rate-limited but key-less, so safe to commit. */
        const val DEMO_KEY = "DEMO_KEY"
    }
}
