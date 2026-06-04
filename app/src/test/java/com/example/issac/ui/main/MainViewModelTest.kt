package com.example.issac.ui.main

import com.example.issac.domain.model.Zodiac
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset

class MainViewModelTest {

    private val viewModel = MainViewModel()

    @Test
    fun `initial state is empty`() {
        val state = viewModel.uiState.value
        assertNull(state.birthDate)
        assertNull(state.zodiac)
        assertNull(state.age)
    }

    @Test
    fun `selecting a date populates birth date, zodiac and age`() {
        val birthDate = LocalDate.of(1990, 8, 10)
        val epochMillis = birthDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

        viewModel.onBirthDateSelected(epochMillis)

        val state = viewModel.uiState.value
        assertEquals(birthDate, state.birthDate)
        assertEquals(Zodiac.LEO, state.zodiac)
        assertNotNull(state.age)
    }

    @Test
    fun `a null selection leaves the state unchanged`() {
        viewModel.onBirthDateSelected(null)

        assertNull(viewModel.uiState.value.birthDate)
    }
}
