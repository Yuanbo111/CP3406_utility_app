package com.example.issac.ui.main.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.issac.R
import com.example.issac.data.settings.ReadingLength
import com.example.issac.domain.model.Horoscope
import com.example.issac.ui.theme.IssacTheme
import java.time.LocalDate

/**
 * Shows today's reading under a small caption, switching between three states:
 * a spinner while [isLoading], an error message when [isError] is true, or the
 * [horoscope] text once loaded. [readingLength] controls how much of the text
 * is shown. Stateless and previewable in every state.
 */
@Composable
fun HoroscopeCard(
    isLoading: Boolean,
    horoscope: Horoscope?,
    isError: Boolean,
    readingLength: ReadingLength,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.todays_reading),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        // AnimatedContent crossfades (and resizes) between the three states
        // instead of snapping, e.g. spinner smoothly becomes the reading.
        AnimatedContent(
            targetState = when {
                isLoading -> ReadingState.LOADING
                isError -> ReadingState.ERROR
                else -> ReadingState.LOADED
            },
            label = "reading state",
        ) { state ->
            when (state) {
                ReadingState.LOADING ->
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp))

                ReadingState.ERROR -> Text(
                    text = stringResource(R.string.horoscope_error),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )

                ReadingState.LOADED -> if (horoscope != null) {
                    Text(
                        text = readingLength.format(horoscope.text),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

/** Which of the card's three states is showing — the key AnimatedContent animates on. */
private enum class ReadingState { LOADING, ERROR, LOADED }

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
            isError = false,
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
            isError = false,
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
            isError = true,
            readingLength = ReadingLength.FULL,
        )
    }
}
