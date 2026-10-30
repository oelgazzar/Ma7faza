package com.example.ma7faza.ui.expenses.form

import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.utils.parseAmount
import java.time.LocalDate

fun validateExpenseForm(
    id: Long,
    name: String,
    amount: String,
    note: String?,
    date: LocalDate
): ExpenseFormValidationResult {
    val causes = mutableListOf<ExpenseFormValidationError>()
    val parsedAmount = parseAmount(amount)

    if (name.isBlank()) {
        causes.add(ExpenseFormValidationError.EmptyName)
    }

    if (amount.isBlank()) {
        causes.add(ExpenseFormValidationError.EmptyAmount)
    }
    else if (parsedAmount == null) {
        causes.add(ExpenseFormValidationError.InvalidAmount)
    } else if (parsedAmount == 0L) {
        causes.add(ExpenseFormValidationError.ZeroAmount)
    }

    if (causes.isNotEmpty()) {
        return ExpenseFormValidationResult.Error(
            causes = causes
        )
    }

    return ExpenseFormValidationResult.Success(
        Expense(
            id = id,
            name = name.trim(),
            amountMinor = parsedAmount!!,
            note = note?.trim()?.takeIf { it.isNotBlank() },
            date = date
        )
    )
}
