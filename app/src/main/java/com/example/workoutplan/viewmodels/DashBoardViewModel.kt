package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.repository.WorkoutRepository
import kotlinx.coroutines.Job

class DashBoardViewModel(
    workoutRepository: WorkoutRepository
) : ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    val workouts = workoutRepository.getWorkouts()

    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    fun onNavigateNext() {
        _navigateToNext.value = true
    }

    /**
     * When navigation is done
     */
    fun doneNavigating() {
        _navigateToNext.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }
}