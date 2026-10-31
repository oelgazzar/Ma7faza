package com.example.ma7faza.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.CategoryExpense
import com.example.ma7faza.ui.categories.color
import com.example.ma7faza.ui.categories.displayName
import com.example.ma7faza.utils.formatCurrency

@Composable
fun CategoryExpenseSection(
    expenses: List<CategoryExpense>,
    categories: Map<String, Category>,
    totalExpensesMinor: Long,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Expenses by category", style = MaterialTheme.typography.titleLarge)

        if (expenses.isEmpty() || totalExpensesMinor <= 0L) {
            Text(
                "No expenses in this period",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            expenses.forEach { expense ->
                val category = categories[expense.categoryId]
                val fraction = (
                        expense.totalMinor.toFloat() / totalExpensesMinor
                        ).coerceIn(0f, 1f)

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            category?.displayName() ?: "Unknown category",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            formatCurrency(expense.totalMinor),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHighest,
                                CircleShape
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction)
                                .fillMaxHeight()
                                .background(
                                    category?.color() ?: MaterialTheme.colorScheme.primary,
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}