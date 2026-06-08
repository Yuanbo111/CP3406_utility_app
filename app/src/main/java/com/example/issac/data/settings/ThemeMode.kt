package com.example.issac.data.settings

import androidx.annotation.StringRes
import com.example.issac.R

/**
 * Which colour theme the app uses. [SYSTEM] follows the device's light/dark
 * setting; [LIGHT] and [DARK] override it from within the app.
 */
enum class ThemeMode(@StringRes val labelRes: Int) {
    SYSTEM(R.string.theme_system),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark),
}
