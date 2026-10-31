package com.example.ma7faza.data.local
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import com.example.ma7faza.domain.models.TransactionType
import java.time.LocalDate
@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "amount_minor") val amountMinor: Long,
    val note: String? = null,
    val date: LocalDate,
    val type: TransactionType,
    @ColumnInfo(name = "category_id", defaultValue = "'OTHER_EXPENSE'") val categoryId: String = "OTHER_EXPENSE"
)
