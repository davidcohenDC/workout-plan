package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetupWorkoutTitleViewModel : ViewModel() {

    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    private val _workoutName = MutableLiveData<String>()
    val workoutName: String?
        get() = _workoutName.value

    private val _nextButtonEnabled = MutableLiveData<Boolean>()
    val nextButtonEnabled: LiveData<Boolean>
        get() = _nextButtonEnabled

    fun nextButtonEnable() {
        _nextButtonEnabled.value = true
    }

    fun nextButtonDisable() {
        _nextButtonEnabled.value = false
    }

    fun isNextButtonEnabled(): Boolean {
        return _nextButtonEnabled.value ?: false
    }

    fun doneNavigating() {
        _navigateToNext.value = null
    }

    fun onNavigateNext() {
        _navigateToNext.value = true
    }

    fun setWorkoutName(text : String) {
        _workoutName.value = text
    }

}