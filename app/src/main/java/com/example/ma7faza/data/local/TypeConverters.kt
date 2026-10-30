package com.example.ma7faza.data.local

import androidx.room3.ColumnTypeConverter
import java.time.LocalDate


object Converters {
    @ColumnTypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @ColumnTypeConverter
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.toString()
    }
}