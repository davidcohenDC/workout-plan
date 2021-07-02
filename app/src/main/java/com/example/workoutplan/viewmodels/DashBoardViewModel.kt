package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashBoardViewModel : ViewModel() {

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
}