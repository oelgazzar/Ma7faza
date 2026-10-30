package com.example.ma7faza.ui.transactions.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.utils.formatAmount
import java.time.LocalDate

@Stable
class TransactionFormState(
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

    val errors: List<TransactionFormValidationError>
        get() = if (submitted) {
            (runValidation() as? TransactionFormValidationResult.Error)?.causes.orEmpty()
        } else {
            emptyList()
        }

    fun updateDate(date: LocalDate) {
        this.date = date
    }

    fun validate(): Transaction? {
        submitted = true
        return (runValidation() as? TransactionFormValidationResult.Success)?.transaction
    }

    private fun runValidation() = validateTransactionForm(
        id = id,
        name = name.text.toString(),
        amount = amount.text.toString(),
        note = note.text.toString(),
        date = date
    )

    companion object {
        fun fromTransaction(transaction: Transaction) = TransactionFormState(
            id = transaction.id,
            name = transaction.name,
            amount = formatAmount(transaction.amountMinor),
            note = transaction.note,
            date = transaction.date,
        )
    }
}