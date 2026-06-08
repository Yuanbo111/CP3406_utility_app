package com.example.issac.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.issac.R
import com.example.issac.domain.model.Zodiac
import com.example.issac.ui.theme.IssacTheme

/**
 * Shows a single zodiac sign as a small caption above its glyph and name,
 * e.g. "Zodiac Sign" over "♌ Leo". Stateless and self-contained so it can be
 * reused on other screens and previewed in isolation.
 */
@Composable
fun ZodiacBadge(
    zodiac: Zodiac,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.zodiac_sign_caption),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "${zodiac.symbol} ${zodiac.displayName}",
            fontSize = 32.sp,
            color = color,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ZodiacBadgePreview() {
    IssacTheme {
        ZodiacBadge(zodiac = Zodiac.LEO)
    }
}
