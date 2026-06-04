package com.example.issac.data.horoscope.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class HoroscopeResponseTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `decodes the daily-horoscope JSON into the DTO`() {
        // A trimmed copy of a real response from the API.
        val raw = """
            {
              "data": {
                "date": "2026-06-04",
                "period": "daily",
                "sign": "Leo",
                "horoscope": "Leos, today is a good day to tackle practical tasks."
              }
            }
        """.trimIndent()

        val response = json.decodeFromString<HoroscopeResponse>(raw)

        assertEquals("2026-06-04", response.data.date)
        assertEquals("daily", response.data.period)
        assertEquals("Leo", response.data.sign)
        assertEquals(
            "Leos, today is a good day to tackle practical tasks.",
            response.data.horoscope,
        )
    }
}
