package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.adapters.items.SessionItem
import com.example.workoutplan.data.entity.Workout

class SessionViewModel: ViewModel() {

    private val _selectedWorkout = MutableLiveData<Workout?>()


    var testSet: MutableLiveData<List<SessionItem>> = MutableLiveData()

    private val _status = MutableLiveData<STATUS>()
    val status: LiveData<STATUS?>
        get() = _status

    init {
        val sessionItem1 = SessionItem(1,2,45,null,null,SessionItem.Companion.STATUS.DONE)
        val sessionItem2 = SessionItem(1,2,45,null,20,SessionItem.Companion.STATUS.DOING)
        val sessionItem3 = SessionItem(1,2,46,500,null,SessionItem.Companion.STATUS.UNDONE)
        val sessionItem4 = SessionItem(1,2,46,300,null,SessionItem.Companion.STATUS.UNDONE)
        testSet.value = listOf(sessionItem1,sessionItem2,sessionItem3,sessionItem4)
    }

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

    fun toggleButton() {
        if(_status.value == STATUS.PAUSED) {
            _status.value = STATUS.RUNNING
        } else {
            _status.value = STATUS.PAUSED
        }
    }

    companion object {
        enum class STATUS {
            PAUSED,RUNNING
        }
    }

}