package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutplan.data.entity.Category

/**
 * The Data Access Object for the Category class.
 */
@Dao
abstract class CategoryDao : BaseDao<Category> {

    @Query("SELECT * FROM category ORDER BY categoryId")
    abstract fun getAllCategory(): LiveData<List<Category>>

    @Query("SELECT * FROM category WHERE categoryId = :key ORDER BY categoryId")
    abstract fun getCategoryById(key: Long): LiveData<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(category: List<Category>)

}