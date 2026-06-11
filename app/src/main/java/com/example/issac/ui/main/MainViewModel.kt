package com.example.issac.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.issac.data.apod.ApodRepository
import com.example.issac.data.horoscope.HoroscopeRepository
import com.example.issac.data.settings.SettingsRepository
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
 * Owns the Today screen's state. Hilt injects the [HoroscopeRepository] and the
 * shared [SettingsRepository].
 *
 * When the user picks a birth date we compute their age and zodiac immediately,
 * then fetch today's horoscope in the background. The reading-length setting is
 * observed from the repository, so changing it in Settings reformats the reading
 * shown here without re-fetching.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val horoscopeRepository: HoroscopeRepository,
    private val settingsRepository: SettingsRepository,
    private val apodRepository: ApodRepository,
) : ViewModel() {

    private val determineZodiac = DetermineZodiacUseCase()

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Photos waiting to be shown — one API call pays for several shuffles.
     * Declared BEFORE the init block: Kotlin runs property initializers and
     * init blocks top-to-bottom, and init's loadBackground() touches this queue.
     */
    private val backgroundQueue = ArrayDeque<String>()

    init {
        viewModelScope.launch {
            settingsRepository.readingLength.collect { length ->
                _uiState.update { it.copy(readingLength = length) }
            }
        }
        loadBackground()
    }

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
                isError = false,
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
                        state.copy(horoscope = horoscope, isLoading = false, isError = false)
                    },
                    onFailure = {
                        state.copy(horoscope = null, isLoading = false, isError = true)
                    },
                )
            }
        }
    }

    /** Shows the next queued NASA space photo as the background. */
    fun refreshBackground() = loadBackground()

    private fun loadBackground() {
        viewModelScope.launch {
            _uiState.update { it.copy(isBackgroundLoading = true) }
            if (backgroundQueue.isEmpty()) {
                apodRepository.getRandomImageUrls()
                    .onSuccess { urls -> backgroundQueue.addAll(urls) }
            }
            val url = backgroundQueue.removeFirstOrNull()
            _uiState.update { state ->
                if (url == null) {
                    state.copy(isBackgroundLoading = false) // keep the gradient
                } else {
                    state.copy(
                        backgroundImageUrl = url,
                        // The UI pre-downloads this one so the next shuffle is instant.
                        nextBackgroundImageUrl = backgroundQueue.firstOrNull(),
                        // Keep spinning until the photo itself has downloaded — the
                        // UI reports that via onBackgroundImageLoaded(). If the
                        // random pick is the photo already showing, stop right away.
                        isBackgroundLoading = url != state.backgroundImageUrl,
                    )
                }
            }
            // Top up before the queue runs dry, so there is always a photo ready
            // to pre-download for the shuffle after this one.
            if (backgroundQueue.isEmpty()) {
                apodRepository.getRandomImageUrls().onSuccess { urls ->
                    backgroundQueue.addAll(urls)
                    _uiState.update { it.copy(nextBackgroundImageUrl = backgroundQueue.firstOrNull()) }
                }
            }
        }
    }

    /** The UI calls this once Coil has finished loading the background photo. */
    fun onBackgroundImageLoaded() {
        _uiState.update { it.copy(isBackgroundLoading = false) }
    }
}
