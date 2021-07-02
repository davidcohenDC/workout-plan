package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.CategoryDao
import com.example.workoutplan.data.entity.Category

class CategoryRepository(private val dao: CategoryDao) {

    fun getAllCategory(): LiveData<List<Category>> {
        return dao.getAllCategory()
    }

    suspend fun insert(category: Category) {
        dao.insert(category)
    }

    suspend fun insertAll(categoryList: List<Category>) {
        return dao.insertAll(categoryList)
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

}