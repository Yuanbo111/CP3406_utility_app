package com.example.issac.data.horoscope

import com.example.issac.data.horoscope.dto.HoroscopeData
import com.example.issac.data.horoscope.dto.HoroscopeResponse
import com.example.issac.domain.model.Zodiac
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException
import java.time.LocalDate

class HoroscopeRepositoryTest {

    @Test
    fun `maps a successful response into the Horoscope domain model`() = runBlocking {
        val api = FakeHoroscopeApi(
            response = HoroscopeResponse(
                HoroscopeData(
                    date = "2026-06-04",
                    period = "daily",
                    sign = "Leo",
                    horoscope = "A good day for practical tasks.",
                ),
            ),
        )
        val repository = HoroscopeRepository(api)

        val result = repository.getDailyHoroscope(Zodiac.LEO)

        assertTrue(result.isSuccess)
        val horoscope = result.getOrThrow()
        assertEquals(LocalDate.of(2026, 6, 4), horoscope.date)
        assertEquals("A good day for practical tasks.", horoscope.text)
    }

    @Test
    fun `sends the capitalised zodiac name to the API`() = runBlocking {
        val api = FakeHoroscopeApi(
            response = HoroscopeResponse(
                HoroscopeData("2026-06-04", "daily", "Leo", "text"),
            ),
        )
        val repository = HoroscopeRepository(api)

        repository.getDailyHoroscope(Zodiac.LEO)

        assertEquals("Leo", api.lastSign)
    }

    @Test
    fun `returns a failure when the API throws`() = runBlocking {
        val api = FakeHoroscopeApi(error = IOException("no network"))
        val repository = HoroscopeRepository(api)

        val result = repository.getDailyHoroscope(Zodiac.LEO)

        assertTrue(result.isFailure)
    }

    @Test
    fun `rejects a corrupted reading instead of passing it to the UI`() = runBlocking {
        // Trimmed-down version of real garbage the live API returned once.
        val garbage = "_GPU heck  ((ryn ,raki Req  cedes,\n,\n ,,, [,. ,\n,, � ,,  ,( ,,,,\n,,\n,\n[\n _ .,[., \n,,, , ,   ,  \n , -deals \n,,  not, ( ,"
        val api = FakeHoroscopeApi(
            response = HoroscopeResponse(
                HoroscopeData("2026-06-11", "daily", "Pisces", garbage),
            ),
        )
        val repository = HoroscopeRepository(api)

        val result = repository.getDailyHoroscope(Zodiac.PISCES)

        assertTrue(result.isFailure)
    }
}

/** Test double for [HoroscopeApi] — returns a canned response or throws, no real network. */
private class FakeHoroscopeApi(
    private val response: HoroscopeResponse? = null,
    private val error: Throwable? = null,
) : HoroscopeApi {

    var lastSign: String? = null
        private set

    override suspend fun getDailyHoroscope(sign: String, day: String): HoroscopeResponse {
        lastSign = sign
        error?.let { throw it }
        return response!!
    }
}
