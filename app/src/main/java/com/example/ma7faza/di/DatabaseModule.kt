package com.example.ma7faza.di

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.ma7faza.data.local.AppDatabase
import com.example.ma7faza.data.local.CategorySeeds
import com.example.ma7faza.data.local.MIGRATION_2_3
import com.example.ma7faza.data.local.MIGRATION_3_4
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .addMigrations(MIGRATION_2_3, MIGRATION_3_4)
            .addCallback(object : RoomDatabase.Callback() {
                override suspend fun onCreate(connection: SQLiteConnection) {
                    super.onCreate(connection)
                    CategorySeeds.all.forEach { category ->
                        val icon = category.iconKey?.let { "'$it'" } ?: "NULL"
                        val color = category.colorKey?.let { "'$it'" } ?: "NULL"
                        val sql = "INSERT INTO categories (id, name, type, isBuiltIn, iconKey, colorKey) VALUES (" +
                            "'${category.id}', '${category.name}', '${category.type.name}', 1, $icon, $color)"
                        connection.execSQL(sql)
                    }
                    context.assets.open("database/sample.sql").use {
                        connection.execSQL(it.bufferedReader().readText())
                    }
                    connection.execSQL("UPDATE transactions SET category_id = 'OTHER_INCOME' WHERE type = 'INCOME'")
                }
            })
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    @Singleton
    fun providesTransactionDao(database: AppDatabase) = database.transactionDao()

    @Provides
    @Singleton
    fun providesCategoryDao(database: AppDatabase) = database.categoryDao()
}
