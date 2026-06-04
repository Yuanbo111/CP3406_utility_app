package com.example.issac.data.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single in-memory source of truth for user settings. Provided as a Hilt
 * singleton so the Settings screen (which changes settings) and the Today screen
 * (which reacts to them) share the exact same instance and stay in sync.
 */
@Singleton
class SettingsRepository @Inject constructor() {

    private val _readingLength = MutableStateFlow(ReadingLength.FULL)
    val readingLength: StateFlow<ReadingLength> = _readingLength.asStateFlow()

    fun setReadingLength(length: ReadingLength) {
        _readingLength.value = length
    }
}
