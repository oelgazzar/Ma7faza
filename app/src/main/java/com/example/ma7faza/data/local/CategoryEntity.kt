package com.example.ma7faza.data.local
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.ma7faza.domain.models.TransactionType
@Entity(tableName = "categories")
data class CategoryEntity(@PrimaryKey val id: String, val name: String, val type: TransactionType, val isBuiltIn: Boolean, val iconKey: String? = null)
