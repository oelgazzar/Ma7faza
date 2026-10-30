package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.R
import com.example.ma7faza.data.sample.sampleTransactions
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.ui.theme.AppTheme
import com.example.test.add
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object TransactionList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        var job: Job? = null
        viewModel.events.collect { event ->
            when(event) {
                TransactionListUiEvent.OnDelete -> {
                    job?.cancel()
                    job = launch {
                        snackBarState.showSnackbar(
                            message = "Transaction deleted",
                            actionLabel = "Undo",
                            withDismissAction = true
                        ).run {
                            when(this) {
                                SnackbarResult.ActionPerformed -> viewModel.undoDelete()
                                SnackbarResult.Dismissed -> viewModel.onConfirmDelete()
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Transactions")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaction,
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(add, null)
            }
        },
        snackbarHost = {
            SnackbarHost(snackBarState)
        }
    ) { innerPadding ->
        TransactionListContent(
            uiState,
            onAddTransaction = onAddTransaction,
            onEditTransaction = onEditTransaction,
            onDelete = viewModel::deleteTransaction,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun TransactionListContent(
    uiState: TransactionListUiState,
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is TransactionListUiState.Loading -> Text(
            text = stringResource(R.string.loading), modifier = modifier
                .fillMaxSize()
                .wrapContentSize()
        )

        is TransactionListUiState.Error -> {
            val message = uiState.message
            Text(
                text = message ?: stringResource(R.string.unknown_error),
                color = MaterialTheme.colorScheme.error,
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }

        is TransactionListUiState.Success -> {
            if (uiState.transactions.isEmpty()) {
                EmptyScreen(
                    onAddTransaction = onAddTransaction,
                    modifier
                )
            } else {
                val transactionGroups = groupTransactionsByDate(transactions = uiState.transactions)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    transactionGroups.forEach { group ->
                        item(key = group.date) {
                            TransactionGroupHeader(group.date)
                        }
                        items(group.transactions, key = { it.id }) { transaction ->
                            TransactionRow(
                                transaction,
                                onEdit = onEditTransaction,
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

@Preview
@Composable
private fun TransactionListContentPreview() {
    AppTheme {
        TransactionListContent(
            uiState = TransactionListUiState.Success(sampleTransactions),
            onAddTransaction = {},
            onEditTransaction = {},
            onDelete = {}
        )
    }
}

@Preview
@Composable
private fun EmptyTransactionListContentPreview() {
    AppTheme {
        TransactionListContent(
            uiState = TransactionListUiState.Success(listOf()),
            onAddTransaction = {},
            onDelete = {},
            onEditTransaction = {}
        )
    }
}

@Preview
@Composable
private fun ErrorTransactionListContentPreview() {
    AppTheme {
        TransactionListContent(
            uiState = TransactionListUiState.Error("Something went wrong!!"),
            onAddTransaction = {},
            onDelete = {},
            onEditTransaction = {}
        )
    }
}

@Preview
@Composable
private fun LoadingTransactionListContentPreview() {
    AppTheme {
        TransactionListContent(
            uiState = TransactionListUiState.Loading,
            onAddTransaction = {},
            onDelete = {},
            onEditTransaction = {}
        )
    }
}