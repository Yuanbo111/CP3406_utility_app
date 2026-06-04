package com.example.issac.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.issac.data.horoscope.HoroscopeRepository
import com.example.issac.domain.model.Zodiac
import com.example.issac.domain.usecase.DetermineZodiacUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * Owns the Today screen's state. Hilt injects the [HoroscopeRepository].
 *
 * When the user picks a birth date we compute their age and zodiac immediately,
 * then fetch today's horoscope in the background and fold success or failure
 * into the state, so the screen can show a loading, loaded, or error view.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val horoscopeRepository: HoroscopeRepository,
) : ViewModel() {

    private val determineZodiac = DetermineZodiacUseCase()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Handles a confirmed date from the picker. Material 3's DatePicker reports
     * UTC-midnight epoch millis, so we read it back in UTC to avoid a day shift.
     */
    fun onBirthDateSelected(epochMillis: Long?) {
        if (epochMillis == null) return

        val birthDate = Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        val zodiac = determineZodiac(birthDate)

        _uiState.update { state ->
            state.copy(
                birthDate = birthDate,
                zodiac = zodiac,
                age = Period.between(birthDate, LocalDate.now()),
                horoscope = null,
                isLoading = true,
                error = null,
            )
        }

        loadHoroscope(zodiac)
    }

    private fun loadHoroscope(zodiac: Zodiac) {
        viewModelScope.launch {
            val result = horoscopeRepository.getDailyHoroscope(zodiac)
            _uiState.update { state ->
                result.fold(
                    onSuccess = { horoscope ->
                        state.copy(horoscope = horoscope, isLoading = false, error = null)
                    },
                    onFailure = {
                        state.copy(
                            horoscope = null,
                            isLoading = false,
                            error = "Couldn't load today's reading. Check your connection and try again.",
                        )
                    },
                )
            }
        }
    }
}
