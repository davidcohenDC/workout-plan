package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class SetupWorkoutTitleViewModel : ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Live Data for navigation to next fragment
     */
    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    /**
     * Live Data that keep the workout name String
     */
    private val _workoutName = MutableLiveData<String>()
    val workoutName: String
        get() = _workoutName.value ?: ""

    /**
     * Live Data keep the navigation status
     */
    private val _nextButtonStatus = MutableLiveData<Boolean>()
    val nextButtonStatus: LiveData<Boolean>
        get() = _nextButtonStatus

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }

    /**
     * Enable the possibility to navigate next
     */
    fun nextButtonEnable() {
        _nextButtonStatus.value = true
    }

    /**
     * Disable next the possibility to navigate next
     */
    fun nextButtonDisable() {
        _nextButtonStatus.value = false
    }

    /**
     * Check the possibility to navigate next
     */
    fun isNextButtonEnabled(): Boolean {
        return _nextButtonStatus.value ?: false
    }

    /**
     * When navigation is done
     */
    fun doneNavigating() {
        _navigateToNext.value = null
    }

    /**
     * This function is call when page navigated to the next fragment
     */
    fun onNavigateNext() {
        _navigateToNext.value = true
    }

    /**
     * Set the workout name
     */
    fun setWorkoutName(text : String) {
        _workoutName.value = text
    }

}