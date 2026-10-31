package com.example.ma7faza.ui.transactions.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.ui.transactions.components.TransactionItem

@Composable
fun TransactionRow(
    transaction: Transaction,
    category: Category? = null,
    onEdit: (Long) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    TransactionItem(
        transaction = transaction,
        category = category,
        onClick = { onEdit(transaction.id) },
        onDelete = { onDelete(transaction) },
        modifier = modifier
    )
}
