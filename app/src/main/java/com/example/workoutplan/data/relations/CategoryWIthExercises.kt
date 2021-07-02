package com.example.workoutplan.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutplan.data.entity.Exercise

data class CategoryWIthExercises(
        @Embedded val exercise: Exercise,
        @Relation(
                parentColumn = "exerciseId",
                entityColumn = "categoryId"
        )
        val exercises: List<Exercise>,
)