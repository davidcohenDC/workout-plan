package com.example.workoutplan.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef

data class WorkoutWithExercises(
        @Embedded val workout: Workout,
        @Relation(
                parentColumn = "workoutId",
                entityColumn = "exerciseId",
                associateBy = Junction(WorkoutExerciseCrossRef::class,
                        parentColumn = "workoutId",
                        entityColumn = "exerciseId")
        )
        val exercises: List<Exercise>,
        val set: Int?,
        val repetition: Int?,
        val duration: Int
        )