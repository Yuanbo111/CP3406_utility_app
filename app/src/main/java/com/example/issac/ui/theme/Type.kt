package com.example.issac.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.issac.R

/**
 * Marcellus (Google Fonts, OFL licence, bundled in res/font) — a Roman-
 * inscription serif that gives titles a celestial, engraved feel to match
 * the night-sky theme. It ships in a single Regular weight, so heading
 * styles deliberately never ask for bold; the letterforms carry the weight.
 */
val MarcellusFamily = FontFamily(Font(R.font.marcellus_regular))

// Headings use Marcellus at the standard Material 3 sizes; body and label
// text stays on the default sans so long readings remain easy to read.
val Typography = Typography(
    displayLarge = TextStyle(fontFamily = MarcellusFamily, fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium = TextStyle(fontFamily = MarcellusFamily, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall = TextStyle(fontFamily = MarcellusFamily, fontSize = 36.sp, lineHeight = 44.sp),
    headlineLarge = TextStyle(fontFamily = MarcellusFamily, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = MarcellusFamily, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(fontFamily = MarcellusFamily, fontSize = 24.sp, lineHeight = 32.sp),
    titleLarge = TextStyle(fontFamily = MarcellusFamily, fontSize = 22.sp, lineHeight = 28.sp),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
)
