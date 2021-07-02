package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.WorkoutDao
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef
import com.example.workoutplan.data.relations.WorkoutWithExercises

class WorkoutRepository(private val dao: WorkoutDao) {

    suspend fun insert(workout: Workout): Long {
        return dao.insertWithId(workout)
    }

    fun getWorkouts(): LiveData<List<Workout>> {
        return dao.getWorkouts()
    }

    fun getAll(): List<Workout> {
        return dao.getAll()
    }

    fun getWorkoutsSize(): LiveData<Int> {
        return dao.getWorkoutsSize()
    }

    suspend fun insertWorkout(workoutExerciseCrossRef: WorkoutExerciseCrossRef) {
        dao.insertWorkout(workoutExerciseCrossRef)
    }

    suspend fun insertWorkoutBinded(workout: Workout, workoutList: List<WorkoutExerciseCrossRef>) {
        return dao.insertWorkoutBinded(workout, workoutList)
    }

    fun getWorkoutWithExercises(): List<WorkoutWithExercises> {
        return dao.getWorkoutWithExercises()
    }

    suspend fun getWorkoutWithExercisesCrossById(workoutId: Long): List<WorkoutExerciseCrossRef> {
        return dao.getWorkoutWithExercisesCrossById(workoutId)
    }


    suspend fun insertAllWorkout(workoutExerciseCrossRefList: List<WorkoutExerciseCrossRef>) {
        dao.insertAllWorkout(workoutExerciseCrossRefList)
    }

}