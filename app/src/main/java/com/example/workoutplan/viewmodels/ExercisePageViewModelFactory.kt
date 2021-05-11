package com.example.workoutplan.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.exercise.ExerciseRepository
import java.lang.IllegalArgumentException


class ExercisePageViewModelFactory(
    private val repository: ExerciseRepository,
    private val exerciseId: Long
): ViewModelProvider.Factory {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ExercisePageViewModel::class.java)) {
            return ExercisePageViewModel(repository, exerciseId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}