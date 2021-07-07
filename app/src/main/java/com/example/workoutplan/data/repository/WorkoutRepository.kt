package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.WorkoutDao
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef
import com.example.workoutplan.serializable.WorkoutSetup

class WorkoutRepository(private val dao: WorkoutDao) {

    suspend fun insert(workout: Workout): Long {
        return dao.insertWithId(workout)
    }

    fun getWorkouts(): LiveData<List<Workout>> {
        return dao.getWorkouts()
    }

    fun getWorkoutById(workoutId: Long): LiveData<Workout> {
        return dao.getWorkoutById(workoutId)
    }

    fun getAll(): List<Workout> {
        return dao.getAll()
    }

    fun getWorkoutsSize(): LiveData<Int> {
        return dao.getWorkoutsSize()
    }

    suspend fun finalQuery(setup: WorkoutSetup, exerciseList: List<Exercise>) {
        return dao.finalQuery(exerciseList,setup)
    }

    suspend fun removeWorkout(workout: Workout) {
        return dao.removeWorkout(workout)
    }

    suspend fun insertWorkout(workoutExerciseCrossRef: WorkoutExerciseCrossRef) {
        dao.insertWorkout(workoutExerciseCrossRef)
    }

    suspend fun insertWorkoutBinded(workout: Workout, workoutList: List<WorkoutExerciseCrossRef>) {
        return dao.insertWorkoutBinded(workout, workoutList)
    }

    fun getWorkoutWithExercisesCrossById(workoutId: Long): LiveData<List<WorkoutExerciseCrossRef>> {
        return dao.getWorkoutWithExercisesCrossById(workoutId)
    }

    suspend fun insertAllWorkout(workoutExerciseCrossRefList: List<WorkoutExerciseCrossRef>) {
        dao.insertAllWorkout(workoutExerciseCrossRefList)
    }

}