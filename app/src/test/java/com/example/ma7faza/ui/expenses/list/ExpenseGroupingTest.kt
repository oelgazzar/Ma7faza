package com.example.ma7faza.ui.expenses.list

import com.example.ma7faza.domain.models.Expense
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ExpenseGroupingTest {

    @Test
    fun `empty list returns empty groups`() {
        val result = groupExpensesByDate(emptyList())

        assertTrue(result.isEmpty())
    }

    @Test
    fun `expenses with same date belong to same group`() {
        val date = LocalDate.of(2026, 10, 30)

        val expenses = listOf(
            expense(1, date),
            expense(2, date),
            expense(3, date)
        )

        val result = groupExpensesByDate(expenses)

        assertEquals(1, result.size)
        assertEquals(date, result[0].date)
        assertEquals(3, result[0].expenses.size)
    }

    @Test
    fun `expenses with different dates create separate groups`() {
        val today = LocalDate.of(2026, 10, 30)
        val yesterday = today.minusDays(1)

        val expenses = listOf(
            expense(1, today),
            expense(2, yesterday)
        )

        val result = groupExpensesByDate(expenses)

        assertEquals(2, result.size)
        assertEquals(today, result[0].date)
        assertEquals(yesterday, result[1].date)
    }

    @Test
    fun `groups are sorted by date descending`() {
        val today = LocalDate.of(2026, 10, 30)
        val yesterday = today.minusDays(1)
        val twoDaysAgo = today.minusDays(2)

        val expenses = listOf(
            expense(1, twoDaysAgo),
            expense(2, today),
            expense(3, yesterday)
        )

        val result = groupExpensesByDate(expenses)

        assertEquals(
            listOf(today, yesterday, twoDaysAgo),
            result.map { it.date }
        )
    }

    @Test
    fun `grouping preserves all original expenses`() {
        val today = LocalDate.of(2026, 10, 30)

        val expenses = listOf(
            expense(1, today),
            expense(2, today.minusDays(1)),
            expense(3, today)
        )

        val result = groupExpensesByDate(expenses)

        val groupedExpenses = result.flatMap { it.expenses }

        assertEquals(expenses.size, groupedExpenses.size)
        assertEquals(expenses.toSet(), groupedExpenses.toSet())
    }

    @Test
    fun `expenses within each group preserve input order`() {
        val date = LocalDate.of(2026, 10, 30)

        val expenses = listOf(
            expense(3, date),
            expense(1, date),
            expense(2, date)
        )

        val result = groupExpensesByDate(expenses)

        assertEquals(
            listOf(3L, 1L, 2L),
            result.first().expenses.map { it.id }
        )
    }

    private fun expense(
        id: Long,
        date: LocalDate
    ) = Expense(
        id = id,
        name = "Expense $id",
        amountMinor = 1000,
        date = date
    )
}