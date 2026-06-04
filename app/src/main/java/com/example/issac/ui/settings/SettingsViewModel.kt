package com.example.issac.ui.settings

import androidx.lifecycle.ViewModel
import com.example.issac.data.settings.ReadingLength
import com.example.issac.data.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Exposes the user's settings to the Settings screen and writes changes back to
 * the shared [SettingsRepository]. Because the repository is a singleton, those
 * changes are immediately visible to the Today screen too.
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val readingLength: StateFlow<ReadingLength> = settingsRepository.readingLength

    fun onReadingLengthChange(length: ReadingLength) {
        settingsRepository.setReadingLength(length)
    }
}
