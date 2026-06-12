package com.example.issac.data.settings

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryTest {

    // Unconfined = launched coroutines run to completion immediately, so the
    // DataStore round-trip (write -> flow -> StateFlow) finishes synchronously.
    private val dispatcher = UnconfinedTestDispatcher()

    private fun repositoryOn(store: FakePreferencesDataStore) =
        SettingsRepository(store, CoroutineScope(dispatcher))

    @Test
    fun `defaults apply before anything is saved`() {
        val repository = repositoryOn(FakePreferencesDataStore())

        assertEquals(ReadingLength.FULL, repository.readingLength.value)
        assertEquals(ThemeMode.SYSTEM, repository.themeMode.value)
        assertNull(repository.birthDate.value)
    }

    @Test
    fun `saved values are emitted back`() {
        val repository = repositoryOn(FakePreferencesDataStore())

        repository.setReadingLength(ReadingLength.SHORT)
        repository.setThemeMode(ThemeMode.DARK)
        repository.setBirthDate(LocalDate.of(1990, 8, 10))

        assertEquals(ReadingLength.SHORT, repository.readingLength.value)
        assertEquals(ThemeMode.DARK, repository.themeMode.value)
        assertEquals(LocalDate.of(1990, 8, 10), repository.birthDate.value)
    }

    @Test
    fun `a fresh repository on the same store sees previously saved values`() {
        // Two repository instances over one store = the closest a unit test
        // gets to killing the process and relaunching the app.
        val store = FakePreferencesDataStore()
        repositoryOn(store).apply {
            setReadingLength(ReadingLength.SHORT)
            setBirthDate(LocalDate.of(2001, 1, 28))
        }

        val relaunched = repositoryOn(store)

        assertEquals(ReadingLength.SHORT, relaunched.readingLength.value)
        assertEquals(LocalDate.of(2001, 1, 28), relaunched.birthDate.value)
    }
}
