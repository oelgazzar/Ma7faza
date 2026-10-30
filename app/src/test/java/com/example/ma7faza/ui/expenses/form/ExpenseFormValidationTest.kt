package com.example.ma7faza.ui.expenses.form

import com.example.ma7faza.domain.models.Expense
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class ExpenseFormValidationTest {
    @Test
    fun `validateExpenseForm should return Success when form is valid`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Success(
                Expense(
                    id = 1,
                    name = "Lunch",
                    amountMinor = 1999,
                    note = null,
                    date = LocalDate.of(2026, 10, 29)
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return EmptyName Error when name is empty`() {
        val result = validateExpenseForm(
            id = 1,
            name = "",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(listOf(ExpenseFormValidationError.EmptyName)), result
        )
    }

    @Test
    fun `validateExpenseForm should return EmptyName Error when name has only white spaces`() {
        val result = validateExpenseForm(
            id = 1,
            name = "    ",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(listOf(ExpenseFormValidationError.EmptyName)), result
        )
    }

    @Test
    fun `validateExpenseForm should return EmptyAmount Error when amount is empty`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.EmptyAmount
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return InvalidAmount Error when amount is not number`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "abc",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.InvalidAmount
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return InvalidAmount Error when amount has more than 2 decimal places`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "5.999",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.InvalidAmount
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return InvalidAmount Error when amount has negative value`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "-3",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.InvalidAmount
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return ZeroAmount Error when amount is zero`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "0",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(listOf(ExpenseFormValidationError.ZeroAmount)), result
        )
    }

    @Test
    fun `validateExpenseForm should return ZeroAmount Error when amount is negative zero`() {
        val result = validateExpenseForm(
            id = 1,
            name = "Lunch",
            amount = "-0",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(listOf(ExpenseFormValidationError.ZeroAmount)), result
        )
    }

    @Test
    fun `validateExpenseForm should return both EmptyName and InvalidAmount Errors`() {
        val result = validateExpenseForm(
            id = 1,
            name = "",
            amount = "abc",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.EmptyName,
                    ExpenseFormValidationError.InvalidAmount
                )
            ), result
        )
    }

    @Test
    fun `validateExpenseForm should return both EmptyName and ZeroAmount Errors`() {
        val result = validateExpenseForm(
            id = 1,
            name = "",
            amount = "0",
            note = null,
            date = LocalDate.of(2026, 10, 29)
        )

        assertEquals(
            ExpenseFormValidationResult.Error(
                listOf(
                    ExpenseFormValidationError.EmptyName,
                    ExpenseFormValidationError.ZeroAmount

                )
            ), result
        )
    }
}