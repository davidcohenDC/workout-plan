package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.data.exercise.ExerciseRepository
import kotlinx.coroutines.Job

class ExercisePageViewModel(
    repository: ExerciseRepository,
    exerciseId: Long
) : ViewModel(){

    private val viewModelJob = Job()

    private var exercise: LiveData<Exercise> = repository.getExerciseById(exerciseId)

    fun getExercise()= exercise

    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateBack.value = null
    }

    fun onClose() {
        _navigateBack. value = true
    }
}