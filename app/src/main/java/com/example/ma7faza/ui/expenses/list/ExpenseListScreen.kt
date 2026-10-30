package com.example.ma7faza.ui.expenses.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.R
import com.example.ma7faza.data.sample.sampleExpenses
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.utils.toDisplayString
import kotlinx.serialization.Serializable
import java.time.LocalDate

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
            onAddExpense = onAddExpense,
            onEditExpense = onEditExpense,
            onDelete = viewModel::deleteExpense,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun ExpenseListContent(
    uiState: ExpenseListUiState,
    onAddExpense: () -> Unit,
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
                EmptyScreen(
                    onAddExpense = onAddExpense,
                    modifier
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    val expenseGroups = groupExpensesByDate(expenses = uiState.expenses)
                    expenseGroups.forEach { group ->
                        item(key = group.date) {
                            ExpenseGroupHeader(group.date)
                        }
                        items(group.expenses, key = { it.id }) { expense ->
                            ExpenseRow(
                                expense,
                                onEdit = onEditExpense,
                                onDelete,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseGroupHeader(date: LocalDate) {
    Text(date.toDisplayString())
}

@Preview
@Composable
private fun ExpenseListContentPreview() {
    AppTheme {
        ExpenseListContent(
            uiState = ExpenseListUiState.Success(sampleExpenses),
            onAddExpense = {},
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
            onAddExpense = {},
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
            onAddExpense = {},
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
            onAddExpense = {},
            onDelete = {},
            onEditExpense = {}
        )
    }
}