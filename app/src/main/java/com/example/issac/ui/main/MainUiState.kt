package com.example.issac.ui.main

import com.example.issac.domain.model.Zodiac
import java.time.LocalDate
import java.time.Period

/**
 * Immutable snapshot of everything the Today screen needs to draw itself.
 *
 * The screen is a pure function of this state: whenever [MainViewModel] emits a
 * new instance, Compose recomposes. A null [birthDate] is the initial
 * "no date chosen yet" state, in which case [zodiac] and [age] are also null.
 */
data class MainUiState(
    val birthDate: LocalDate? = null,
    val zodiac: Zodiac? = null,
    val age: Period? = null,
)
