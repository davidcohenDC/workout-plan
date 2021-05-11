package com.example.workoutplan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.category.CategoryRepository
import java.lang.IllegalArgumentException

class SetupWorkoutCategoryViewModelFactory(
    private val repository: CategoryRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SetupWorkoutCategoryViewModel::class.java)) {
            return SetupWorkoutCategoryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}