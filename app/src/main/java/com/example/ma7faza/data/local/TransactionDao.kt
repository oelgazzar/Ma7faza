package com.example.ma7faza.data.local

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC")
    fun observeAll(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun observe(id: Long): Flow<TransactionEntity?>

    @Query("""
        SELECT COALESCE(
            SUM(
                CASE
                    WHEN type = 'INCOME' THEN amount_minor
                    WHEN type = 'EXPENSE' THEN -amount_minor
                    ELSE 0
                END
            ),
            0
        )
        FROM transactions
    """)
    fun observeBalance(): Flow<Long>

    @Upsert
    suspend fun upsert(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun delete(id: Long)
}