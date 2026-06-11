package com.example.issac.data.apod.dto

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

class ApodResponseTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `parses a list of APOD entries, ignoring unknown fields`() {
        val raw = """
            [
              {
                "date": "2024-01-01",
                "explanation": "A long description we don't use.",
                "media_type": "image",
                "title": "The Milky Way",
                "url": "https://apod.nasa.gov/apod/image/milky.jpg",
                "copyright": "Jane Doe"
              },
              {
                "media_type": "video",
                "title": "A space video",
                "url": "https://youtube.com/x"
              }
            ]
        """.trimIndent()

        val result = json.decodeFromString<List<ApodResponse>>(raw)

        assertEquals(2, result.size)
        assertEquals("The Milky Way", result[0].title)
        assertEquals("https://apod.nasa.gov/apod/image/milky.jpg", result[0].url)
        assertEquals("image", result[0].mediaType)
        assertEquals("Jane Doe", result[0].copyright)
        assertEquals("video", result[1].mediaType)
    }
}
