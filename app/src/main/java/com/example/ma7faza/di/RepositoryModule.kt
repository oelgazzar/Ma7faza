package com.example.ma7faza.di
import com.example.ma7faza.data.repository.CategoryRepositoryImpl
import com.example.ma7faza.data.repository.TransactionRepositoryImpl
import com.example.ma7faza.domain.repository.CategoryRepository
import com.example.ma7faza.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds abstract fun bindTransactionRepository(impl: TransactionRepositoryImpl): TransactionRepository
    @Binds abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}
