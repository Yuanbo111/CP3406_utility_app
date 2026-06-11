package com.example.issac

import android.app.Activity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.example.issac.ui.main.MainScreen
import com.example.issac.ui.settings.SettingsScreen
import com.example.issac.ui.theme.NightGradientBottom
import com.example.issac.ui.theme.OnGlass
import com.example.issac.ui.theme.StarGold

private enum class Tab { Today, Settings }

@Composable
fun UtilityApp(darkTheme: Boolean) {
    var currentTab by remember { mutableStateOf(Tab.Today) }

    // Status-bar icon colour: Today always has the dark photo behind the
    // (transparent) status bar, so its icons must stay light; Settings sits on
    // the theme surface, so there the icons follow the theme instead.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                currentTab == Tab.Settings && !darkTheme
        }
    }

    Scaffold(
        bottomBar = {
            // "Glass" bar in the same language as the destiny card: on Today a
            // translucent indigo veil lets the NASA photo glow through (the
            // screen's scrim already keeps it dark enough for the labels);
            // on Settings there is only a flat surface behind it, so the bar
            // goes opaque night indigo instead of washing out.
            NavigationBar(
                containerColor = if (currentTab == Tab.Today) {
                    NightGradientBottom.copy(alpha = 0.45f)
                } else {
                    NightGradientBottom
                },
            ) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = StarGold,
                    selectedTextColor = StarGold,
                    indicatorColor = OnGlass.copy(alpha = 0.15f),
                    unselectedIconColor = OnGlass.copy(alpha = 0.6f),
                    unselectedTextColor = OnGlass.copy(alpha = 0.6f),
                )
                NavigationBarItem(
                    selected = currentTab == Tab.Today,
                    onClick = { currentTab = Tab.Today },
                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_today)) },
                    colors = itemColors,
                )
                NavigationBarItem(
                    selected = currentTab == Tab.Settings,
                    onClick = { currentTab = Tab.Settings },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_settings)) },
                    colors = itemColors,
                )
            }
        },
        // Hand all insets to the screens themselves: Today draws the photo
        // behind the status bar AND the glass bottom bar. innerPadding still
        // carries the bottom bar's height for the content that must clear it.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        when (currentTab) {
            // The screen fills the window (photo runs behind the glass bar);
            // only its foreground content pads itself clear of the bar.
            Tab.Today -> MainScreen(contentPadding = innerPadding)
            Tab.Settings -> SettingsScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
