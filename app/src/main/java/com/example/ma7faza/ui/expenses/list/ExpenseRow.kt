package com.example.ma7faza.ui.expenses.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ma7faza.data.sample.sampleExpenses
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.ui.theme.icons.delete
import com.example.ma7faza.utils.formatCurrency

@Composable
fun ExpenseRow(
    expense: Expense,
    onEdit: (Long) -> Unit,
    onDelete: (Expense) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onEdit(expense.id) },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
                Text(
                    text = expense.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatCurrency(expense.amountMinor),
                    style = MaterialTheme.typography.titleLarge
                )

            IconButton(
                onClick = { onDelete(expense) }
            ) {
                Icon(delete, null)
            }
            }
    }
}

@Preview
@Composable
private fun ExpenseRowPrev() {
    AppTheme {
        ExpenseRow(
            sampleExpenses.first(),
            onEdit = {},
            onDelete = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}