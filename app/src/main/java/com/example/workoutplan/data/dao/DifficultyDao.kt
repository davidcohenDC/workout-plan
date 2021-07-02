package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutplan.data.entity.Difficulty

/**
 * The Data Access Object for the Category class.
 */
@Dao
abstract class DifficultyDao : BaseDao<Difficulty> {

    @Query("SELECT * FROM difficulty ORDER BY difficultyId")
    abstract fun getAllDifficulty(): LiveData<List<Difficulty>>

    @Query("SELECT * FROM difficulty WHERE difficultyId = :key")
    abstract fun getDifficultyById(key: Long): LiveData<Difficulty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(difficulty: List<Difficulty>)
}