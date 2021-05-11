package com.example.workoutplan.data.exercise

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents one exercise
 */
@Entity(tableName = "exercise")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Long = 0L,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "muscle")
    val muscle: Int,

    @ColumnInfo(name = "difficulty")
    val difficulty: Int,

    @ColumnInfo(name = "category")
    val category: Int
)