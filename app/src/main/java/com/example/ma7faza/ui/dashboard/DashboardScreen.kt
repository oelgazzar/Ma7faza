package com.example.ma7faza.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.CategoryExpense
import com.example.ma7faza.ui.categories.color
import com.example.ma7faza.ui.categories.displayName
import com.example.ma7faza.ui.transactions.components.TransactionItem
import com.example.ma7faza.utils.formatCurrency
import com.example.test.add

@Composable
fun DashboardScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onSeeAllTransactions: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = onAddTransaction) { Icon(add, contentDescription = null) }
    }) { padding ->
        when (val current = state) {
            DashboardUiState.Loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            is DashboardUiState.Error -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    current.message ?: "Something went wrong",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is DashboardUiState.Success -> DashboardContent(
                current,
                viewModel::selectPeriod,
                onEditTransaction,
                onSeeAllTransactions,
                Modifier.padding(padding)
            )
        }
    }
}

@Composable
private fun DashboardContent(
    state: DashboardUiState.Success,
    onPeriodSelected: (SummaryPeriod) -> Unit,
    onEdit: (Long) -> Unit,
    onSeeAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                "Ma7faza",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Total balance", style = MaterialTheme.typography.titleMedium)
                    Text(
                        formatCurrency(state.balanceMinor),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Across all transactions", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Financial overview", style = MaterialTheme.typography.titleLarge)
                SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                    SummaryPeriod.entries.forEachIndexed { index, period ->
                        SegmentedButton(
                            selected = state.selectedPeriod == period,
                            onClick = { onPeriodSelected(period) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index,
                                SummaryPeriod.entries.size
                            )
                        ) {
                            Text(
                                when (period) {
                                    SummaryPeriod.THIS_WEEK -> "Week"
                                    SummaryPeriod.THIS_MONTH -> "Month"
                                    SummaryPeriod.LAST_MONTH -> "Last"
                                    SummaryPeriod.ALL_TIME -> "All"
                                },
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SummaryCard("Income", state.summary.incomeMinor, Modifier.weight(1f))
                    SummaryCard("Expenses", state.summary.expensesMinor, Modifier.weight(1f))
                }
            }
        }
        item {
            CategoryExpenseSection(
                expenses = state.categoryExpenses,
                categories = state.categories,
                totalExpensesMinor = state.summary.expensesMinor
            )
        }
        item {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent transactions", style = MaterialTheme.typography.titleLarge)
                TextButton(onClick = onSeeAll) { Text("See all") }
            }
        }
        if (state.recentTransactions.isEmpty()) {
            item {
                Text("No transactions yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            items(state.recentTransactions, key = { it.id }) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    category = state.categories[transaction.categoryId],
                    date = transaction.date,
                    onClick = { onEdit(transaction.id) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CategoryExpenseSection(
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

@Composable
private fun SummaryCard(
    title: String,
    amountMinor: Long,
    modifier: Modifier = Modifier
) {
    Card(
        modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                formatCurrency(amountMinor),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
