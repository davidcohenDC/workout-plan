package com.example.workoutplan.data.muscle

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the Muscle class.
 */
@Dao
interface MuscleDao {

    @Query("SELECT * FROM muscle ORDER BY name")
    fun getAllCategory(): LiveData<List<Muscle>>

    @Update
    suspend fun update(muscle: Muscle)

    @Query("SELECT * FROM muscle WHERE muscleId = :key")
    fun getMuscleById(key: Long): LiveData<Muscle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(muscle: Muscle)

    @Delete
    suspend fun delete(muscle: Muscle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(muscle: List<Muscle>)
}