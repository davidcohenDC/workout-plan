package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.viewmodels.HomeViewModel

class HomeViewModelFactory(
        private val workoutRepository: WorkoutRepository,
        private val sessionRepository: SessionRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(workoutRepository,sessionRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}