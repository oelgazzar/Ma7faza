package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ma7faza.R
import com.example.ma7faza.data.sample.sampleTransactions
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.ui.theme.AppTheme
import com.example.ma7faza.ui.theme.icons.add
import com.example.ma7faza.ui.theme.icons.search
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneOffset

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
    val searchQuery = viewModel.searchQuery
    val filters by viewModel.filters.collectAsStateWithLifecycle()
    val snackBarState = remember { SnackbarHostState() }
    var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
    var isFilterSheetVisible by rememberSaveable { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val errorMessage = stringResource(R.string.transaction_deleted)

    LaunchedEffect(viewModel) {
        var job: Job? = null
        viewModel.events.collect { event ->
            when (event) {
                TransactionListUiEvent.OnDelete -> {
                    job?.cancel()
                    job = launch {
                        snackBarState.showSnackbar(
                            message = errorMessage,
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

    LaunchedEffect(isSearchExpanded) {
        if (isSearchExpanded) focusRequester.requestFocus()
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
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
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

                    TextButton(onClick = { isFilterSheetVisible = true }) {
                        Text(
                            if (filters.type == TransactionTypeFilter.ALL) {
                                stringResource(R.string.filter)
                            } else {
                                stringResource(R.string.filter_active)
                            }
                        )
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
        snackbarHost = { SnackbarHost(snackBarState) }
    ) { innerPadding ->
        TransactionListContent(
            uiState = uiState,
            searchQuery = searchQuery.text.toString(),
            filters = filters,
            onAddTransaction = onAddTransaction,
            onEditTransaction = onEditTransaction,
            onDelete = viewModel::deleteTransaction,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }

    if (isFilterSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isFilterSheetVisible = false }
        ) {
            TransactionFilterSheet(
                initialFilters = filters,
                categories = (uiState as? TransactionListUiState.Success)?.categories?.values?.toList().orEmpty(),
                onApply = { updatedFilters ->
                    viewModel.updateFilters(updatedFilters)
                    isFilterSheetVisible = false
                }
            )
        }
    }
}

@Composable
private fun TransactionFilterSheet(
    initialFilters: TransactionFilterState,
    categories: List<Category>,
    onApply: (TransactionFilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    var temporaryFilters by remember {
        mutableStateOf(initialFilters)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.filter_transactions),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.transaction_type),
            style = MaterialTheme.typography.titleMedium
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TransactionTypeFilter.entries.forEach { type ->
                FilterChip(
                    selected = temporaryFilters.type == type,
                    onClick = { temporaryFilters = temporaryFilters.copy(type = type) },
                    label = {
                        Text(when (type) {
                            TransactionTypeFilter.ALL -> stringResource(R.string.all_transactions)
                            TransactionTypeFilter.INCOME -> stringResource(R.string.income)
                            TransactionTypeFilter.EXPENSE -> stringResource(R.string.expense)
                        })
                    }
                )
            }
        }

        HorizontalDivider()
        Text(stringResource(R.string.category), style = MaterialTheme.typography.titleMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = temporaryFilters.categoryIds.isEmpty(),
                onClick = { temporaryFilters = temporaryFilters.copy(categoryIds = emptySet()) },
                label = { Text(stringResource(R.string.all_transactions)) }
            )
            categories.filter { category ->
                temporaryFilters.type == TransactionTypeFilter.ALL ||
                    (temporaryFilters.type == TransactionTypeFilter.INCOME && category.type == TransactionType.INCOME) ||
                    (temporaryFilters.type == TransactionTypeFilter.EXPENSE && category.type == TransactionType.EXPENSE)
            }.forEach { category ->
                FilterChip(
                    selected = category.id in temporaryFilters.categoryIds,
                    onClick = {
                        val selected = temporaryFilters.categoryIds.toMutableSet()
                        if (!selected.add(category.id)) selected.remove(category.id)
                        temporaryFilters = temporaryFilters.copy(categoryIds = selected)
                    },
                    label = { Text(category.name) }
                )
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = {
                    temporaryFilters = TransactionFilterState()
                }
            ) {
                Text(stringResource(R.string.reset))
            }
            Button(onClick = { onApply(temporaryFilters) }) {
                Text(stringResource(R.string.apply))
            }
        }
    }
}

@Composable
private fun TransactionListContent(
    uiState: TransactionListUiState,
    searchQuery: String,
    filters: TransactionFilterState,
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is TransactionListUiState.Loading -> Text(
            text = stringResource(R.string.loading),
            modifier = modifier.fillMaxSize().wrapContentSize()
        )

        is TransactionListUiState.Error -> {
            Text(
                text = uiState.message ?: stringResource(R.string.unknown_error),
                color = MaterialTheme.colorScheme.error,
                modifier = modifier.fillMaxSize().wrapContentSize()
            )
        }

        is TransactionListUiState.Success -> {
            val filteredTransactions = filterTransactions(
                transactions = uiState.transactions,
                query = searchQuery,
                filters = filters,
                categories = uiState.categories
            )
            val hasActiveFilter = filters.type != TransactionTypeFilter.ALL || filters.categoryIds.isNotEmpty() || filters.startDate != null || filters.endDate != null

            if (filteredTransactions.isEmpty()) {
                if (searchQuery.isBlank() && !hasActiveFilter) {
                    EmptyScreen(onAddTransaction = onAddTransaction, modifier = modifier)
                } else {
                    Text(
                        text = stringResource(R.string.no_matching_transactions),
                        modifier = modifier.fillMaxSize().wrapContentSize()
                    )
                }
            } else {
                val transactionGroups = groupTransactionsByDate(filteredTransactions)

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
            filters = TransactionFilterState(),
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
            filters = TransactionFilterState(),
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
            filters = TransactionFilterState(),
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
            filters = TransactionFilterState(),
            onAddTransaction = {},
            onDelete = {},
            onEditTransaction = {}
        )
    }
}
