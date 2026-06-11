package com.example.issac.ui.main

import com.example.issac.data.settings.ReadingLength
import com.example.issac.domain.model.Horoscope
import com.example.issac.domain.model.Zodiac
import java.time.LocalDate
import java.time.Period

/**
 * Immutable snapshot of everything the Today screen needs to draw itself.
 *
 * The screen is a pure function of this state: whenever [MainViewModel] emits a
 * new instance, Compose recomposes. A null [birthDate] is the initial
 * "no date chosen yet" state. While the horoscope is being fetched [isLoading]
 * is true; on failure [isError] is true and the UI shows the error message.
 * [readingLength] mirrors the user's Settings choice and controls how much of
 * the reading shows. [backgroundImageUrl] is the NASA space photo behind the
 * screen (null = show the gradient only); [isBackgroundLoading] is true while a
 * new one is being fetched.
 */
data class MainUiState(
    val birthDate: LocalDate? = null,
    val zodiac: Zodiac? = null,
    val age: Period? = null,
    val horoscope: Horoscope? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val readingLength: ReadingLength = ReadingLength.FULL,
    val backgroundImageUrl: String? = null,
    val isBackgroundLoading: Boolean = false,
)
