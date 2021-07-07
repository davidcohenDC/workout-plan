package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.viewmodels.WorkoutExercisePageViewModel

class WorkoutExercisePageViewModelFactory(
    private val repository: WorkoutExerciseCrossRefRepository,
    private val workoutId: Long,
    private val exerciseId: Long,
    ) : ViewModelProvider.Factory {


        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorkoutExercisePageViewModel::class.java)) {
                return WorkoutExercisePageViewModel(repository, workoutId,exerciseId) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
}