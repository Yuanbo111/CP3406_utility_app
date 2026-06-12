package com.example.issac.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single source of truth for everything the user has told us: birth date,
 * reading length and theme. Values live in Preferences DataStore, so they
 * survive the process being killed — the whole point of a "set it once,
 * glance at it daily" app. Provided as a Hilt singleton so the Settings
 * screen (which changes values) and the Today screen (which reacts to them)
 * observe the exact same flows.
 *
 * Reads go disk -> Flow -> StateFlow; writes go through [DataStore.edit] on
 * the application scope and arrive back via that same read path, so the UI
 * only ever shows what is actually stored.
 */
@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val scope: CoroutineScope,
) {

    private object Keys {
        val READING_LENGTH = stringPreferencesKey("reading_length")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val BIRTH_DATE = longPreferencesKey("birth_date_epoch_day")
    }

    val readingLength: StateFlow<ReadingLength> = dataStore.data
        .map { prefs -> readingLengthFrom(prefs) }
        .stateIn(scope, SharingStarted.Eagerly, ReadingLength.FULL)

    fun setReadingLength(length: ReadingLength) {
        scope.launch { dataStore.edit { it[Keys.READING_LENGTH] = length.name } }
    }

    val themeMode: StateFlow<ThemeMode> = dataStore.data
        .map { prefs -> themeModeFrom(prefs) }
        .stateIn(scope, SharingStarted.Eagerly, ThemeMode.SYSTEM)

    fun setThemeMode(mode: ThemeMode) {
        scope.launch { dataStore.edit { it[Keys.THEME_MODE] = mode.name } }
    }

    /** Null until the user has picked a birth date for the first time. */
    val birthDate: StateFlow<LocalDate?> = dataStore.data
        .map { prefs -> prefs[Keys.BIRTH_DATE]?.let(LocalDate::ofEpochDay) }
        .stateIn(scope, SharingStarted.Eagerly, null)

    fun setBirthDate(date: LocalDate) {
        scope.launch { dataStore.edit { it[Keys.BIRTH_DATE] = date.toEpochDay() } }
    }

    // Stored as enum names; entries.find tolerates values written by an older
    // or newer version of the app instead of crashing on valueOf.
    private fun readingLengthFrom(prefs: Preferences): ReadingLength =
        ReadingLength.entries.find { it.name == prefs[Keys.READING_LENGTH] } ?: ReadingLength.FULL

    private fun themeModeFrom(prefs: Preferences): ThemeMode =
        ThemeMode.entries.find { it.name == prefs[Keys.THEME_MODE] } ?: ThemeMode.SYSTEM
}
