package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.data.repository.WorkoutRepository

class HomeViewModel(
        workoutRepository: WorkoutRepository,
        sessionRepository: SessionRepository,
) : ViewModel() {

    val workouts = workoutRepository.getWorkouts()

    val sessions = sessionRepository.getSessions()

    private val _selectedWorkout = MutableLiveData<Workout?>()

    private val _navigateToSetupActivity = MutableLiveData<Boolean?>()
    val navigateToSetupActivity: LiveData<Boolean?>
    get() = _navigateToSetupActivity

    private val _navigateToWorkoutExercisePage = MutableLiveData<Long?>()
    val navigateToWorkoutExercisePage : LiveData<Long?>
        get() = _navigateToWorkoutExercisePage

    private val _navigateTSummaryPage = MutableLiveData<Long?>()
    val navigateTSummaryPage : LiveData<Long?>
        get() = _navigateTSummaryPage

    fun onNavigateToWorkoutExercisePage(workout: Workout) {
        _navigateToWorkoutExercisePage.value = workout.workoutId
        _selectedWorkout.value = workout
    }

    fun onNavigateToSummaryPageDone() {
        _navigateTSummaryPage.value = null
    }
    fun onNavigateToSummaryPage(session: Session) {
        _navigateTSummaryPage.value = session.sessionId
    }

    fun navigateToWorkoutExercisePageDone() {
        _navigateToWorkoutExercisePage.value = null
    }

    fun onNavigateToSetupActivity() {
        _navigateToSetupActivity.value = true
    }
}