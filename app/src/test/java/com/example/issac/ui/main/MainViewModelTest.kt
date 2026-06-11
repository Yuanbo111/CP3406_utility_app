package com.example.issac.ui.main

import com.example.issac.data.apod.ApodApi
import com.example.issac.data.apod.ApodRepository
import com.example.issac.data.apod.dto.ApodResponse
import com.example.issac.data.horoscope.HoroscopeApi
import com.example.issac.data.horoscope.HoroscopeRepository
import com.example.issac.data.horoscope.dto.HoroscopeData
import com.example.issac.data.horoscope.dto.HoroscopeResponse
import com.example.issac.data.settings.SettingsRepository
import com.example.issac.domain.model.Zodiac
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @Before
    fun setUp() {
        // viewModelScope dispatches on Dispatchers.Main, which doesn't exist in a
        // plain JVM unit test — swap in a test dispatcher that runs eagerly.
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModelWith(
        text: String = "A bright day ahead.",
        error: Throwable? = null,
    ) = MainViewModel(
        HoroscopeRepository(FakeHoroscopeApi(text, error)),
        SettingsRepository(),
        ApodRepository(FakeApodApi()),
    )

    private fun millisFor(date: LocalDate): Long =
        date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    @Test
    fun `initial state is empty`() {
        val state = viewModelWith().uiState.value
        assertNull(state.birthDate)
        assertNull(state.zodiac)
        assertNull(state.age)
        assertNull(state.horoscope)
        assertFalse(state.isLoading)
        assertFalse(state.isError)
    }

    @Test
    fun `selecting a date populates zodiac, age and horoscope`() {
        val viewModel = viewModelWith(text = "A bright Leo day.")

        viewModel.onBirthDateSelected(millisFor(LocalDate.of(1990, 8, 10)))

        val state = viewModel.uiState.value
        assertEquals(LocalDate.of(1990, 8, 10), state.birthDate)
        assertEquals(Zodiac.LEO, state.zodiac)
        assertEquals("A bright Leo day.", state.horoscope?.text)
        assertFalse(state.isLoading)
    }

    @Test
    fun `a network error is surfaced as an error message`() {
        val viewModel = viewModelWith(error = IOException("no network"))

        viewModel.onBirthDateSelected(millisFor(LocalDate.of(1990, 8, 10)))

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.horoscope)
        assertTrue(state.isError)
    }

    @Test
    fun `a null selection leaves the state unchanged`() {
        val viewModel = viewModelWith()

        viewModel.onBirthDateSelected(null)

        assertNull(viewModel.uiState.value.birthDate)
    }
}

/** Test double for [HoroscopeApi] — returns a canned reading or throws, no real network. */
private class FakeHoroscopeApi(
    private val text: String,
    private val error: Throwable? = null,
) : HoroscopeApi {
    override suspend fun getDailyHoroscope(sign: String, day: String): HoroscopeResponse {
        error?.let { throw it }
        return HoroscopeResponse(HoroscopeData("2026-06-04", "daily", sign, text))
    }
}

/** Test double for [ApodApi] — returns one canned image, no real network. */
private class FakeApodApi : ApodApi {
    override suspend fun getRandomPictures(count: Int, apiKey: String): List<ApodResponse> =
        listOf(ApodResponse(title = "Test", url = "https://example.com/star.jpg", mediaType = "image"))
}
