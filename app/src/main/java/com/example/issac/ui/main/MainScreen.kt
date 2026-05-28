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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.issac.R
import com.example.issac.domain.usecase.DetermineZodiacUseCase
import com.example.issac.ui.theme.IssacTheme
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    MainScreenContent(
        modifier = modifier,
        selectedDate = selectedDate,
        showDatePicker = showDatePicker,
        onDateChange = { selectedDate = it },
        onShowDatePicker = { showDatePicker = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenContent(
    modifier: Modifier = Modifier,
    selectedDate: Long?,
    showDatePicker: Boolean,
    onDateChange: (Long?) -> Unit,
    onShowDatePicker: (Boolean) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val determineZodiac = remember { DetermineZodiacUseCase() }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.background_zodiac),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Age & Zodiac Calculator",
                fontSize = 28.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = { onShowDatePicker(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Text(text = if (selectedDate == null) "Select Birth Date" else "Change Date")
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { onShowDatePicker(false) },
                    confirmButton = {
                        TextButton(onClick = {
                            onDateChange(datePickerState.selectedDateMillis)
                            onShowDatePicker(false)
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { onShowDatePicker(false) }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            selectedDate?.let { millis ->
                val birthDate = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                val today = LocalDate.now()
                val period = Period.between(birthDate, today)
                val zodiac = determineZodiac(birthDate)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your Destiny",
                            fontSize = 22.sp,
                            color = Color(0xFF1B263B),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Text(text = "Birth Date: $birthDate", fontSize = 16.sp)
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.Gray.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "Age: ${period.years} years, ${period.months} months, ${period.days} days",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Zodiac Sign", fontSize = 14.sp, color = Color.Gray)
                        Text(
                            text = "${zodiac.symbol} ${zodiac.displayName}",
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    IssacTheme {
        MainScreenContent(
            selectedDate = 648432000000L,
            showDatePicker = false,
            onDateChange = {},
            onShowDatePicker = {}
        )
    }
}
