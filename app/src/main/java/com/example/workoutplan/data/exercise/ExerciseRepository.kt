package com.example.workoutplan.data.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.workoutplan.data.CategoryWIthExercises
import com.example.workoutplan.serializable.WorkoutSetup

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

    fun getAllFiltered(workoutSetup: WorkoutSetup) : LiveData<MutableList<Exercise>> {
        return dao.getAllFiltered(workoutSetup.category,workoutSetup.category,workoutSetup.difficulty)
    }


     fun getFilteredSize(workoutSetup: WorkoutSetup): Int {
        return dao.getFilteredSize(workoutSetup.category,workoutSetup.category,workoutSetup.difficulty)
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
}