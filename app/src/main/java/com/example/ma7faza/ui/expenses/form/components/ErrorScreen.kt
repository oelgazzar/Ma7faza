package com.example.ma7faza.ui.expenses.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ma7faza.ui.theme.AppTheme
import com.example.test.sentiment_very_dissatisfied

@Composable
fun ErrorScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            sentiment_very_dissatisfied,
            null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(64.dp)
        )
        Text("Failed to load data")
        TextButton(onClick = onBack) {
            Text("Go back")
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPrev() {
    AppTheme {
        ErrorScreen(
            onBack = {}
        )
    }
}