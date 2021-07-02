package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workoutplan.data.entity.Exercise

/**
 * The Data Access Object for the Exercise class.
 */
@Dao
abstract class ExerciseDao : BaseDao<Exercise> {

    @Query("SELECT * FROM exercise ORDER BY name")
    abstract suspend fun getAll(): List<Exercise>

    @Query("SELECT * FROM exercise ORDER BY name")
    abstract fun getExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercise WHERE exerciseId = :key")
    abstract fun getExerciseById(key: Long): LiveData<Exercise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(exercises: List<Exercise>)

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY category = :categoryId DESC, muscle = :muscleId DESC, difficulty = :difficultyId DESC")
    abstract fun getAllFiltered(categoryId: Long, muscleId: Long, difficultyId: Long): LiveData<MutableList<Exercise>>

    @Query("SELECT * FROM exercise WHERE category = :categoryId AND  muscle = :muscleId AND difficulty = :difficultyId")
    abstract fun getFilteredSize(categoryId: Long, muscleId: Long, difficultyId: Long): Int

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY difficulty ASC")
    abstract fun getFilteredWithDifficulty(): LiveData<MutableList<Exercise>>

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY  muscle ASC")
    abstract fun getFilteredWithMuscle(): LiveData<MutableList<Exercise>>

    @Transaction
    @Query("SELECT * FROM exercise ORDER BY category")
    abstract fun getFilteredWithCategory(): LiveData<MutableList<Exercise>>

}