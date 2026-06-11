package com.example.issac.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.issac.R
import com.example.issac.data.settings.ReadingLength
import com.example.issac.data.settings.ThemeMode
import com.example.issac.ui.theme.IssacTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val readingLength by viewModel.readingLength.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    SettingsScreenContent(
        modifier = modifier,
        readingLength = readingLength,
        onReadingLengthChange = viewModel::onReadingLengthChange,
        themeMode = themeMode,
        onThemeModeChange = viewModel::onThemeModeChange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
    modifier: Modifier = Modifier,
    readingLength: ReadingLength,
    onReadingLengthChange: (ReadingLength) -> Unit,
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            // The window is edge-to-edge, so keep the content below the
            // status bar's clock and icons.
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineSmall)

        Text(
            text = stringResource(R.string.reading_length_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp),
        )
        Text(
            text = stringResource(R.string.reading_length_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            ReadingLength.entries.forEachIndexed { index, length ->
                SegmentedButton(
                    selected = length == readingLength,
                    onClick = { onReadingLengthChange(length) },
                    shape = SegmentedButtonDefaults.itemShape(index, ReadingLength.entries.size),
                ) {
                    Text(stringResource(length.labelRes))
                }
            }
        }

        Text(
            text = stringResource(R.string.theme_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp),
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            ThemeMode.entries.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = mode == themeMode,
                    onClick = { onThemeModeChange(mode) },
                    shape = SegmentedButtonDefaults.itemShape(index, ThemeMode.entries.size),
                ) {
                    Text(stringResource(mode.labelRes))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    IssacTheme {
        SettingsScreenContent(
            readingLength = ReadingLength.FULL,
            onReadingLengthChange = {},
            themeMode = ThemeMode.SYSTEM,
            onThemeModeChange = {},
        )
    }
}
