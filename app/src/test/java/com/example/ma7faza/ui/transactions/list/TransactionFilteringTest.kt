package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class TransactionFilteringTest {

    private val categories = mapOf(
        "FOOD" to category("FOOD", "Groceries", TransactionType.EXPENSE),
        "TRANSPORT" to category("TRANSPORT", "Transport", TransactionType.EXPENSE),
        "SALARY" to category("SALARY", "Salary", TransactionType.INCOME)
    )

    private val transactions = listOf(
        transaction(1, "Monthly payment", TransactionType.INCOME, categoryId = "SALARY"),
        transaction(2, "Supermarket", TransactionType.EXPENSE, categoryId = "FOOD"),
        transaction(3, "Freelance", TransactionType.INCOME, categoryId = "SALARY"),
        transaction(
            4,
            "Taxi",
            TransactionType.EXPENSE,
            note = "Airport ride",
            categoryId = "TRANSPORT"
        )
    )

    @Test
    fun `ALL returns every transaction`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionFilterState(),
            categories
        )

        assertEquals(4, result.size)
    }

    @Test
    fun `INCOME returns only income transactions`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionFilterState(TransactionTypeFilter.INCOME),
            categories
        )

        assertEquals(listOf(1L, 3L), result.map { it.id })
    }

    @Test
    fun `EXPENSE returns only expense transactions`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionFilterState(TransactionTypeFilter.EXPENSE),
            categories
        )

        assertEquals(listOf(2L, 4L), result.map { it.id })
    }

    @Test
    fun `search and type filter work together`() {
        val result = filterTransactions(
            transactions,
            "super",
            TransactionFilterState(TransactionTypeFilter.EXPENSE),
            categories
        )

        assertEquals(listOf(2L), result.map { it.id })
    }

    @Test
    fun `search matches transaction name case insensitively`() {
        val result = filterTransactions(
            transactions,
            "MONTHLY",
            TransactionFilterState(),
            categories
        )

        assertEquals(listOf(1L), result.map { it.id })
    }

    @Test
    fun `search matches note`() {
        val result = filterTransactions(
            transactions,
            "airport",
            TransactionFilterState(),
            categories
        )

        assertEquals(listOf(4L), result.map { it.id })
    }

    @Test
    fun `search matches category name`() {
        val result = filterTransactions(
            transactions,
            "groceries",
            TransactionFilterState(),
            categories
        )

        assertEquals(listOf(2L), result.map { it.id })
    }

    @Test
    fun `search trims surrounding whitespace`() {
        val result = filterTransactions(
            transactions,
            "  supermarket  ",
            TransactionFilterState(),
            categories
        )

        assertEquals(listOf(2L), result.map { it.id })
    }

    @Test
    fun `category filter supports multiple selected categories`() {
        val result = filterTransactions(
            transactions,
            "",
            TransactionFilterState(categoryIds = setOf("FOOD", "TRANSPORT")),
            categories
        )

        assertEquals(listOf(2L, 4L), result.map { it.id })
    }

    @Test
    fun `date range includes both boundary dates`() {
        val datedTransactions = listOf(
            transaction(5, "Before", TransactionType.EXPENSE, categoryId = "FOOD", date = LocalDate.of(2026, 10, 31)),
            transaction(6, "Start", TransactionType.EXPENSE, categoryId = "FOOD", date = LocalDate.of(2026, 11, 1)),
            transaction(7, "End", TransactionType.EXPENSE, categoryId = "FOOD", date = LocalDate.of(2026, 11, 3)),
            transaction(8, "After", TransactionType.EXPENSE, categoryId = "FOOD", date = LocalDate.of(2026, 11, 4))
        )

        val result = filterTransactions(
            datedTransactions,
            "",
            TransactionFilterState(
                startDate = LocalDate.of(2026, 11, 1),
                endDate = LocalDate.of(2026, 11, 3)
            ),
            categories
        )

        assertEquals(listOf(6L, 7L), result.map { it.id })
    }

    private fun transaction(
        id: Long,
        name: String,
        type: TransactionType,
        note: String? = null,
        categoryId: String,
        date: LocalDate = LocalDate.of(2026, 11, 1)
    ) = Transaction(
        id = id,
        name = name,
        amountMinor = 1000,
        note = note,
        date = date,
        type = type,
        categoryId = categoryId
    )

    private fun category(
        id: String,
        name: String,
        type: TransactionType
    ) = Category(
        id = id,
        name = name,
        type = type,
        isBuiltIn = true,
        iconKey = null
    )
}
