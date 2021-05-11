package com.example.workoutplan.data.exercise

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the Exercise class.
 */
@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise ORDER BY name")
    fun getExercises(): LiveData<List<Exercise>>

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
}