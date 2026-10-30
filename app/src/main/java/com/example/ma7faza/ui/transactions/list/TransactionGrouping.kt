package com.example.ma7faza.ui.transactions.list

import com.example.ma7faza.domain.models.Transaction
import java.time.LocalDate

fun groupTransactionsByDate(transactions: List<Transaction>): List<TransactionGroup> {
    return transactions
        .groupBy { it.date }
        .map { (date, expenses) ->
            TransactionGroup(
                date = date,
                transactions = expenses
            )
        }
        .sortedByDescending { it.date }
}

data class TransactionGroup(
    val date: LocalDate,
    val transactions: List<Transaction>
)