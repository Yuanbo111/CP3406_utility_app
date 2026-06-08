package com.example.issac.data.settings

import androidx.annotation.StringRes
import com.example.issac.R

/** How much of the daily horoscope to show on the Today screen. */
enum class ReadingLength(@StringRes val labelRes: Int) {
    SHORT(R.string.reading_length_short),
    FULL(R.string.reading_length_full);

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
