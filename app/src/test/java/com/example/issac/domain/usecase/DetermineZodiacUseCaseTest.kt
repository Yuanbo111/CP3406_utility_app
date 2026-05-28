package com.example.issac.domain.usecase

import com.example.issac.domain.model.Zodiac
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DetermineZodiacUseCaseTest {

    private val determineZodiac = DetermineZodiacUseCase()

    @Test
    fun `mid-sign dates map to the correct zodiac`() {
        assertEquals(Zodiac.LEO, determineZodiac(LocalDate.of(1990, 8, 10)))
        assertEquals(Zodiac.GEMINI, determineZodiac(LocalDate.of(1990, 6, 18)))
        assertEquals(Zodiac.CAPRICORN, determineZodiac(LocalDate.of(2000, 1, 1)))
    }

    @Test
    fun `cusp boundary dates fall on the expected side`() {
        // Aries begins on March 21
        assertEquals(Zodiac.PISCES, determineZodiac(LocalDate.of(2001, 3, 20)))
        assertEquals(Zodiac.ARIES, determineZodiac(LocalDate.of(2001, 3, 21)))
        // Capricorn begins on December 22 (wraps across the new year)
        assertEquals(Zodiac.SAGITTARIUS, determineZodiac(LocalDate.of(2001, 12, 21)))
        assertEquals(Zodiac.CAPRICORN, determineZodiac(LocalDate.of(2001, 12, 22)))
    }
}
