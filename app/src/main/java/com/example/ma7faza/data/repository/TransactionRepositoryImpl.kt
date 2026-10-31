package com.example.ma7faza.data.repository

import com.example.ma7faza.data.local.TransactionDao
import com.example.ma7faza.data.local.TransactionEntity
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun observeAll() = transactionDao.observeAll().map { it.toDomain() }
    override fun observe(id: Long) = transactionDao.observe(id).map { it?.toDomain() }
    override fun observeBalance() = transactionDao.observeBalance()
    override suspend fun upsert(transaction: Transaction) = transactionDao.upsert(transaction.toEntity())
    override suspend fun delete(id: Long) = transactionDao.delete(id)
}

private fun TransactionEntity.toDomain() = Transaction(
    id = id,
    name = name,
    amountMinor = amountMinor,
    note = note,
    date = date,
    type = type
)

private fun List<TransactionEntity>.toDomain() = map { it.toDomain() }

private fun Transaction.toEntity() = TransactionEntity(
    id = id,
    name = name,
    amountMinor = amountMinor,
    note = note,
    date = date,
    type = type
)