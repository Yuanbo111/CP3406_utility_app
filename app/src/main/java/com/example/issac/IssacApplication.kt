package com.example.issac

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point annotated with @HiltAndroidApp. This triggers Hilt's
 * code generation and creates the application-wide dependency container that
 * every @AndroidEntryPoint and @HiltViewModel draws from.
 *
 * Registered in AndroidManifest.xml via `android:name=".IssacApplication"`.
 */
@HiltAndroidApp
class IssacApplication : Application()
