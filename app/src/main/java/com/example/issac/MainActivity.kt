package com.example.issac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.issac.ui.theme.IssacTheme
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneId

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IssacTheme {
                AgeConstellationApp()
            }
        }
    }
}

fun getZodiacSign(month: Int, day: Int): String {
    return when (month) {
        1 -> if (day < 20) "Capricorn" else "Aquarius"
        2 -> if (day < 19) "Aquarius" else "Pisces"
        3 -> if (day < 21) "Pisces" else "Aries"
        4 -> if (day < 20) "Aries" else "Taurus"
        5 -> if (day < 21) "Taurus" else "Gemini"
        6 -> if (day < 21) "Gemini" else "Cancer"
        7 -> if (day < 23) "Cancer" else "Leo"
        8 -> if (day < 23) "Leo" else "Virgo"
        9 -> if (day < 23) "Virgo" else "Libra"
        10 -> if (day < 23) "Libra" else "Scorpio"
        11 -> if (day < 22) "Scorpio" else "Sagittarius"
        12 -> if (day < 22) "Sagittarius" else "Capricorn"
        else -> "Unknown"
    }
}

@Preview(showBackground = true)
@Composable
fun AgeConstellationPreview() {
    IssacTheme {
        AgeConstellationAppContent(
            selectedDate = 648432000000L, // June 18, 1990
            showDatePicker = false,
            onDateChange = {},
            onShowDatePicker = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeConstellationApp(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        AgeConstellationAppContent(
            modifier = modifier.padding(innerPadding),
            selectedDate = selectedDate,
            showDatePicker = showDatePicker,
            onDateChange = { selectedDate = it },
            onShowDatePicker = { showDatePicker = it }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeConstellationAppContent(
    modifier: Modifier = Modifier,
    selectedDate: Long?,
    showDatePicker: Boolean,
    onDateChange: (Long?) -> Unit,
    onShowDatePicker: (Boolean) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Use the photo provided by the user
        Image(
            painter = painterResource(id = R.drawable.background_zodiac),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay to ensure text readability if the photo is light
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
        )

        Column(
            modifier = modifier
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
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
                val birthDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                val today = LocalDate.now()
                val period = Period.between(birthDate, today)

                val zodiac = getZodiacSign(birthDate.monthValue, birthDate.dayOfMonth)

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
                        Text(text = "Birth Date: ${birthDate}", fontSize = 16.sp)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color.Gray.copy(alpha = 0.5f))
                        Text(
                            text = "Age: ${period.years} years, ${period.months} months, ${period.days} days",
                            fontSize = 18.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Zodiac Sign",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = zodiac,
                            fontSize = 32.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
