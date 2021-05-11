package com.example.workoutplan.data.muscle

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents one muscle
 */
@Entity(tableName = "muscle")
data class Muscle(
    @PrimaryKey(autoGenerate = true)
    val muscleId: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    )