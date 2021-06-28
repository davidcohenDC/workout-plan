package com.example.workoutplan.data.exercise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

/**
 * The Data Access Object for the Exercise class.
 */
@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY name")
    fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise ORDER BY name")
    suspend fun getAll(): List<Exercise>

    @Update
    suspend fun update(exercise: Exercise)

    @Query("SELECT * FROM exercise WHERE exerciseId = :key")
    fun getExerciseById(key: Long): LiveData<Exercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exercise: Exercise)

    @Delete
    suspend fun delete(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY category = :categoryId DESC, muscle = :muscleId , difficulty = :difficultyId DESC")
    fun getAllFiltered(categoryId: Long, muscleId: Long, difficultyId: Long): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM exercise WHERE category = :categoryId AND  muscle = :muscleId AND difficulty = :difficultyId")
    fun getFilteredSize(categoryId: Long, muscleId: Long, difficultyId: Long): Int
}