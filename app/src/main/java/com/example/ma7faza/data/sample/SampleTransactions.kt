package com.example.ma7faza.data.sample

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import java.time.LocalDate


val sampleTransactions = listOf(
    Transaction(
        id = 1, name = "Shopping", amountMinor = 1000, date = LocalDate.now(), type = TransactionType.EXPENSE
    ),
    Transaction(
        id = 2, name = "Groceries", amountMinor = 500, date = LocalDate.now(), type = TransactionType.EXPENSE
    ),
    Transaction(
        id = 3, name = "Transport", amountMinor = 200, date = LocalDate.now(), type = TransactionType.EXPENSE
    ),
    Transaction(
        id = 4, name = "Entertainment", amountMinor = 300, date = LocalDate.now(), type = TransactionType.EXPENSE
    ),
)