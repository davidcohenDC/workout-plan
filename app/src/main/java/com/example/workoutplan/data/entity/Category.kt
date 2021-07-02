package com.example.workoutplan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Represents one category
 */
@Entity(tableName = "category")
data class Category(
        @PrimaryKey(autoGenerate = true)
        val categoryId: Long = 0,

        @ColumnInfo(name = "name")
        val name: String,

        )