package com.example.workoutplan.viewmodels

import androidx.lifecycle.*
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutExercisesViewModel(
        private val workoutRepository: WorkoutRepository,
        private val workoutExeRepository: WorkoutExerciseCrossRefRepository,
        workoutId: Long
) : ViewModel() {

    private val _selectedWorkout = workoutRepository.getWorkoutById(workoutId)
    val selectedWorkout: LiveData<Workout>
        get() = _selectedWorkout

    val exercisesDetailed = Transformations.switchMap(_selectedWorkout) {
        when(it) {
            null -> null
            else -> workoutExeRepository.getExercisesDetailedByWorkoutId(it.workoutId)
        }
    }

    private val _navigateToWorkoutExercisePage = MutableLiveData<Long?>()
    val navigateToWorkoutExercisePage : LiveData<Long?>
        get() = _navigateToWorkoutExercisePage

    private val _navigateToSessionWorkoutPage = MutableLiveData<Long?>()
    val navigateToSessionWorkoutPage: LiveData<Long?>
        get() = _navigateToSessionWorkoutPage

    fun deleteWorkout() {
        _selectedWorkout.value?.let {
            viewModelScope.launch {
                workoutRepository.removeWorkout(it)
            }
        }
    }

    fun onExercisePage(exercise: ExerciseDetailed) {
        _navigateToWorkoutExercisePage.value = exercise.exerciseId
    }

    fun exercisePageDone() {
        _navigateToWorkoutExercisePage.value = null
    }

    fun onSessionPage() {
        _navigateToSessionWorkoutPage.value = selectedWorkout.value?.workoutId
    }

    fun onSessionPageDone() {
        _navigateToSessionWorkoutPage.value = null
    }

}