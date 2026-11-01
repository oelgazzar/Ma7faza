package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class TransactionFilteringTest {

    private val transactions = listOf(
        transaction(1, "Salary", TransactionType.INCOME),
        transaction(2, "Food", TransactionType.EXPENSE),
        transaction(3, "Freelance", TransactionType.INCOME),
        transaction(4, "Transport", TransactionType.EXPENSE)
    )

    @Test
    fun `ALL returns every transaction`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionTypeFilter.ALL
        )

        assertEquals(4, result.size)
    }

    @Test
    fun `INCOME returns only income transactions`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionTypeFilter.INCOME
        )

        assertEquals(listOf(1L, 3L), result.map { it.id })
    }

    @Test
    fun `EXPENSE returns only expense transactions`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionTypeFilter.EXPENSE
        )

        assertEquals(listOf(2L, 4L), result.map { it.id })
    }

    @Test
    fun `search and type filter work together`() {
        val result = filterTransactions(
            transactions,
            "food",
            TransactionTypeFilter.EXPENSE
        )

        assertEquals(listOf(2L), result.map { it.id })
    }

    @Test
    fun `search is case insensitive`() {
        val result = filterTransactions(
            transactions,
            "SALARY",
            TransactionTypeFilter.ALL
        )

        assertEquals(listOf(1L), result.map { it.id })
    }

    @Test
    fun `search trims surrounding whitespace`() {
        val result = filterTransactions(
            transactions,
            "  food  ",
            TransactionTypeFilter.ALL
        )

        assertEquals(listOf(2L), result.map { it.id })
    }

    private fun transaction(
        id: Long,
        name: String,
        type: TransactionType
    ) = Transaction(
        id = id,
        name = name,
        amountMinor = 1000,
        date = LocalDate.of(2026, 11, 1),
        type = type
    )
}
