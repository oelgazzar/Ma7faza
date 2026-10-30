package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ma7faza.data.sample.sampleTransactions
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.ui.theme.icons.delete
import com.example.ma7faza.utils.formatCurrency

@Composable
fun TransactionRow(
    transaction: Transaction,
    onEdit: (Long) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onEdit(transaction.id) },
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
                Text(
                    text = transaction.name,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = formatCurrency(transaction.amountMinor),
                    style = MaterialTheme.typography.titleMedium
                )

            IconButton(
                onClick = { onDelete(transaction) }
            ) {
                Icon(delete, null)
            }
            }
    }
}

@Preview
@Composable
private fun TransactionRowPrev() {
    AppTheme {
        TransactionRow(
            sampleTransactions.first(),
            onEdit = {},
            onDelete = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}