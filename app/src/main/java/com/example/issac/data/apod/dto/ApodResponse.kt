package com.example.issac.data.apod.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * One NASA "Astronomy Picture of the Day" entry, e.g.
 *
 * ```
 * { "title": "The Milky Way", "url": "https://apod.nasa.gov/...jpg",
 *   "media_type": "image", "copyright": "Jane Doe" }
 * ```
 *
 * APOD returns more fields than we need (date, explanation, hdurl, ...); the
 * Json parser is configured to ignore unknown keys, so we declare only these.
 * `media_type` is "image" or "video" — we keep only images for the background.
 */
@Serializable
data class ApodResponse(
    val title: String = "",
    val url: String = "",
    @SerialName("media_type") val mediaType: String = "",
    val copyright: String? = null,
)
