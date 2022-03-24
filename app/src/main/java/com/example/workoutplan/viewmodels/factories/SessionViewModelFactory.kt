package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.viewmodels.SessionViewModel
import java.lang.IllegalArgumentException

class SessionViewModelFactory(
    private val workoutExeRepository: WorkoutExerciseCrossRefRepository,
    private val workoutId: Long
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SessionViewModel::class.java)) {
            return SessionViewModel(workoutExeRepository,workoutId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}