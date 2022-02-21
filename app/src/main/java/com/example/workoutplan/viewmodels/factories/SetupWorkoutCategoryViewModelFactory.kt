package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.CategoryRepository
import com.example.workoutplan.viewmodels.SetupWorkoutCategoryViewModel

class SetupWorkoutCategoryViewModelFactory(
        private val repository: CategoryRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetupWorkoutCategoryViewModel::class.java)) {
            return SetupWorkoutCategoryViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}