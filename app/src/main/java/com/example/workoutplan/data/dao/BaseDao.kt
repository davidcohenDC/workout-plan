package com.example.workoutplan.data.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface BaseDao<E> {

    @Update
    suspend fun update(obj: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: E)

    @Delete
    suspend fun delete(obj: E)
}