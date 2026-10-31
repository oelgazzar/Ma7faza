package com.example.ma7faza.domain.repository

import com.example.ma7faza.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeAll(): Flow<List<Transaction>>
    fun observe(id: Long): Flow<Transaction?>
    fun observeBalance(): Flow<Long>

    suspend fun upsert(transaction: Transaction)

    suspend fun delete(id: Long)
}