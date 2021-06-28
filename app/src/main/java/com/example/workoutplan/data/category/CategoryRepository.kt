package com.example.workoutplan.data.category

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.CategoryWIthExercises
import com.example.workoutplan.data.category.Category
import com.example.workoutplan.data.category.CategoryDao

class CategoryRepository(private val dao: CategoryDao){

    fun getAllCategory(): LiveData<List<Category>> {
        return dao.getAllCategory()
    }

    suspend fun insert(category: Category) {
        dao.insert(category)
    }

    suspend fun delete(category: Category) {
        dao.delete(category)
    }

    suspend fun update(category: Category) {
        dao.update(category)
    }

    fun getCategoryById(categoryId: Long): LiveData<Category> {
        return dao.getCategoryById(categoryId)
    }

    suspend fun insertAll(categoryList: List<Category>) {
        return dao.insertAll(categoryList)
    }


}