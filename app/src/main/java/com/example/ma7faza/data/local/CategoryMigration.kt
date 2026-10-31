package com.example.ma7faza.data.local

import androidx.room3.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

val MIGRATION_2_3 = object : Migration(2, 3) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("""
            CREATE TABLE IF NOT EXISTS categories (
                id TEXT NOT NULL, name TEXT NOT NULL, type TEXT NOT NULL,
                isBuiltIn INTEGER NOT NULL, iconKey TEXT, PRIMARY KEY(id)
            )
        """.trimIndent())
        CategorySeeds.all.forEach { category ->
            val icon = category.iconKey?.let { "'$it'" } ?: "NULL"
            val sql = "INSERT INTO categories (id, name, type, isBuiltIn, iconKey) VALUES (" +
                "'${category.id}', '${category.name}', '${category.type.name}', 1, $icon)"
            connection.execSQL(sql)
        }
        connection.execSQL("ALTER TABLE transactions ADD COLUMN category_id TEXT NOT NULL DEFAULT 'OTHER_EXPENSE'")
        connection.execSQL("UPDATE transactions SET category_id = 'OTHER_INCOME' WHERE type = 'INCOME'")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL("ALTER TABLE categories ADD COLUMN colorKey TEXT")
    }
}
