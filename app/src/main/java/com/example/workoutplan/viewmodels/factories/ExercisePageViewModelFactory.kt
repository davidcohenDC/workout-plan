package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.viewmodels.ExercisePageViewModel


class ExercisePageViewModelFactory(
        private val repository: ExerciseRepository,
        private val exerciseId: Long,
) : ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel > create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExercisePageViewModel::class.java)) {
            return ExercisePageViewModel(repository, exerciseId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}