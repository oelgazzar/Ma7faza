package com.example.ma7faza.data.local

import androidx.room3.ColumnTypeConverters
import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [TransactionEntity::class, CategoryEntity::class], version = 4)
@ColumnTypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
}
