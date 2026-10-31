package com.example.ma7faza.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.R
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
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            is DashboardUiState.Error -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
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
                stringResource(R.string.app_name),
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

