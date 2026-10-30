package com.example.ma7faza.di

import android.content.Context
import androidx.room3.Room
import com.example.ma7faza.data.local.AppDatabase
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
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration(true)
        .build()

    @Singleton
    @Provides
    fun providesTransactionDao(database: AppDatabase) = database.transactionDao()
}