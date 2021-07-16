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
                    1L -> 600
                    2L -> 720
                    3L -> 840
                    4L -> 960
                    else -> null
                }
                2L -> when (exercise.difficulty) {
                    1L -> 1080
                    2L -> 1200
                    3L -> 1320
                    4L -> 1500
                    else -> null
                }
                3L -> when (exercise.difficulty) {
                    1L -> 1080
                    2L -> 1200
                    3L -> 1320
                    4L -> 1500
                    else -> null
                }
                4L -> when (exercise.difficulty) {
                    1L -> 1080
                    2L -> 1200
                    3L -> 1320
                    4L -> 1500
                    else -> null
                }
                5L -> when (exercise.difficulty) {
                    1L -> 1600
                    2L -> 1800
                    3L -> 2100
                    4L -> 2400
                    else -> null
                }
                else -> null
            }
        }
        3L -> {
            when (workoutSetup.difficulty) {
                1L -> when (exercise.difficulty) {
                    1L -> 15
                    2L -> 16
                    3L -> 18
                    4L -> 20
                    else -> null
                }
                2L -> when (exercise.difficulty) {
                    1L -> 22
                    2L -> 24
                    3L -> 25
                    4L -> 27
                    else -> null
                }
                3L -> when (exercise.difficulty) {
                    1L -> 28
                    2L -> 30
                    3L -> 35
                    4L -> 38
                    else -> null
                }
                4L -> when (exercise.difficulty) {
                    1L -> 40
                    2L -> 42
                    3L -> 45
                    4L -> 47
                    else -> null
                }
                5L -> when (exercise.difficulty) {
                    1L -> 50
                    2L -> 52
                    3L -> 54
                    4L -> 60
                    else -> null
                }
                else -> null
            }
        }
        else -> null
    }

}
