package com.example.ma7faza.ui.transactions.list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.CategoryRepository
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {
    val uiState: StateFlow<TransactionListUiState> = combine(
        transactionRepository.observeAll(),
        categoryRepository.observeByType(TransactionType.EXPENSE),
        categoryRepository.observeByType(TransactionType.INCOME)
    ) { transactions, expenseCategories, incomeCategories ->
        TransactionListUiState.Success(
            transactions = transactions,
            categories = (expenseCategories + incomeCategories).associateBy { it.id }
        ) as TransactionListUiState
    }
        .catch {
            emit(TransactionListUiState.Error(it.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionListUiState.Loading
        )

    val searchQuery = TextFieldState()

    private val _filters = MutableStateFlow(TransactionFilterState())
    val filters: StateFlow<TransactionFilterState> = _filters.asStateFlow()

    fun updateFilters(filters: TransactionFilterState) {
        _filters.value = filters
    }

    var deletedTransaction: Transaction? = null

    private val _events = Channel<TransactionListUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.delete(transaction.id)
            deletedTransaction = transaction
            _events.trySend(TransactionListUiEvent.OnDelete)
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            deletedTransaction?.let { transactionRepository.upsert(it) }
            deletedTransaction = null
        }
    }

    fun onConfirmDelete() {
        deletedTransaction = null
    }
}

sealed interface TransactionListUiEvent {
    data object OnDelete : TransactionListUiEvent
}
