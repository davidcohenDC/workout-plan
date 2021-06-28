package com.example.workoutplan.data.category

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workoutplan.data.CategoryWIthExercises
import com.example.workoutplan.data.category.Category

/**
 * The Data Access Object for the Category class.
 */
@Dao
interface CategoryDao {

    @Query("SELECT * FROM category ORDER BY categoryId")
    fun getAllCategory(): LiveData<List<Category>>

    @Update
    suspend fun update(category: Category)

    @Query("SELECT * FROM category WHERE categoryId = :key ORDER BY categoryId")
    fun getCategoryById(key: Long): LiveData<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Delete
    suspend fun delete(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(category: List<Category>)

}