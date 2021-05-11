package com.example.workoutplan.data.difficulty

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the Category class.
 */
@Dao
interface DifficultyDao {

    @Query("SELECT * FROM difficulty ORDER BY name")
    fun getAllDifficulty(): LiveData<List<Difficulty>>

    @Update
    suspend fun update(difficulty: Difficulty)

    @Query("SELECT * FROM difficulty WHERE difficultyId = :key")
    fun getDifficultyById(key: Long): LiveData<Difficulty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(difficulty: Difficulty)

    @Delete
    suspend fun delete(difficulty: Difficulty)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(difficulty: List<Difficulty>)
}