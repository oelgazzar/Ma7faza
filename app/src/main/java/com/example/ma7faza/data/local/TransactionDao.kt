package com.example.ma7faza.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.ma7faza.domain.models.CategoryExpense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun observe(id: Long): Flow<TransactionEntity?>

    @Query("""
        SELECT
            COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount_minor ELSE 0 END), 0) AS incomeMinor,
            COALESCE(SUM(CASE WHEN type = 'EXPENSE' THEN amount_minor ELSE 0 END), 0) AS expensesMinor
        FROM transactions
        WHERE (:startDate IS NULL OR date >= :startDate)
          AND (:endDate IS NULL OR date <= :endDate)
    """)
    fun observeSummary(startDate: LocalDate?, endDate: LocalDate?): Flow<TransactionSummaryResult>

    @Query("""
        SELECT
            category_id AS categoryId,
            SUM(amount_minor) AS totalMinor
        FROM transactions
        WHERE type = 'EXPENSE'
          AND (:startDate IS NULL OR date >= :startDate)
          AND (:endDate IS NULL OR date <= :endDate)
        GROUP BY category_id
        ORDER BY totalMinor DESC
    """)
    fun observeCategoryExpenses(
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Flow<List<CategoryExpense>>

    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: Long)
}
