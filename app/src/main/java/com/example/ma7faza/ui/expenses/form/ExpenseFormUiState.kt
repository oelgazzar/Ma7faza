package com.example.ma7faza.ui.expenses.form

sealed interface ExpenseFormUiState {
    data object Loading : ExpenseFormUiState
    data class Success(val formState: ExpenseFormState) : ExpenseFormUiState
    data class Error(val cause: ExpenseFormError) : ExpenseFormUiState
}

enum class ExpenseFormError {
    ExpenseNotFound
}