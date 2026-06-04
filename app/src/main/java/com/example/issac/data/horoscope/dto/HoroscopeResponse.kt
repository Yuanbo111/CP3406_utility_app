package com.example.issac.data.horoscope.dto

import kotlinx.serialization.Serializable

/**
 * Mirrors the JSON returned by the daily-horoscope endpoint, e.g.
 *
 * ```
 * { "data": { "date": "2026-06-04", "period": "daily",
 *             "sign": "Leo", "horoscope": "..." } }
 * ```
 *
 * These classes map 1:1 to the wire format and stay inside the data layer; the
 * rest of the app works with a cleaner domain model rather than these DTOs.
 * The `@Serializable` annotation is what the kotlinx-serialization compiler
 * plugin reads to generate the JSON parser at build time.
 */
@Serializable
data class HoroscopeResponse(
    val data: HoroscopeData,
)

@Serializable
data class HoroscopeData(
    val date: String,
    val period: String,
    val sign: String,
    val horoscope: String,
)
