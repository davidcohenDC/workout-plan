package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.DifficultyRepository
import com.example.workoutplan.viewmodels.SetupWorkoutDifficultyViewModel

class SetupWorkoutDifficultyViewModelFactory(
        private val repository: DifficultyRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SetupWorkoutDifficultyViewModel::class.java)) {
            return SetupWorkoutDifficultyViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}