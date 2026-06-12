package com.example.issac.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory stand-in for Preferences DataStore — same interface, no files, no
 * async machinery, so tests stay fast and deterministic.
 */
class FakePreferencesDataStore : DataStore<Preferences> {

    private val state = MutableStateFlow<Preferences>(emptyPreferences())

    override val data: Flow<Preferences> = state

    override suspend fun updateData(
        transform: suspend (t: Preferences) -> Preferences,
    ): Preferences {
        // toPreferences() snapshots the MutablePreferences that edit passes in,
        // matching the immutability guarantee of the real store.
        val next = transform(state.value).toPreferences()
        state.value = next
        return next
    }
}
