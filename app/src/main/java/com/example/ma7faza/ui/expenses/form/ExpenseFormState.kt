package com.example.ma7faza.ui.expenses.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.utils.formatAmount
import java.time.LocalDate

@Stable
class ExpenseFormState(
    val id: Long = 0,
    name: String = "",
    amount: String = "",
    note: String? = null,
    date: LocalDate = LocalDate.now(),
) {
    val name = TextFieldState(name)
    val amount = TextFieldState(amount)
    val note = TextFieldState(note ?: "")
    var date: LocalDate by mutableStateOf(date)
        private set
    private var submitted by mutableStateOf(false)

    val errors: List<ExpenseFormValidationError>
        get() = if (submitted) {
            (runValidation() as? ExpenseFormValidationResult.Error)?.causes.orEmpty()
        } else {
            emptyList()
        }

    fun updateDate(date: LocalDate) {
        this.date = date
    }

    fun validate(): Expense? {
        submitted = true
        return (runValidation() as? ExpenseFormValidationResult.Success)?.expense
    }

    private fun runValidation() = validateExpenseForm(
        id = id,
        name = name.text.toString(),
        amount = amount.text.toString(),
        note = note.text.toString(),
        date = date
    )

    companion object {
        fun fromExpense(expense: Expense) = ExpenseFormState(
            id = expense.id,
            name = expense.name,
            amount = formatAmount(expense.amountMinor),
            note = expense.note,
            date = expense.date,
        )
    }
}