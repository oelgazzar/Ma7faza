package com.example.ma7faza.ui.expenses.list

import com.example.ma7faza.domain.models.Expense

sealed interface ExpenseListUiState {
    data object Loading: ExpenseListUiState
    data class Success(val expenses: List<Expense>): ExpenseListUiState
    data class Error(val message: String? = null): ExpenseListUiState
}