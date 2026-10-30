package com.example.ma7faza.ui.transactions.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
): ViewModel() {
    val uiState: StateFlow<TransactionListUiState> = transactionRepository.observeAll()
        .map<List<Transaction>, TransactionListUiState> {
            TransactionListUiState.Success(it)
        }
        .catch {
            emit(TransactionListUiState.Error(it.message))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TransactionListUiState.Loading
        )

    var deletedTransaction: Transaction? = null

    private val _events = Channel<TransactionListUiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            transactionRepository.delete(transaction.id)
            println(deletedTransaction)
            deletedTransaction = transaction
            println(deletedTransaction)
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
    data object OnDelete: TransactionListUiEvent
}