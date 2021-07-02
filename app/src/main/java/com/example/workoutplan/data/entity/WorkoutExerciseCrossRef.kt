package com.example.workoutplan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(primaryKeys = ["workoutId", "exerciseId"], tableName = "workoutExerciseCross")
data class WorkoutExerciseCrossRef(
        val workoutId: Long,
        val exerciseId: Long,

        @ColumnInfo(name = "set")
        val set: Int?,

        @ColumnInfo(name = "repetition")
        val repetition: Int?,

        @ColumnInfo(name = "duration")
        val duration: Int?,
)