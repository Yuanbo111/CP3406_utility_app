package com.example.issac.domain.model

import java.time.LocalDate

/**
 * A daily horoscope reading in a clean, UI-friendly shape.
 *
 * Deliberately separate from the network DTO
 * ([com.example.issac.data.horoscope.dto.HoroscopeResponse]): the repository
 * maps the JSON into this model, so the rest of the app never depends on the
 * wire format. If the API changes, only that mapping has to change.
 */
data class Horoscope(
    val date: LocalDate,
    val text: String,
)
