package com.example.issac.domain.model

/**
 * The twelve western zodiac signs, each paired with its display name and
 * astrological glyph so the UI can render either without a lookup table.
 */
enum class Zodiac(
    val displayName: String,
    val symbol: String,
) {
    ARIES("Aries", "♈"),
    TAURUS("Taurus", "♉"),
    GEMINI("Gemini", "♊"),
    CANCER("Cancer", "♋"),
    LEO("Leo", "♌"),
    VIRGO("Virgo", "♍"),
    LIBRA("Libra", "♎"),
    SCORPIO("Scorpio", "♏"),
    SAGITTARIUS("Sagittarius", "♐"),
    CAPRICORN("Capricorn", "♑"),
    AQUARIUS("Aquarius", "♒"),
    PISCES("Pisces", "♓"),
}
