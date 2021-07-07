package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.ExerciseDao
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.serializable.WorkoutSetup

class ExerciseRepository(private val dao: ExerciseDao) {

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

    fun getAllFiltered(workoutSetup: WorkoutSetup): LiveData<MutableList<Exercise>> {
        return dao.getAllFiltered(workoutSetup.category, workoutSetup.category, workoutSetup.difficulty)
    }

    fun getFilteredWithDifficulty(): LiveData<MutableList<Exercise>> {
        return dao.getFilteredWithDifficulty()
    }

    fun getFilteredWithMuscle(): LiveData<MutableList<Exercise>> {
        return dao.getFilteredWithMuscle()
    }

    fun getFilteredWithAlphabetic(): LiveData<MutableList<Exercise>> {
        return dao.getFilteredWithCategory()
    }

    fun getExercisesByWorkoutId(workoutId: Long): LiveData<List<Exercise>> {
        return dao.getExercisesByWorkoutId(workoutId)
    }

}