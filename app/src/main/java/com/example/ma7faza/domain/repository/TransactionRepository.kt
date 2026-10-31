package com.example.ma7faza.domain.repository

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionSummary
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observe(id: Long): Flow<Transaction?>
    fun observeBalance(): Flow<Long>
    fun observeSummary(): Flow<TransactionSummary>

    suspend fun upsert(transaction: Transaction)

    suspend fun delete(id: Long)
}