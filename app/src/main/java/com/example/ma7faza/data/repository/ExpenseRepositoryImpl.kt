package com.example.ma7faza.data.repository

import androidx.compose.ui.graphics.colorspace.Illuminant
import com.example.ma7faza.data.local.ExpenseDao
import com.example.ma7faza.data.local.ExpenseEntity
import com.example.ma7faza.domain.models.Expense
import com.example.ma7faza.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    override fun observeAll() = expenseDao.observeAll().map { it.toDomain() }
    override fun observe(id: Long) = expenseDao.observe(id).map { it?.toDomain() }
    override suspend fun upsert(expense: Expense) = expenseDao.upsert(expense.toEntity())
    override suspend fun delete(id: Long) = expenseDao.delete(id)
}

private fun ExpenseEntity.toDomain() = Expense(
    id = id,
    name = name,
    amountMinor = amountMinor,
    note = note,
    date = date
)

private fun List<ExpenseEntity>.toDomain() = map { it.toDomain() }

private fun Expense.toEntity() = ExpenseEntity(
    id = id,
    name = name,
    amountMinor = amountMinor,
    note = note,
    date = date
)
