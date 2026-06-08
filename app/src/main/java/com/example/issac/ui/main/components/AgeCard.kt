package com.example.issac.ui.main.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.issac.R
import com.example.issac.ui.theme.IssacTheme
import java.time.Period

/**
 * Renders an age as a single centred line, e.g.
 * "Age: 28 years, 5 months, 3 days". Stateless and self-contained so it can be
 * reused and previewed in isolation.
 */
@Composable
fun AgeCard(
    age: Period,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.age_format, age.years, age.months, age.days),
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun AgeCardPreview() {
    IssacTheme {
        AgeCard(age = Period.of(28, 5, 3))
    }
}
