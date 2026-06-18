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

// Hero night-sky gradient — the Today backdrop, drawn behind the NASA photo
// and scrim. Always dark in both themes so the gold title stays readable.
val NightGradientTop = Color(0xFF222C63)
val NightGradientBottom = Color(0xFF0A0B14)

// "Glass" destiny-card colours — the card sits directly on the photo, so like
// StarGold these are fixed rather than theme roles: a faint dark veil that the
// photo shows through, with light text that stays readable in both themes.
val GlassDark = Color(0x59000000)  // 35% black
val OnGlass = Color(0xFFF4F3F9)    // near-white text on the glass
val GlassError = Color(0xFFFFB4AB) // soft light red, readable on the dark glass

// Light-theme "frosted glass" variants of the destiny card. In light theme the
// card flips to a pale frost with dark text, so the Today screen reads bright;
// dark theme keeps the dark glass above. Both still let the photo show through
// and stay legible over bright AND dark NASA photos (the scrim sits under the
// card in both themes). GlassLight's alpha is the main tuning knob.
val GlassLight = Color(0xA6FFFFFF)      // 65% white frost
val OnGlassLight = Color(0xFF1B1B2F)    // deep indigo-black text on the frost
val GlassErrorLight = Color(0xFFB3261E) // deep red, readable on the pale frost
