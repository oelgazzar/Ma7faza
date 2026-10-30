package com.example.ma7faza.data.local

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Query
import androidx.room3.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expense ORDER BY date DESC, id DESC")
    fun observeAll(): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE id = :id")
    fun observe(id: Long): Flow<ExpenseEntity?>

    @Upsert
    suspend fun upsert(expense: ExpenseEntity)

    @Query("DELETE FROM expense WHERE id = :id")
    suspend fun delete(id: Long)
}