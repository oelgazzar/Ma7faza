package com.example.ma7faza.data.repository

import com.example.ma7faza.data.local.CategoryDao
import com.example.ma7faza.data.local.CategoryEntity
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import com.example.ma7faza.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun observeByType(type: TransactionType): Flow<List<Category>> =
        categoryDao.observeByType(type).map { list -> list.map { it.toDomain() } }
}

private fun CategoryEntity.toDomain() =
    Category(id, name, type, isBuiltIn, iconKey, colorKey)
