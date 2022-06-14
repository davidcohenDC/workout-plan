package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.viewmodels.SessionExercisePageViewModel

class SessionExercisePageViewModelFactory(
    private val repository: ExerciseRepository,
    private val exerciseId: Long,
    private val workoutId: Long,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionExercisePageViewModel::class.java)) {
            return SessionExercisePageViewModel(repository,exerciseId,workoutId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}