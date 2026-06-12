package com.example.issac.ui.main

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.SingletonImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.issac.R
import com.example.issac.domain.model.Horoscope
import com.example.issac.domain.model.Zodiac
import com.example.issac.ui.main.components.AgeCard
import com.example.issac.ui.main.components.HoroscopeCard
import com.example.issac.ui.main.components.ZodiacBadge
import com.example.issac.ui.theme.GlassDark
import com.example.issac.ui.theme.IssacTheme
import com.example.issac.ui.theme.NightGradientBottom
import com.example.issac.ui.theme.NightGradientTop
import com.example.issac.ui.theme.OnGlass
import com.example.issac.ui.theme.StarGold
import java.time.LocalDate
import java.time.Period

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    MainScreenContent(
        modifier = modifier,
        contentPadding = contentPadding,
        uiState = uiState,
        showDatePicker = showDatePicker,
        onDateSelected = viewModel::onBirthDateSelected,
        onShowDatePicker = { showDatePicker = it },
        onRefreshBackground = viewModel::refreshBackground,
        onBackgroundLoaded = viewModel::onBackgroundImageLoaded,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    uiState: MainUiState,
    showDatePicker: Boolean,
    onDateSelected: (Long?) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
    onRefreshBackground: () -> Unit,
    onBackgroundLoaded: () -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    Box(modifier = modifier.fillMaxSize()) {
        // 1. Always-present night-sky gradient — the offline / loading fallback.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(NightGradientTop, NightGradientBottom))),
        )

        // 2. The NASA space photo fetched from the API, layered over the gradient.
        val backgroundUrl = uiState.backgroundImageUrl
        if (backgroundUrl != null) {
            // Remember the previous photo's URL: it is still in Coil's memory
            // cache, so the new request can keep showing it as the placeholder
            // while the next photo downloads, instead of dropping back to the
            // gradient for the whole download.
            var previousUrl by remember { mutableStateOf<String?>(null) }
            val placeholderKey = remember(backgroundUrl) {
                previousUrl.also { previousUrl = backgroundUrl }
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(backgroundUrl)
                    .placeholderMemoryCacheKey(placeholderKey)
                    // Each new photo fades in over the previous one instead of popping.
                    .crossfade(600)
                    .build(),
                contentDescription = null,
                onSuccess = { onBackgroundLoaded() },
                onError = { onBackgroundLoaded() },
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        // Quietly download the next queued photo into Coil's cache, so the next
        // shuffle swaps instantly instead of waiting on NASA's image servers.
        val nextUrl = uiState.nextBackgroundImageUrl
        val platformContext = LocalPlatformContext.current
        LaunchedEffect(nextUrl) {
            if (nextUrl != null) {
                SingletonImageLoader.get(platformContext).enqueue(
                    ImageRequest.Builder(platformContext).data(nextUrl).build(),
                )
            }
        }

        // 3. Scrim so the title and card stay legible over any photo.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f)),
        )

        // 4. Protection gradient: the photo fades into night indigo behind the
        //    glass bottom bar. Without it, a bright photo burns straight
        //    through the bar's 45% veil and the "glass" looks like it only
        //    covers the dark parts of the picture.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, NightGradientBottom),
                    ),
                ),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                // The photo behind us extends under the status bar and the
                // glass bottom bar; the content itself stays below the clock
                // (status bar inset) and above the bar (contentPadding).
                .padding(contentPadding)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(R.string.app_name),
                color = StarGold,
                // The theme's Marcellus display style; a soft dark shadow lifts
                // the title cleanly off the photo so it stays vivid in both themes.
                style = MaterialTheme.typography.displaySmall.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.7f),
                        offset = Offset(0f, 3f),
                        blurRadius = 12f,
                    ),
                ),
                modifier = Modifier.padding(bottom = 32.dp),
            )

            Button(
                onClick = { onShowDatePicker(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Text(
                    text = stringResource(
                        if (uiState.birthDate == null) R.string.select_birth_date
                        else R.string.change_date,
                    ),
                )
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { onShowDatePicker(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            onDateSelected(datePickerState.selectedDateMillis)
                            onShowDatePicker(false)
                        }) {
                            Text(stringResource(R.string.action_ok))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onShowDatePicker(false) }) {
                            Text(stringResource(R.string.action_cancel))
                        }
                    },
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val birthDate = uiState.birthDate
            val zodiac = uiState.zodiac
            val age = uiState.age
            // The card fades in and rises when the first date is picked, then
            // animateContentSize grows it smoothly as the horoscope arrives or
            // the reading length changes.
            AnimatedVisibility(
                visible = birthDate != null && zodiac != null && age != null,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 3 }),
            ) {
                if (birthDate != null && zodiac != null && age != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        colors = CardDefaults.cardColors(
                            // A faint dark "glass" over the photo. Fixed colours
                            // (like StarGold) rather than theme roles, because the
                            // card always sits on the photo: the photo shows
                            // through the veil, and the light text stays readable
                            // in both themes thanks to the scrim underneath.
                            containerColor = GlassDark,
                            contentColor = OnGlass,
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = stringResource(R.string.destiny_title),
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(bottom = 12.dp),
                            )
                            Text(text = stringResource(R.string.birth_date_label, birthDate), fontSize = 16.sp)
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = OnGlass.copy(alpha = 0.3f),
                            )
                            AgeCard(age = age)
                            Spacer(modifier = Modifier.height(16.dp))
                            // Gold glyph (not theme primary): indigo would vanish
                            // against the dark glass in light theme.
                            ZodiacBadge(zodiac = zodiac, color = StarGold)

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = OnGlass.copy(alpha = 0.3f),
                            )
                            val context = LocalContext.current
                            HoroscopeCard(
                                isLoading = uiState.isLoading,
                                horoscope = uiState.horoscope,
                                isError = uiState.isError,
                                readingLength = uiState.readingLength,
                                onShare = {
                                    // Hand the reading to any app that accepts
                                    // plain text via the system share sheet.
                                    uiState.horoscope?.let { horoscope ->
                                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                context.getString(
                                                    R.string.share_reading_text,
                                                    "${zodiac.symbol} ${zodiac.displayName}",
                                                    horoscope.date.toString(),
                                                    uiState.readingLength.format(horoscope.text),
                                                ),
                                            )
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, null))
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }

        // 5. Shuffle button — pulls a different photo from NASA on demand.
        IconButton(
            onClick = onRefreshBackground,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(8.dp),
        ) {
            // Crossfade swaps the refresh icon and the spinner smoothly.
            Crossfade(targetState = uiState.isBackgroundLoading, label = "shuffle") { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = stringResource(R.string.shuffle_background),
                        tint = Color.White,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val sampleBirthDate = LocalDate.of(1990, 8, 10)
    IssacTheme {
        MainScreenContent(
            uiState = MainUiState(
                birthDate = sampleBirthDate,
                zodiac = Zodiac.LEO,
                age = Period.between(sampleBirthDate, LocalDate.now()),
                horoscope = Horoscope(
                    date = LocalDate.now(),
                    text = "A calm, productive day — tackle the small tasks first.",
                ),
            ),
            showDatePicker = false,
            onDateSelected = {},
            onShowDatePicker = {},
            onRefreshBackground = {},
            onBackgroundLoaded = {},
        )
    }
}
