package com.example.workoutplan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(

        @PrimaryKey(autoGenerate = true)
        val workoutId: Long,

        @ColumnInfo(name = "title")
        val title: String,

        @ColumnInfo(name = "category")
        val categoryId: Long,

        @ColumnInfo(name = "difficulty")
        val difficultyId: Long,

        @ColumnInfo(name = "muscle")
        val muscleId: Long,

        )