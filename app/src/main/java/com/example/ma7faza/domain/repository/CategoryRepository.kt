package com.example.ma7faza.domain.repository
import com.example.ma7faza.domain.models.Category
import com.example.ma7faza.domain.models.TransactionType
import kotlinx.coroutines.flow.Flow
interface CategoryRepository {
    fun observeByType(type: TransactionType): Flow<List<Category>>
}
