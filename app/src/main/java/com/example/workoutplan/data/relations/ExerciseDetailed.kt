package com.example.workoutplan.data.relations

class ExerciseDetailed(
        val workoutId: Long,
        val exerciseId: Long,
        val description: String,
        val name: String,
        val muscle: Long,
        val difficulty: Long,
        val category: Long,
        val set: Int?,
        val repetition: Int?,
        val duration: Long?
)