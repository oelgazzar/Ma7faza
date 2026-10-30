package com.example.ma7faza.ui.expenses.list

import com.example.ma7faza.domain.models.Expense
import java.time.LocalDate

fun groupExpensesByDate(expenses: List<Expense>): List<ExpenseGroup> {
    return expenses
        .groupBy { it.date }
        .map { (date, expenses) ->
            ExpenseGroup(
                date = date,
                expenses = expenses
            )
        }
        .sortedByDescending { it.date }
}

data class ExpenseGroup(
    val date: LocalDate,
    val expenses: List<Expense>
)