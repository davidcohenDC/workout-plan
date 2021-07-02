package com.example.workoutplan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a difficulty
 */
@Entity(tableName = "difficulty")
data class Difficulty(
        @PrimaryKey(autoGenerate = true)
        val difficultyId: Long = 0,

        @ColumnInfo(name = "name")
        val name: String,

        )