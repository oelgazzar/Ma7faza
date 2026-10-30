package com.example.ma7faza.ui.expenses.form

import com.example.ma7faza.domain.models.Expense

sealed interface ExpenseFormValidationResult {
    data class Success(val expense: Expense): ExpenseFormValidationResult
    data class Error(val causes: List<ExpenseFormValidationError>): ExpenseFormValidationResult
}

enum class ExpenseFormValidationError(val field: String) {
    EmptyName("name"),
    EmptyAmount("amount"),
    InvalidAmount("amount"),
    ZeroAmount("amount"),
}