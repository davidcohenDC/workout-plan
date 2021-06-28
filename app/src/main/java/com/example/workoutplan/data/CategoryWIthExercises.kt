package com.example.workoutplan.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutplan.data.exercise.Exercise

data class CategoryWIthExercises(
        @Embedded val exercise: Exercise,
        @Relation(
                parentColumn = "exerciseId",
                entityColumn = "categoryId"
        )
        val exercises: List<Exercise>
)