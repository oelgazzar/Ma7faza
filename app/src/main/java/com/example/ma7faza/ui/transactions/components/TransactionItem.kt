package com.example.ma7faza.ui.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.ui.categories.color
import com.example.ma7faza.ui.categories.displayName
import com.example.ma7faza.utils.formatCurrency
import java.time.LocalDate

@Composable
fun TransactionItem(
    transaction: Transaction,
    category: Category?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    date: LocalDate? = null,
    onDelete: (() -> Unit)? = null
) {
    val amountColor = when (transaction.type) {
        TransactionType.INCOME -> MaterialTheme.colorScheme.primary
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
    }

    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.name,
                    style = MaterialTheme.typography.titleMedium
                )

                if (category != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(category.color(), CircleShape)
                        )
                        Spacer(Modifier.size(6.dp))
                        Text(
                            text = category.displayName(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                date?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = formatCurrency(transaction.amountMinor),
                style = MaterialTheme.typography.titleMedium,
                color = amountColor
            )

            onDelete?.let { delete ->
                IconButton(onClick = delete) {
                    Icon(com.example.ma7faza.ui.theme.icons.delete, contentDescription = null)
                }
            }
        }
    }
}
