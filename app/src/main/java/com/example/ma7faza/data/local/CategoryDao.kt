package com.example.ma7faza.data.local
import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import com.example.ma7faza.domain.models.TransactionType
import kotlinx.coroutines.flow.Flow
@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY isBuiltIn DESC, name COLLATE NOCASE")
    fun observeByType(type: TransactionType): Flow<List<CategoryEntity>>
    @Upsert suspend fun upsert(category: CategoryEntity)
}
