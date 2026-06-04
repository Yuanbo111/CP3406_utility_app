package com.example.issac.data.horoscope

import com.example.issac.data.horoscope.dto.HoroscopeResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit description of the daily-horoscope endpoint on freehoroscopeapi.com.
 * Retrofit reads these annotations and generates the real HTTP implementation
 * of this interface at runtime, so we never write the networking code by hand.
 */
interface HoroscopeApi {

    /**
     * `GET api/v1/get-horoscope/daily?sign=Leo&day=TODAY`
     *
     * @param sign capitalised zodiac name, e.g. "Leo" (matches `Zodiac.displayName`).
     * @param day  "TODAY", "TOMORROW", "YESTERDAY", or a `yyyy-MM-dd` date.
     */
    @GET("api/v1/get-horoscope/daily")
    suspend fun getDailyHoroscope(
        @Query("sign") sign: String,
        @Query("day") day: String = "TODAY",
    ): HoroscopeResponse
}
