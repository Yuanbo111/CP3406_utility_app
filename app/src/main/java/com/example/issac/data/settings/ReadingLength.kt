package com.example.issac.data.settings

/** How much of the daily horoscope to show on the Today screen. */
enum class ReadingLength(val label: String) {
    SHORT("Short"),
    FULL("Full");

    /**
     * Applies this length to a full horoscope: [SHORT] keeps only the first
     * sentence, [FULL] returns the text unchanged.
     */
    fun format(text: String): String = when (this) {
        FULL -> text
        SHORT -> {
            val firstSentence = text.substringBefore(". ")
            if (firstSentence.length == text.length) text else "$firstSentence."
        }
    }
}
