package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.test.search
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
    var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
    val searchQuery = rememberTextFieldState()

    LaunchedEffect(viewModel) {
        var job: Job? = null
        viewModel.events.collect { event ->
            when (event) {
                TransactionListUiEvent.OnDelete -> {
                    job?.cancel()
                    job = launch {
                        snackBarState.showSnackbar(
                            message = "Transaction deleted",
                            actionLabel = "Undo",
                            withDismissAction = true
                        ).run {
                            when (this) {
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
                    if (isSearchExpanded) {
                        TextField(
                            state = searchQuery,
                            placeholder = { Text(stringResource(R.string.search_transactions)) },
                            lineLimits = TextFieldLineLimits.SingleLine,
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                    } else {
                        Text(stringResource(R.string.transactions))
                    }
                },
                actions = {
                    if (isSearchExpanded) {
                        TextButton(
                            onClick = {
                                searchQuery.clearText()
                                isSearchExpanded = false
                            }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    } else {
                        IconButton(onClick = { isSearchExpanded = true }) {
                            Icon(
                                search,
                                null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
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
            uiState = uiState,
            searchQuery = searchQuery.text.toString(),
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
    searchQuery: String,
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
            val query = searchQuery.trim()
            val filteredTransactions = if (query.isBlank()) {
                uiState.transactions
            } else {
                uiState.transactions.filter { transaction ->
                    transaction.name.contains(query, ignoreCase = true) ||
                            transaction.note.orEmpty().contains(query, ignoreCase = true) ||
                            uiState.categories[transaction.categoryId]?.name
                                ?.contains(query, ignoreCase = true) == true
                }
            }

            if (filteredTransactions.isEmpty()) {
                if (query.isBlank()) {
                    EmptyScreen(onAddTransaction = onAddTransaction, modifier = modifier)
                } else {
                    Text(
                        text = "No matching transactions",
                        modifier = modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            } else {
                val transactionGroups = groupTransactionsByDate(transactions = filteredTransactions)

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    modifier = modifier.fillMaxSize()
                ) {
                    transactionGroups.forEach { group ->
                        item(key = group.date) {
                            TransactionGroupHeader(group.date)
                        }
                        items(group.transactions, key = { it.id }) { transaction ->
                            TransactionRow(
                                transaction = transaction,
                                category = uiState.categories[transaction.categoryId],
                                onEdit = onEditTransaction,
                                onDelete = onDelete,
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
            uiState = TransactionListUiState.Success(sampleTransactions, emptyMap()),
            searchQuery = "",
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
            uiState = TransactionListUiState.Success(emptyList(), emptyMap()),
            searchQuery = "",
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
            searchQuery = "",
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
            searchQuery = "",
            onAddTransaction = {},
            onDelete = {},
            onEditTransaction = {}
        )
    }
}
