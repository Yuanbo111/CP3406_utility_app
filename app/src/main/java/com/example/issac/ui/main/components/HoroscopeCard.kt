package com.example.issac.ui.main.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import com.example.issac.ui.theme.GlassError
import com.example.issac.ui.theme.IssacTheme
import java.time.LocalDate

/**
 * Shows today's reading under a small caption, switching between three states:
 * a spinner while [isLoading], an error message when [isError] is true, or the
 * [horoscope] text once loaded. [readingLength] controls how much of the text
 * is shown, and [onShare] fires from the share icon that accompanies a loaded
 * reading. Stateless and previewable in every state.
 */
@Composable
fun HoroscopeCard(
    isLoading: Boolean,
    horoscope: Horoscope?,
    isError: Boolean,
    readingLength: ReadingLength,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Inherits the parent surface's content colour (the glass card sets a
        // light one), slightly faded to read as a caption.
        Text(
            text = stringResource(R.string.todays_reading),
            fontSize = 14.sp,
            color = LocalContentColor.current.copy(alpha = 0.75f),
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
                ReadingState.LOADING -> CircularProgressIndicator(
                    modifier = Modifier.padding(8.dp),
                    color = LocalContentColor.current,
                )

                ReadingState.ERROR -> Text(
                    text = stringResource(R.string.horoscope_error),
                    fontSize = 14.sp,
                    // Fixed soft red: the theme's error role is too dark to read
                    // on the dark glass card in light theme.
                    color = GlassError,
                    textAlign = TextAlign.Center,
                )

                ReadingState.LOADED -> if (horoscope != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = readingLength.format(horoscope.text),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                        )
                        // Only a real reading is shareable, so the icon lives
                        // inside the loaded state and animates in with it.
                        IconButton(onClick = onShare) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = stringResource(R.string.share_reading),
                                tint = LocalContentColor.current.copy(alpha = 0.75f),
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
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
            onShare = {},
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
            onShare = {},
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
            onShare = {},
        )
    }
}
