package com.example.issac.ui.main

import androidx.lifecycle.ViewModel
import com.example.issac.domain.usecase.DetermineZodiacUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset

/**
 * Owns the state for the Today screen and the age/zodiac computation that used
 * to live inside the composable. Because a ViewModel survives configuration
 * changes, rotating the device no longer discards the user's result.
 */
class MainViewModel : ViewModel() {

    private val determineZodiac = DetermineZodiacUseCase()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Handles a confirmed date from the picker. Material 3's DatePicker reports
     * the selection as UTC-midnight epoch millis, so we read it back in UTC;
     * converting through the device's local zone could shift the date by a day.
     */
    fun onBirthDateSelected(epochMillis: Long?) {
        if (epochMillis == null) return

        val birthDate = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        _uiState.update { state ->
            state.copy(
                birthDate = birthDate,
                zodiac = determineZodiac(birthDate),
                age = Period.between(birthDate, LocalDate.now()),
            )
        }
    }
}
