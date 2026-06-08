package com.example.issac.ui.theme

import androidx.compose.ui.graphics.Color

// "Night sky" brand palette: a deep indigo primary with a warm gold star accent.
// Light and dark variants of each role keep contrast readable in both themes.

// Indigo — primary
val Indigo40 = Color(0xFF3A4A8F) // primary on light
val Indigo80 = Color(0xFFB9C3FF) // primary on dark
val IndigoContainerLight = Color(0xFFDCE1FF)
val IndigoContainerDark = Color(0xFF21317A)
val OnIndigoLight = Color(0xFFFFFFFF)
val OnIndigoDark = Color(0xFF06205F)
val OnIndigoContainerLight = Color(0xFF001257)

// Slate — secondary
val Slate40 = Color(0xFF595E72) // secondary on light
val Slate80 = Color(0xFFC2C6DD) // secondary on dark

// Gold — tertiary (the "star" accent)
val Gold40 = Color(0xFF6F5B00) // tertiary on light (dark gold for contrast)
val Gold80 = Color(0xFFE9C44C) // tertiary on dark (bright gold)
val GoldContainerLight = Color(0xFFFCE186)
val GoldContainerDark = Color(0xFF534500)
val OnGoldContainerLight = Color(0xFF221B00)

// Night surfaces — keep the dark theme genuinely dark, like a night sky
val NightSurface = Color(0xFF12131A)
val LightSurface = Color(0xFFFDFBFF)

// Fixed bright gold for the hero title, which always sits on the dark
// background image (so it can't follow the light/dark theme tertiary).
val StarGold = Color(0xFFF2C94C)
