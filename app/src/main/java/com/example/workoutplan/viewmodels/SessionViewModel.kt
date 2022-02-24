package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.entity.Workout

class SessionViewModel: ViewModel() {

    private val _selectedWorkout = MutableLiveData<Workout?>()

    private val _navigateToEndSession = MutableLiveData<Boolean?>()
    val navigateToEndSession: LiveData<Boolean?>
        get() = _navigateToEndSession

    private val _navigateToWorkoutExercisePage = MutableLiveData<Long?>()
    val navigateToWorkoutExercisePage : LiveData<Long?>
        get() = _navigateToWorkoutExercisePage

    fun navigateToEndSessionPage(workout: Workout) {
        _navigateToEndSession.value = true
    }

    fun navigateToEndSessionPageDone() {
        _navigateToEndSession.value = null
    }

    fun onNavigateToWorkoutExercisePage(workout: Workout) {
        _navigateToWorkoutExercisePage.value = workout.workoutId
        _selectedWorkout.value = workout
    }
    fun navigateToWorkoutExercisePageDone() {
        _navigateToWorkoutExercisePage.value = null
    }

}