package com.example.ma7faza.ui.expenses.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ma7faza.R
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.ui.theme.icons.receipt_long

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
            stringResource(R.string.no_expenses_yet),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            stringResource(R.string.start_tracking_your_spending_by_adding_your_first_expense),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .widthIn(max = 320.dp)
        )
        Button(onClick = onAddExpense) {
            Text(stringResource(R.string.add_expense))
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