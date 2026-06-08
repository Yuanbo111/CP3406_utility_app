package com.example.issac.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = OnIndigoDark,
    primaryContainer = IndigoContainerDark,
    onPrimaryContainer = IndigoContainerLight,
    secondary = Slate80,
    tertiary = Gold80,
    tertiaryContainer = GoldContainerDark,
    onTertiaryContainer = GoldContainerLight,
    background = NightSurface,
    surface = NightSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    onPrimary = OnIndigoLight,
    primaryContainer = IndigoContainerLight,
    onPrimaryContainer = OnIndigoContainerLight,
    secondary = Slate40,
    tertiary = Gold40,
    tertiaryContainer = GoldContainerLight,
    onTertiaryContainer = OnGoldContainerLight,
    background = LightSurface,
    surface = LightSurface,
)

@Composable
fun IssacTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Off by default so the Daily Star brand colours show. When true on Android
    // 12+, the system wallpaper palette (Material You) overrides the brand.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}