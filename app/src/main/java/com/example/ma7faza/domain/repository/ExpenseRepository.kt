package com.example.ma7faza.domain.repository

import com.example.ma7faza.domain.models.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun observeAll(): Flow<List<Expense>>
    fun observe(id: Long): Flow<Expense?>

    suspend fun upsert(expense: Expense)

    suspend fun delete(id: Long)
}