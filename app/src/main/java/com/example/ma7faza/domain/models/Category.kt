package com.example.ma7faza.domain.models

data class Category(
    val id: String,
    val name: String,
    val type: TransactionType,
    val isBuiltIn: Boolean,
    val iconKey: String?,
    val colorKey: String? = null
)
