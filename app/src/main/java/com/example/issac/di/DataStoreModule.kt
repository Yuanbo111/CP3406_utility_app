package com.example.issac.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

// The delegate must live at file level so exactly one DataStore ever exists for
// this file name — a second instance on the same file is a runtime error.
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "daily_star_settings",
)

/** Provides the on-disk settings store and the long-lived scope that writes to it. */
@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideSettingsDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.settingsDataStore

    // Settings writes must not die with a screen's ViewModel, so repositories
    // run them on this application-lifetime scope instead of viewModelScope.
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
}
