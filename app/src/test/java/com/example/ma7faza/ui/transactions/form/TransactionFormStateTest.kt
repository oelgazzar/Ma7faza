package com.example.ma7faza.ui.transactions.form

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class TransactionFormStateTest {
    private val date = LocalDate.of(2026, 1, 15)

    @Test
    fun `errors are empty before validate`() {
        val form = TransactionFormState(date = date)
        assertEquals(emptyList<TransactionFormValidationError>(), form.errors)
    }

    @Test
    fun `validate on an empty form shows both errors`() {
        val form = TransactionFormState(date = date)
        assertNull(form.validate())
        assertEquals(
            listOf(TransactionFormValidationError.EmptyName, TransactionFormValidationError.EmptyAmount),
            form.errors
        )
    }

    @Test
    fun `fixing the amount clears only the amount error`() {
        val form = TransactionFormState(date = date)
        form.validate()
        form.amount.setTextAndPlaceCursorAtEnd("25.00")
        assertEquals(listOf(TransactionFormValidationError.EmptyName), form.errors)
    }

    @Test
    fun `editing an existing transaction round trips`() {
        val original =
            Transaction(
                id = 7,
                name = "Lunch",
                amountMinor = 1999,
                note = "work",
                date = date,
                type = TransactionType.EXPENSE
            )
        val form = TransactionFormState.fromTransaction(original)
        assertEquals(original, form.validate())
    }
}