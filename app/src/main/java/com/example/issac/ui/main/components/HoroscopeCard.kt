package com.example.issac.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.issac.data.settings.ReadingLength
import com.example.issac.domain.model.Horoscope
import com.example.issac.ui.theme.IssacTheme
import java.time.LocalDate

/**
 * Shows today's reading under a small caption, switching between three states:
 * a spinner while [isLoading], an error message when [error] is set, or the
 * [horoscope] text once loaded. [readingLength] controls how much of the text
 * is shown. Stateless and previewable in every state.
 */
@Composable
fun HoroscopeCard(
    isLoading: Boolean,
    horoscope: Horoscope?,
    error: String?,
    readingLength: ReadingLength,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Today's Reading", fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(8.dp))
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.padding(8.dp))

            error != null -> Text(
                text = error,
                fontSize = 14.sp,
                color = Color(0xFFB00020),
                textAlign = TextAlign.Center,
            )

            horoscope != null -> Text(
                text = readingLength.format(horoscope.text),
                fontSize = 15.sp,
                color = Color(0xFF1B263B),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true, name = "Loaded")
@Composable
private fun HoroscopeCardLoadedPreview() {
    IssacTheme {
        HoroscopeCard(
            isLoading = false,
            horoscope = Horoscope(
                date = LocalDate.now(),
                text = "A calm, productive day. Tackle the small tasks first.",
            ),
            error = null,
            readingLength = ReadingLength.FULL,
        )
    }
}

@Preview(showBackground = true, name = "Loading")
@Composable
private fun HoroscopeCardLoadingPreview() {
    IssacTheme {
        HoroscopeCard(
            isLoading = true,
            horoscope = null,
            error = null,
            readingLength = ReadingLength.FULL,
        )
    }
}

@Preview(showBackground = true, name = "Error")
@Composable
private fun HoroscopeCardErrorPreview() {
    IssacTheme {
        HoroscopeCard(
            isLoading = false,
            horoscope = null,
            error = "Couldn't load today's reading. Check your connection and try again.",
            readingLength = ReadingLength.FULL,
        )
    }
}
