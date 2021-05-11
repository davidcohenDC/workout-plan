package com.example.workoutplan.data.exercise

import androidx.lifecycle.LiveData

class ExerciseRepository(private val dao: ExerciseDao){

    fun getExercises(): LiveData<List<Exercise>> {
        return dao.getExercises()
    }

    suspend fun insert(exercise: Exercise) {
        dao.insert(exercise)
    }

    suspend fun delete(exercise: Exercise) {
        dao.delete(exercise)
    }

    suspend fun update(exercise: Exercise) {
        dao.update(exercise)
    }

    fun getExerciseById(exerciseId: Long): LiveData<Exercise> {
        return dao.getExerciseById(exerciseId)
    }

    suspend fun insertAll(exerciseList: List<Exercise>) {
        return dao.insertAll(exerciseList)
    }

}