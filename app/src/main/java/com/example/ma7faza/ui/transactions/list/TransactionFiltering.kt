package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType

enum class TransactionTypeFilter {
    ALL,
    INCOME,
    EXPENSE
}

fun filterTransactions(
    transactions: List<Transaction>,
    query: String,
    typeFilter: TransactionTypeFilter
): List<Transaction> {
    val normalizedQuery = query.trim()

    return transactions.filter { transaction ->
        val matchesType = when (typeFilter) {
            TransactionTypeFilter.ALL -> true
            TransactionTypeFilter.INCOME ->
                transaction.type == TransactionType.INCOME
            TransactionTypeFilter.EXPENSE ->
                transaction.type == TransactionType.EXPENSE
        }

        val matchesQuery = normalizedQuery.isBlank() ||
            transaction.name.contains(normalizedQuery, ignoreCase = true) ||
            transaction.note.orEmpty().contains(normalizedQuery, ignoreCase = true)

        matchesType && matchesQuery
    }
}
