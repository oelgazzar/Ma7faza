package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class TransactionGroupingTest {

    @Test
    fun `empty list returns empty groups`() {
        val result = groupTransactionsByDate(emptyList())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `transactions with same date belong to same group`() {
        val date = LocalDate.of(2026, 10, 30)

        val transactions = listOf(
            transaction(1, date),
            transaction(2, date),
            transaction(3, date)
        )

        val result = groupTransactionsByDate(transactions)

        assertEquals(1, result.size)
        assertEquals(date, result[0].date)
        assertEquals(3, result[0].transactions.size)
    }

    @Test
    fun `transactions with different dates create separate groups`() {
        val today = LocalDate.of(2026, 10, 30)
        val yesterday = today.minusDays(1)

        val transactions = listOf(
            transaction(1, today),
            transaction(2, yesterday)
        )

        val result = groupTransactionsByDate(transactions)

        assertEquals(2, result.size)
        assertEquals(today, result[0].date)
        assertEquals(yesterday, result[1].date)
    }

    @Test
    fun `groups are sorted by date descending`() {
        val today = LocalDate.of(2026, 10, 30)
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)

        val transactions = listOf(
            transaction(1, twoDaysAgo),
            transaction(2, today),
            transaction(3, yesterday)
        )

        val result = groupTransactionsByDate(transactions)

        assertEquals(
            listOf(today, yesterday, twoDaysAgo),
            result.map { it.date }
        )
    }

    @Test
    fun `grouping preserves all original transactions`() {
        val today = LocalDate.of(2026, 10, 30)

        val transactions = listOf(
            transaction(1, today),
            transaction(2, today.minusDays(1)),
            transaction(3, today)
        )

        val result = groupTransactionsByDate(transactions)

        val groupedExpenses = result.flatMap { it.transactions }

        assertEquals(transactions.size, groupedExpenses.size)
        assertEquals(transactions.toSet(), groupedExpenses.toSet())
    }

    @Test
    fun `transactions within each group preserve input order`() {
        val date = LocalDate.of(2026, 10, 30)

        val transactions = listOf(
            transaction(3, date),
            transaction(1, date),
            transaction(2, date)
        )

        val result = groupTransactionsByDate(transactions)

        assertEquals(
            listOf(3L, 1L, 2L),
            result.first().transactions.map { it.id }
        )
    }

    private fun transaction(
        id: Long,
        date: LocalDate
    ) = Transaction(
        id = id,
        name = "Expense $id",
        amountMinor = 1000,
        date = date,
        type = TransactionType.EXPENSE
    )
}