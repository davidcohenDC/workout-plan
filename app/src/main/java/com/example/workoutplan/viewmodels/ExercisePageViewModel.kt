package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.repository.ExerciseRepository
import kotlinx.coroutines.Job

class ExercisePageViewModel(
        repository: ExerciseRepository,
        exerciseId: Long,
) : ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Repo call to get actual exercise
     */
    val exercise: LiveData<Exercise> = repository.getExerciseById(exerciseId)

    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onNavigateBack() {
        _navigateBack.value = true
    }

    /**
     * When navigation is done
     */
    fun doneNavigating() {
        _navigateBack.value = null
    }
}