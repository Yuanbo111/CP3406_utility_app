package com.example.issac.data.horoscope

import com.example.issac.domain.model.Horoscope
import com.example.issac.domain.model.Zodiac
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

/**
 * Single source of truth for daily horoscopes. The rest of the app depends only
 * on this class — never on Retrofit or the JSON DTO directly.
 *
 * It calls [HoroscopeApi], maps the response into the clean [Horoscope] domain
 * model, and reports any problem as a failed [Result] instead of throwing, so
 * the ViewModel can show an error state rather than crashing.
 */
@Singleton
class HoroscopeRepository @Inject constructor(
    private val api: HoroscopeApi,
) {

    suspend fun getDailyHoroscope(zodiac: Zodiac): Result<Horoscope> {
        return try {
            val response = api.getDailyHoroscope(sign = zodiac.displayName)
            val text = response.data.horoscope
            if (!looksLikeProse(text)) {
                return Result.failure(IllegalStateException("API returned a corrupted reading"))
            }
            val horoscope = Horoscope(
                date = LocalDate.parse(response.data.date),
                text = text,
            )
            Result.success(horoscope)
        } catch (e: CancellationException) {
            throw e // Never swallow coroutine cancellation — always let it propagate.
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * The API occasionally returns corrupted, machine-generated gibberish
     * (observed live: a "reading" that was mostly commas, symbols and �
     * characters). Real prose is mostly letters, so reject anything that is too
     * short or where letters don't clearly dominate — better to show the error
     * state than nonsense.
     */
    private fun looksLikeProse(text: String): Boolean {
        if (text.length < 20 || '�' in text) return false
        val letters = text.count { it.isLetter() }
        return letters.toDouble() / text.length > 0.6
    }
}
