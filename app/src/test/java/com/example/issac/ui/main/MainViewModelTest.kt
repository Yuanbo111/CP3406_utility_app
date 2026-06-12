package com.example.issac.ui.main

import com.example.issac.data.apod.ApodApi
import com.example.issac.data.apod.ApodRepository
import com.example.issac.data.apod.dto.ApodResponse
import com.example.issac.data.horoscope.HoroscopeApi
import com.example.issac.data.horoscope.HoroscopeRepository
import com.example.issac.data.horoscope.dto.HoroscopeData
import com.example.issac.data.horoscope.dto.HoroscopeResponse
import com.example.issac.data.settings.FakePreferencesDataStore
import com.example.issac.data.settings.SettingsRepository
import com.example.issac.domain.model.Zodiac
import kotlinx.coroutines.CoroutineScope
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

    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        // viewModelScope dispatches on Dispatchers.Main, which doesn't exist in a
        // plain JVM unit test — swap in a test dispatcher that runs eagerly.
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun settingsRepository() =
        SettingsRepository(FakePreferencesDataStore(), CoroutineScope(dispatcher))

    // Fake readings must be at least 20 mostly-letter characters, or the
    // repository's corrupted-reading guard rejects them as garbage.
    private fun viewModelWith(
        text: String = "A calm and bright day ahead.",
        error: Throwable? = null,
        settings: SettingsRepository = settingsRepository(),
    ) = MainViewModel(
        HoroscopeRepository(FakeHoroscopeApi(text, error)),
        settings,
        ApodRepository(FakeApodApi()),
    )

    private fun millisFor(date: LocalDate): Long =
        date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    // The date now travels picker -> DataStore -> repository flow -> ViewModel
    // collector -> horoscope fetch. Even an eager dispatcher queues the deeper
    // hops, so tests drain the scheduler before asserting on the final state.
    private fun awaitIdle() = dispatcher.scheduler.advanceUntilIdle()

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
        val viewModel = viewModelWith(text = "A bright day ahead for you, Leo.")

        viewModel.onBirthDateSelected(millisFor(LocalDate.of(1990, 8, 10)))
        awaitIdle()

        val state = viewModel.uiState.value
        assertEquals(LocalDate.of(1990, 8, 10), state.birthDate)
        assertEquals(Zodiac.LEO, state.zodiac)
        assertEquals("A bright day ahead for you, Leo.", state.horoscope?.text)
        assertFalse(state.isLoading)
    }

    @Test
    fun `a network error is surfaced as an error message`() {
        val viewModel = viewModelWith(error = IOException("no network"))

        viewModel.onBirthDateSelected(millisFor(LocalDate.of(1990, 8, 10)))
        awaitIdle()

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

    @Test
    fun `a previously saved birth date is restored on launch`() {
        // The date is already on disk before this ViewModel ever exists —
        // exactly what happens when the app is reopened after process death.
        val settings = settingsRepository().apply {
            setBirthDate(LocalDate.of(1990, 8, 10))
        }

        val viewModel = viewModelWith(text = "Welcome back to your stars, Leo.", settings = settings)
        awaitIdle()

        val state = viewModel.uiState.value
        assertEquals(LocalDate.of(1990, 8, 10), state.birthDate)
        assertEquals(Zodiac.LEO, state.zodiac)
        assertEquals("Welcome back to your stars, Leo.", state.horoscope?.text)
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
