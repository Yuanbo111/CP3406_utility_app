package com.example.issac

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.issac.ui.main.MainScreen
import com.example.issac.ui.settings.SettingsScreen

private enum class Tab { Today, Settings }

@Composable
fun UtilityApp() {
    var currentTab by remember { mutableStateOf(Tab.Today) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == Tab.Today,
                    onClick = { currentTab = Tab.Today },
                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                    label = { Text("Today") }
                )
                NavigationBarItem(
                    selected = currentTab == Tab.Settings,
                    onClick = { currentTab = Tab.Settings },
                    icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { innerPadding ->
        when (currentTab) {
            Tab.Today -> MainScreen(modifier = Modifier.padding(innerPadding))
            Tab.Settings -> SettingsScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
