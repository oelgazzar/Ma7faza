package com.example.ma7faza.ui.transactions.form

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.utils.parseAmount
import java.time.LocalDate

fun validateTransactionForm(
    id: Long,
    name: String,
    amount: String,
    note: String?,
    date: LocalDate
): TransactionFormValidationResult {
    val causes = mutableListOf<TransactionFormValidationError>()
    val parsedAmount = parseAmount(amount)

    if (name.isBlank()) {
        causes.add(TransactionFormValidationError.EmptyName)
    }

    if (amount.isBlank()) {
        causes.add(TransactionFormValidationError.EmptyAmount)
    } else if (parsedAmount == 0L) {
        causes.add(TransactionFormValidationError.ZeroAmount)
    }
    else if (parsedAmount == null) {
        causes.add(TransactionFormValidationError.InvalidAmount)
    }

    if (causes.isNotEmpty() || parsedAmount == null) {
        return TransactionFormValidationResult.Error(
            causes = causes
        )
    }

    return TransactionFormValidationResult.Success(
        Transaction(
            id = id,
            name = name.trim(),
            amountMinor = parsedAmount,
            note = note?.trim()?.takeIf { it.isNotBlank() },
            date = date,
            type = TransactionType.EXPENSE
        )
    )
}