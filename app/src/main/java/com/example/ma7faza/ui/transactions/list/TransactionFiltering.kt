package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import java.time.LocalDate

enum class TransactionTypeFilter {
    ALL,
    INCOME,
    EXPENSE
}

data class TransactionFilterState(
    val type: TransactionTypeFilter = TransactionTypeFilter.ALL,
    val categoryIds: Set<String> = emptySet(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
)

fun filterTransactions(
    transactions: List<Transaction>,
    query: String,
    filters: TransactionFilterState,
    categories: Map<String, Category>
): List<Transaction> {
    val normalizedQuery = query.trim()

    return transactions.filter { transaction ->
        val matchesType = when (filters.type) {
            TransactionTypeFilter.ALL -> true
            TransactionTypeFilter.INCOME ->
                transaction.type == TransactionType.INCOME
            TransactionTypeFilter.EXPENSE ->
                transaction.type == TransactionType.EXPENSE
        }

        val matchesCategory = filters.categoryIds.isEmpty() ||
            transaction.categoryId in filters.categoryIds

        val matchesDate = (filters.startDate == null || !transaction.date.isBefore(filters.startDate)) &&
            (filters.endDate == null || !transaction.date.isAfter(filters.endDate))

        val matchesQuery = normalizedQuery.isBlank() ||
            transaction.name.contains(normalizedQuery, ignoreCase = true) ||
            transaction.note.orEmpty().contains(normalizedQuery, ignoreCase = true) ||
            categories[transaction.categoryId]?.name
                ?.contains(normalizedQuery, ignoreCase = true) == true

        matchesType && matchesCategory && matchesDate && matchesQuery
    }
}
