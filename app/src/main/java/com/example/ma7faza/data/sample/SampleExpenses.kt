package com.example.ma7faza.data.sample

import com.example.ma7faza.domain.models.Expense
import java.time.LocalDate


val sampleExpenses = listOf(
    Expense(
        id = 1, name = "Shopping", amountMinor = 1000, note = null, date = LocalDate.now()
    ),
    Expense(
        id = 2, name = "Groceries", amountMinor = 500, note = null, date = LocalDate.now()
    ),
    Expense(
        id = 3, name = "Transport", amountMinor = 200, note = null, date = LocalDate.now()
    ),
    Expense(
        id = 4, name = "Entertainment", amountMinor = 300, note = null, date = LocalDate.now()
    ),
)