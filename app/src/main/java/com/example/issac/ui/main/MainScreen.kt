package com.example.issac.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.issac.R
import com.example.issac.domain.model.Horoscope
import com.example.issac.domain.model.Zodiac
import com.example.issac.ui.main.components.AgeCard
import com.example.issac.ui.main.components.ZodiacBadge
import com.example.issac.ui.theme.IssacTheme
import java.time.LocalDate
import java.time.Period

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }

    MainScreenContent(
        modifier = modifier,
        uiState = uiState,
        showDatePicker = showDatePicker,
        onDateSelected = viewModel::onBirthDateSelected,
        onShowDatePicker = { showDatePicker = it },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    uiState: MainUiState,
    showDatePicker: Boolean,
    onDateSelected: (Long?) -> Unit,
    onShowDatePicker: (Boolean) -> Unit,
) {
    val datePickerState = rememberDatePickerState()

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_zodiac),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Age & Zodiac Calculator",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp),
            )

            Button(
                onClick = { onShowDatePicker(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Text(text = if (uiState.birthDate == null) "Select Birth Date" else "Change Date")
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { onShowDatePicker(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            onDateSelected(datePickerState.selectedDateMillis)
                            onShowDatePicker(false)
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onShowDatePicker(false) }) {
                            Text("Cancel")
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
            if (birthDate != null && zodiac != null && age != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f),
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Your Destiny",
                            fontSize = 22.sp,
                            color = Color(0xFF1B263B),
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(text = "Birth Date: $birthDate", fontSize = 16.sp)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.5f),
                        )
                        AgeCard(age = age)
                        Spacer(modifier = Modifier.height(16.dp))
                        ZodiacBadge(zodiac = zodiac)

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.5f),
                        )
                        Text(text = "Today's Reading", fontSize = 14.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))

                        val horoscope = uiState.horoscope
                        val error = uiState.error
                        when {
                            uiState.isLoading -> CircularProgressIndicator(
                                modifier = Modifier.padding(8.dp),
                            )

                            error != null -> Text(
                                text = error,
                                fontSize = 14.sp,
                                color = Color(0xFFB00020),
                                textAlign = TextAlign.Center,
                            )

                            horoscope != null -> Text(
                                text = horoscope.text,
                                fontSize = 15.sp,
                                color = Color(0xFF1B263B),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
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
        )
    }
}
