package com.example.ma7faza.data.repository
import com.example.ma7faza.data.local.TransactionDao
import com.example.ma7faza.data.local.TransactionEntity
import com.example.ma7faza.data.local.TransactionSummaryResult
import com.example.ma7faza.domain.models.Transaction
import com.example.ma7faza.domain.models.TransactionSummary
import com.example.ma7faza.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject
class TransactionRepositoryImpl @Inject constructor(private val transactionDao: TransactionDao) : TransactionRepository {
    override fun observeAll() = transactionDao.observeAll().map { it.toDomain() }
    override fun observe(id: Long) = transactionDao.observe(id).map { it?.toDomain() }
    override fun observeBalance() = observeSummary().map { it.balanceMinor }
    override fun observeSummary(startDate: LocalDate?, endDate: LocalDate?) =
        transactionDao.observeSummary(startDate, endDate).map { it.toDomain() }
    override suspend fun upsert(transaction: Transaction) = transactionDao.upsert(transaction.toEntity())
    override suspend fun delete(id: Long) = transactionDao.delete(id)
}
private fun TransactionSummaryResult.toDomain() = TransactionSummary(incomeMinor, expensesMinor)
private fun TransactionEntity.toDomain() = Transaction(id, name, amountMinor, note, date, type, categoryId)
private fun List<TransactionEntity>.toDomain() = map { it.toDomain() }
private fun Transaction.toEntity() = TransactionEntity(id, name, amountMinor, note, date, type, categoryId)
