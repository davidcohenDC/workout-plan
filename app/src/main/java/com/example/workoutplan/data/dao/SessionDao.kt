package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutplan.data.entity.Session

/**
 * The Data Access Object for the Session class.
 */
@Dao
abstract class SessionDao: BaseDao<Session> {

    @Query("SELECT * FROM session")
    abstract suspend fun getAll(): List<Session>

    @Query("SELECT * FROM session")
    abstract fun getSessions(): LiveData<List<Session>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun insertAll(sessions: List<Session>)

    @Query("SELECT * FROM session WHERE sessionId = :key")
    abstract fun getSessionById(key: Long): LiveData<Session>
}