package com.example.issac.domain.usecase

import com.example.issac.domain.model.Zodiac
import java.time.LocalDate

/**
 * Maps a birth date to its western zodiac sign. Kept as a pure, dependency-free
 * use case so the rule is trivial to unit test and reuse outside the UI layer.
 */
class DetermineZodiacUseCase {

    operator fun invoke(date: LocalDate): Zodiac {
        val day = date.dayOfMonth
        return when (date.monthValue) {
            1 -> if (day < 20) Zodiac.CAPRICORN else Zodiac.AQUARIUS
            2 -> if (day < 19) Zodiac.AQUARIUS else Zodiac.PISCES
            3 -> if (day < 21) Zodiac.PISCES else Zodiac.ARIES
            4 -> if (day < 20) Zodiac.ARIES else Zodiac.TAURUS
            5 -> if (day < 21) Zodiac.TAURUS else Zodiac.GEMINI
            6 -> if (day < 21) Zodiac.GEMINI else Zodiac.CANCER
            7 -> if (day < 23) Zodiac.CANCER else Zodiac.LEO
            8 -> if (day < 23) Zodiac.LEO else Zodiac.VIRGO
            9 -> if (day < 23) Zodiac.VIRGO else Zodiac.LIBRA
            10 -> if (day < 23) Zodiac.LIBRA else Zodiac.SCORPIO
            11 -> if (day < 22) Zodiac.SCORPIO else Zodiac.SAGITTARIUS
            12 -> if (day < 22) Zodiac.SAGITTARIUS else Zodiac.CAPRICORN
            else -> error("Invalid month: ${date.monthValue}")
        }
    }
}
