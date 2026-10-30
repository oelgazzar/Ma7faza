package com.example.ma7faza.fakes

import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeExpenseRepository : ExpenseRepository {
    var expenses = mutableListOf<Expense>()

    override fun observeAll(): Flow<List<Expense>> {
        TODO("Not yet implemented")
    }

    override fun observe(id: Long): Flow<Expense?> {
        return flowOf(expenses.find { it.id == id })
    }

    override suspend fun upsert(expense: Expense) {
        val index = expenses.indexOfFirst { it.id == expense.id }

        if (index == -1) {
            expenses.add(expense)
        } else {
            expenses[index] = expense
        }
    }

    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}