package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.viewmodels.DashBoardViewModel

class DashBoardViewModelFactory(
private val workoutRepository: WorkoutRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashBoardViewModel::class.java)) {
            return DashBoardViewModel(workoutRepository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}