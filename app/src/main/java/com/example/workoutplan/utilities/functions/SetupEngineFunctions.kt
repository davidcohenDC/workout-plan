package com.example.workoutplan.utilities.functions

import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef
import com.example.workoutplan.serializable.WorkoutSetup

fun workoutSetupToWorkout(workoutSetup: WorkoutSetup): Workout {
    return Workout(0L, workoutSetup.title, workoutSetup.category, workoutSetup.difficulty, workoutSetup.muscle)
}

fun workoutToWorkoutSetup(workout: Workout): WorkoutSetup {
    return WorkoutSetup().apply {
        this.title = workout.title
        this.category = workout.categoryId
        this.difficulty = workout.difficultyId
        this.muscle = workout.muscleId
        this.exercises = listOf()
    }
}

fun configureWorkout(workoutId: Long, workoutSetup: WorkoutSetup, exercises: List<Exercise>): List<WorkoutExerciseCrossRef> {
    return exercises.map {
        WorkoutExerciseCrossRef(workoutId, it.exerciseId, confSet(it, workoutSetup), confRepetition(it, workoutSetup), confDuration(it, workoutSetup))
    }
}

//for set
private fun confSet(exercise: Exercise, workoutSetup: WorkoutSetup): Int? {
    return when (exercise.category) {
        1L -> {
            when (workoutSetup.difficulty) {
                1L -> 2
                2L -> 2
                3L -> 3
                4L -> 4
                5L -> 5
                else -> null
            }
        }
        2L -> null
        3L -> null
        else -> null
    }
}

private fun confRepetition(exercise: Exercise, workoutSetup: WorkoutSetup): Int? {
    return when (exercise.category) {
        1L -> {
            when (workoutSetup.difficulty) {
                1L -> 6
                2L -> 8
                3L -> 10
                4L -> 12
                5L -> 15
                else -> null
            }
        }
        2L -> null
        3L -> null
        else -> null
    }
}

private fun confDuration(exercise: Exercise, workoutSetup: WorkoutSetup): Int? {
    return when (exercise.category) {
        1L -> null
        2L -> {
            when (workoutSetup.difficulty) {
                1L -> when (exercise.difficulty) {
                    2L -> 600
                    3L -> 720
                    4L -> 840
                    5L -> 960
                    else -> null
                }
                2L -> when (exercise.difficulty) {
                    2L -> 1080
                    3L -> 1200
                    4L -> 1320
                    5L -> 1500
                    else -> null
                }
                3L -> when (exercise.difficulty) {
                    2L -> 1080
                    3L -> 1200
                    4L -> 1320
                    5L -> 1500
                    else -> null
                }
                4L -> when (exercise.difficulty) {
                    2L -> 1080
                    3L -> 1200
                    4L -> 1320
                    5L -> 1500
                    else -> null
                }
                5L -> when (exercise.difficulty) {
                    2L -> 1600
                    3L -> 1800
                    4L -> 2100
                    5L -> 2400
                    else -> null
                }
                else -> null
            }
        }
        3L -> {
            when (workoutSetup.difficulty) {
                1L -> when (exercise.difficulty) {
                    2L -> 15
                    3L -> 16
                    4L -> 18
                    5L -> 20
                    else -> null
                }
                2L -> when (exercise.difficulty) {
                    2L -> 22
                    3L -> 24
                    4L -> 25
                    5L -> 27
                    else -> null
                }
                3L -> when (exercise.difficulty) {
                    2L -> 28
                    3L -> 30
                    4L -> 35
                    5L -> 38
                    else -> null
                }
                4L -> when (exercise.difficulty) {
                    2L -> 40
                    3L -> 42
                    4L -> 45
                    5L -> 47
                    else -> null
                }
                5L -> when (exercise.difficulty) {
                    2L -> 50
                    3L -> 52
                    4L -> 54
                    5L -> 60
                    else -> null
                }
                else -> null
            }
        }
        else -> null
    }

}
