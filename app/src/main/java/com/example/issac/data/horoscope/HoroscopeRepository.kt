package com.example.issac.data.horoscope

import com.example.issac.domain.model.Horoscope
import com.example.issac.domain.model.Zodiac
import java.time.LocalDate
import kotlin.coroutines.cancellation.CancellationException

/**
 * Single source of truth for daily horoscopes. The rest of the app depends only
 * on this class — never on Retrofit or the JSON DTO directly.
 *
 * It calls [HoroscopeApi], maps the response into the clean [Horoscope] domain
 * model, and reports any problem as a failed [Result] instead of throwing, so
 * the ViewModel can show an error state rather than crashing.
 */
class HoroscopeRepository(
    private val api: HoroscopeApi,
) {

    suspend fun getDailyHoroscope(zodiac: Zodiac): Result<Horoscope> {
        return try {
            val response = api.getDailyHoroscope(sign = zodiac.displayName)
            val horoscope = Horoscope(
                date = LocalDate.parse(response.data.date),
                text = response.data.horoscope,
            )
            Result.success(horoscope)
        } catch (e: CancellationException) {
            throw e // Never swallow coroutine cancellation — always let it propagate.
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
