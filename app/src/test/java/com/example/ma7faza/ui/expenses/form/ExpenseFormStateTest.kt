package com.example.ma7faza.ui.expenses.form

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import com.example.ma7faza.domain.models.Expense
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ExpenseFormStateTest {
    private val date = LocalDate.of(2026, 1, 15)

    @Test
    fun `errors are empty before validate`() {
        val form = ExpenseFormState(date = date)
        assertEquals(emptyList<ExpenseFormValidationError>(), form.errors)
    }

    @Test
    fun `validate on an empty form shows both errors`() {
        val form = ExpenseFormState(date = date)
        assertNull(form.validate())
        assertEquals(
            listOf(ExpenseFormValidationError.EmptyName, ExpenseFormValidationError.EmptyAmount),
            form.errors
        )
    }

    @Test
    fun `fixing the amount clears only the amount error`() {
        val form = ExpenseFormState(date = date)
        form.validate()
        form.amount.setTextAndPlaceCursorAtEnd("25.00")
        assertEquals(listOf(ExpenseFormValidationError.EmptyName), form.errors)
    }

    @Test
    fun `editing an existing expense round trips`() {
        val original =
            Expense(id = 7, name = "Lunch", amountMinor = 1999, note = "work", date = date)
        val form = ExpenseFormState.fromExpense(original)
        assertEquals(original, form.validate())
    }
}