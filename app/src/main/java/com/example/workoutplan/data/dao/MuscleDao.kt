package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutplan.data.entity.Muscle

/**
 * The Data Access Object for the Muscle class.
 */
@Dao
abstract class MuscleDao : BaseDao<Muscle> {

    @Query("SELECT * FROM muscle ORDER BY muscleId")
    abstract fun getAllCategory(): LiveData<List<Muscle>>

    @Query("SELECT * FROM muscle ORDER BY muscleId")
    abstract fun getAll(): List<Muscle>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(muscle: List<Muscle>)

    @Query("SELECT * FROM muscle WHERE muscleId = :key")
    abstract fun getMuscleById(key: Long): LiveData<Muscle>


}