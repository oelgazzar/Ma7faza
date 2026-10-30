package com.example.ma7faza.ui.expenses.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.R
import com.example.ma7faza.data.sample.sampleExpenses
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.ui.theme.AppTheme
import kotlinx.serialization.Serializable

@Serializable
data object ExpenseList

@Composable
fun ExpenseListScreen(
    onAddExpense: () -> Unit,
    onEditExpense: (Long) -> Unit,
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddExpense,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Add")
            }
        }
    ) { innerPadding ->
        ExpenseListContent(
            uiState,
            onEditExpense = onEditExpense,
            onDelete = viewModel::deleteExpense,
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ExpenseListContent(
    uiState: ExpenseListUiState,
    onEditExpense: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is ExpenseListUiState.Loading -> Text(
            text = stringResource(R.string.loading), modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
        )

        is ExpenseListUiState.Error -> {
            val message = uiState.message
            Text(
                text = message ?: stringResource(R.string.unknown_error),
                color = MaterialTheme.colorScheme.error,
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }

        is ExpenseListUiState.Success -> {
            if (uiState.expenses.isEmpty()) {
                Text(
                    "No expenses", modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    items(uiState.expenses, key = { it.id }) { expense ->
                        ExpenseRow(
                            expense, onDelete,
                            modifier = Modifier.fillMaxWidth()
                                .clickable(onClick = { onEditExpense(expense.id) })
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(
    expense: Expense,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = expense.name,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
            )
            TextButton(onClick = { onDelete(expense.id) }) {
                Text(text = stringResource(R.string.delete))
            }
        }
    }
}

@Preview
@Composable
private fun ExpenseListContentPreview() {
    AppTheme {
        ExpenseListContent(
            uiState = ExpenseListUiState.Success(sampleExpenses),
            onEditExpense = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun EmptyExpenseListContentPreview() {
    AppTheme {
        ExpenseListContent(
            uiState = ExpenseListUiState.Success(listOf()),
            onDelete = {},
            onEditExpense = {}
        )
    }
}

@Preview
@Composable
private fun ErrorExpenseListContentPreview() {
    AppTheme {
        ExpenseListContent(
            uiState = ExpenseListUiState.Error("Something went wrong!!"),
            onDelete = {},
            onEditExpense = {}
        )
    }
}

@Preview
@Composable
private fun LoadingExpenseListContentPreview() {
    AppTheme {
        ExpenseListContent(
            uiState = ExpenseListUiState.Loading,
            onDelete = {},
            onEditExpense = {}
        )
    }
}