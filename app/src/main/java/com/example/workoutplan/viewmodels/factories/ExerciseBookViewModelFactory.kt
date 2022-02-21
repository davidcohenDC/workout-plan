package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.serializable.WorkoutSetup
import com.example.workoutplan.viewmodels.ExerciseBookViewModel

class ExerciseBookViewModelFactory(
        private val exerciseRepository: ExerciseRepository,
        private val workoutRepository: WorkoutRepository,
        private val workoutSetup: WorkoutSetup,
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExerciseBookViewModel::class.java)) {
            return ExerciseBookViewModel(exerciseRepository, workoutRepository, workoutSetup) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}