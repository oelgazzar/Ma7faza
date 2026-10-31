package com.example.ma7faza.ui.transactions.form

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.utils.formatAmount
import java.time.LocalDate

@Stable
class TransactionFormState(
    val id: Long = 0,
    name: String = "",
    amount: String = "",
    note: String? = null,
    date: LocalDate = LocalDate.now(),
    type: TransactionType = TransactionType.EXPENSE,
    categoryId: String = defaultCategoryId(type)
) {
    val name = TextFieldState(name)
    val amount = TextFieldState(amount)
    val note = TextFieldState(note ?: "")
    var date: LocalDate by mutableStateOf(date)
        private set

    var type: TransactionType by mutableStateOf(type)
        private set

    var categoryId: String by mutableStateOf(categoryId)
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

    fun updateType(type: TransactionType) {
        if (this.type == type) return
        this.type = type
        categoryId = defaultCategoryId(type)
    }

    fun updateCategory(categoryId: String) {
        this.categoryId = categoryId
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
        date = date,
        type = type,
        categoryId = categoryId
    )

    companion object {
        fun fromTransaction(transaction: Transaction) = TransactionFormState(
            id = transaction.id,
            name = transaction.name,
            amount = formatAmount(transaction.amountMinor),
            note = transaction.note,
            date = transaction.date,
            type = transaction.type,
            categoryId = transaction.categoryId
        )
    }
}

private fun defaultCategoryId(type: TransactionType) = when (type) {
    TransactionType.EXPENSE -> "OTHER_EXPENSE"
    TransactionType.INCOME -> "OTHER_INCOME"
}
