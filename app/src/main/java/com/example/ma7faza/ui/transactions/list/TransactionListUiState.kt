package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Transaction

sealed interface TransactionListUiState {
    data object Loading: TransactionListUiState
    data class Success(val transactions: List<Transaction>): TransactionListUiState
    data class Error(val message: String? = null): TransactionListUiState
}