package com.example.ma7faza.ui.transactions.form

import com.example.ma7faza.domain.models.Transaction

sealed interface TransactionFormValidationResult {
    data class Success(val transaction: Transaction): TransactionFormValidationResult
    data class Error(val causes: List<TransactionFormValidationError>): TransactionFormValidationResult
}

enum class TransactionFormValidationError(val field: String) {
    EmptyName("name"),
    EmptyAmount("amount"),
    InvalidAmount("amount"),
    ZeroAmount("amount"),
}

fun TransactionFormValidationError.toMessage(): String =
    when (this) {
        TransactionFormValidationError.EmptyName ->
            "Title is required"

        TransactionFormValidationError.EmptyAmount ->
            "Amount is required"

        TransactionFormValidationError.InvalidAmount ->
            "Enter a valid amount"

        TransactionFormValidationError.ZeroAmount ->
            "Amount must be greater than zero"
    }