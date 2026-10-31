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
    fun `new form defaults to EXPENSE`() {
        val form = TransactionFormState(date = date)
        assertEquals(TransactionType.EXPENSE, form.type)
    }

    @Test
    fun `changing type to INCOME is reflected in validated transaction`() {
        val form = TransactionFormState(date = date)
        form.name.setTextAndPlaceCursorAtEnd("Salary")
        form.amount.setTextAndPlaceCursorAtEnd("25000")
        form.updateType(TransactionType.INCOME)

        assertEquals(TransactionType.INCOME, form.validate()?.type)
    }

    @Test
    fun `new form uses matching default category`() {
        val form = TransactionFormState(date = date)
        assertEquals("OTHER_EXPENSE", form.categoryId)
        form.updateType(TransactionType.INCOME)
        assertEquals("OTHER_INCOME", form.categoryId)
    }

    @Test
    fun `changing category is included in validated transaction`() {
        val form = TransactionFormState(date = date)
        form.name.setTextAndPlaceCursorAtEnd("Lunch")
        form.amount.setTextAndPlaceCursorAtEnd("25")
        form.updateCategory("FOOD")

        assertEquals("FOOD", form.validate()?.categoryId)
    }

    @Test
    fun `editing transaction preserves category`() {
        val original = Transaction(
            id = 9,
            name = "Lunch",
            amountMinor = 2500,
            date = date,
            type = TransactionType.EXPENSE,
            categoryId = "FOOD"
        )
        val form = TransactionFormState.fromTransaction(original)

        assertEquals("FOOD", form.categoryId)
        assertEquals(original, form.validate())
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

    @Test
    fun `editing an existing income transaction preserves its type`() {
        val original = Transaction(
            id = 8,
            name = "Salary",
            amountMinor = 2_500_000,
            date = date,
            type = TransactionType.INCOME
        )
        val form = TransactionFormState.fromTransaction(original)

        assertEquals(TransactionType.INCOME, form.type)
        assertEquals(original, form.validate())
    }
}