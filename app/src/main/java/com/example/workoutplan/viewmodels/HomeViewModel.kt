package com.example.workoutplan.viewmodels

import androidx.lifecycle.*
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.repository.WorkoutRepository

class HomeViewModel(
        workoutRepository: WorkoutRepository
) : ViewModel() {

    val workouts = workoutRepository.getWorkouts()

    private val _selectedWorkout = MutableLiveData<Workout?>()

    private val _navigateToSetupActivity = MutableLiveData<Boolean?>()
    val navigateToSetupActivity: LiveData<Boolean?>
    get() = _navigateToSetupActivity

    private val _navigateToWorkoutExercisePage = MutableLiveData<Long?>()
    val navigateToWorkoutExercisePage : LiveData<Long?>
        get() = _navigateToWorkoutExercisePage

    fun onNavigateToWorkoutExercisePage(workout: Workout) {
        _navigateToWorkoutExercisePage.value = workout.workoutId
        _selectedWorkout.value = workout
    }

    fun navigateToWorkoutExercisePageDone() {
        _navigateToWorkoutExercisePage.value = null
    }

    fun onNavigateToSetupActivity() {
        _navigateToSetupActivity.value = true
    }
}