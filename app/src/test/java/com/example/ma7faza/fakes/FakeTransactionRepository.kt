package com.example.ma7faza.fakes

import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.TransactionRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeTransactionRepository : TransactionRepository {
    var transactions = mutableListOf<Transaction>()
    var shouldThrowOnUpsert = false
    var shouldSuspenOnUpsert = false
    val upsertGate = CompletableDeferred<Unit>()
    var upsertCalls = 0

    override fun observeAll(): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override fun observe(id: Long): Flow<Transaction?> {
        return flowOf(transactions.find { it.id == id })
    }

    override fun observeBalance(): Flow<Long> {
        val balance = transactions.sumOf {
            when (it.type) {
                TransactionType.INCOME -> it.amountMinor
                TransactionType.EXPENSE -> -it.amountMinor
            }
        }
        return flowOf(balance)
    }

    override suspend fun upsert(transaction: Transaction) {
        upsertCalls++

        if (shouldThrowOnUpsert) {
            throw Exception("Upsert failed")
        }
        if (!shouldSuspenOnUpsert) {
            upsertGate.complete(Unit)
        }
        upsertGate.await()

        val index = transactions.indexOfFirst { it.id == transaction.id }

        if (index == -1) {
            transactions.add(transaction)
        } else {
            transactions[index] = transaction
        }
    }

    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}