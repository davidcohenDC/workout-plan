package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.muscle.MuscleRepository
import com.example.workoutplan.viewmodels.SetupWorkoutMuscleViewModel
import java.lang.IllegalArgumentException

class SetupWorkoutMuscleViewModelFactory(
    private val repository: MuscleRepository
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SetupWorkoutMuscleViewModel::class.java)) {
                return SetupWorkoutMuscleViewModel(repository) as T
            } else {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }