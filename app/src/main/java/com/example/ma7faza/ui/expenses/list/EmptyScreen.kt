package com.example.ma7faza.ui.expenses.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ma7faza.ui.theme.AppTheme
import com.example.test.receipt_long

@Composable
fun EmptyScreen(
    onAddExpense: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize()
    ) {
        Icon(
            receipt_long,
            null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(128.dp)
        )
        Text(
            "No expenses yet",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            "Start tracking your spending by adding your first expense.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(max = 320.dp)
        )
        TextButton(onClick = onAddExpense) {
            Text("Add expense")
        }
    }
}

@Preview
@Composable
private fun EmptyScreenPrev() {
    AppTheme {
        EmptyScreen(
            onAddExpense = {}
        )
    }
}