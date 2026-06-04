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
 * is true; on failure [error] holds a user-facing message. [readingLength]
 * mirrors the user's Settings choice and controls how much of the reading shows.
 */
data class MainUiState(
    val birthDate: LocalDate? = null,
    val zodiac: Zodiac? = null,
    val age: Period? = null,
    val horoscope: Horoscope? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val readingLength: ReadingLength = ReadingLength.FULL,
)
