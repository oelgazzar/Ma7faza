package com.example.ma7faza.ui.transactions.form

sealed interface TransactionFormUiState {
    data object Loading : TransactionFormUiState
    data class Success(val formState: TransactionFormState) : TransactionFormUiState
    data object Error : TransactionFormUiState
}
