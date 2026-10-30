package com.example.ma7faza.ui.transactions.form

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate

class TransactionFormValidationTest {
    @Test
    fun `validateTransactionForm should return Success when form is valid`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Success(
                Transaction(
                    id = 1,
                    name = "Lunch",
                    amountMinor = 1999,
                    date = LocalDate.of(2026, 10, 29),
                    type = TransactionType.EXPENSE
                )
            ), result
        )
    }

    @Test
    fun `validateTransactionForm preserves INCOME type`() {
        val result = validateTransactionForm(
            id = 2,
            name = "Salary",
            amount = "25000",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.INCOME
        )

        assertEquals(
            TransactionFormValidationResult.Success(
                Transaction(
                    id = 2,
                    name = "Salary",
                    amountMinor = 2_500_000,
                    date = LocalDate.of(2026, 10, 29),
                    type = TransactionType.INCOME
                )
            ),
            result
        )
    }

    @Test
    fun `validateTransactionForm should return EmptyName Error when name is empty`() {
        val result = validateTransactionForm(
            id = 1,
            name = "",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(listOf(TransactionFormValidationError.EmptyName)), result
        )
    }

    @Test
    fun `validateTransactionForm should return EmptyName Error when name has only white spaces`() {
        val result = validateTransactionForm(
            id = 1,
            name = "    ",
            amount = "19.99",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(listOf(TransactionFormValidationError.EmptyName)), result
        )
    }

    @Test
    fun `validateTransactionForm should return EmptyAmount Error when amount is empty`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(TransactionFormValidationError.EmptyAmount)
            ), result
        )
    }

    @Test
    fun `validateTransactionForm should return InvalidAmount Error when amount is not number`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "abc",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(TransactionFormValidationError.InvalidAmount)
            ), result
        )
    }

    @Test
    fun `validateTransactionForm should return InvalidAmount Error when amount has more than 2 decimal places`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "5.999",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(TransactionFormValidationError.InvalidAmount)
            ), result
        )
    }

    @Test
    fun `validateTransactionForm should return InvalidAmount Error when amount has negative value`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "-3",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(TransactionFormValidationError.InvalidAmount)
            ), result
        )
    }

    @Test
    fun `validateTransactionForm should return ZeroAmount Error when amount is zero`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "0",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(listOf(TransactionFormValidationError.ZeroAmount)), result
        )
    }

    @Test
    fun `validateTransactionForm should return ZeroAmount Error when amount is negative zero`() {
        val result = validateTransactionForm(
            id = 1,
            name = "Lunch",
            amount = "-0",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(listOf(TransactionFormValidationError.ZeroAmount)), result
        )
    }

    @Test
    fun `validateTransactionForm should return both EmptyName and InvalidAmount Errors`() {
        val result = validateTransactionForm(
            id = 1,
            name = "",
            amount = "abc",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(
                    TransactionFormValidationError.EmptyName,
                    TransactionFormValidationError.InvalidAmount
                )
            ), result
        )
    }

    @Test
    fun `validateTransactionForm should return both EmptyName and ZeroAmount Errors`() {
        val result = validateTransactionForm(
            id = 1,
            name = "",
            amount = "0",
            note = null,
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )

        assertEquals(
            TransactionFormValidationResult.Error(
                listOf(
                    TransactionFormValidationError.EmptyName,
                    TransactionFormValidationError.ZeroAmount
                )
            ), result
        )
    }

    @Test
    fun `passing name and note with white spaces return trimmed version`() {
        val result = validateTransactionForm(
            id = 1,
            name = " hi ",
            amount = "5",
            note = " welcome  ",
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )
        assertEquals(
            TransactionFormValidationResult.Success(
                Transaction(
                    id = 1,
                    name = "hi",
                    amountMinor = 500,
                    note = "welcome",
                    date = LocalDate.of(2026, 10, 29),
                    type = TransactionType.EXPENSE
                )
            ),
            result
        )
    }

    @Test
    fun `passing note with only white spaces returns null`() {
        val result = validateTransactionForm(
            id = 1,
            name = "hi",
            amount = "5",
            note = "      ",
            date = LocalDate.of(2026, 10, 29),
            type = TransactionType.EXPENSE
        )
        assertEquals(
            TransactionFormValidationResult.Success(
                Transaction(
                    id = 1,
                    name = "hi",
                    amountMinor = 500,
                    date = LocalDate.of(2026, 10, 29),
                    type = TransactionType.EXPENSE
                )
            ),
            result
        )
    }
}