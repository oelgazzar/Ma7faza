package com.example.ma7faza.di

import com.example.ma7faza.data.repository.ExpenseRepositoryImpl
import com.example.ma7faza.domain.repository.ExpenseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindsExpenseRepository(impl: ExpenseRepositoryImpl): ExpenseRepository
}