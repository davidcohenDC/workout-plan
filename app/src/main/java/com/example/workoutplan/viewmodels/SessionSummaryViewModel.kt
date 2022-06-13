package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.repository.SessionRepository
import kotlinx.coroutines.Job

class SessionSummaryViewModel(
    sessionRepository: SessionRepository
): ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    val session = sessionRepository.getLast()


    /**
     * Livedata to navigate to SummaryPage
     */
    private val _navigateToHome = MutableLiveData<Boolean?>()
    val navigateToHome: LiveData<Boolean?>
        get() = _navigateToHome


    fun onNavigateToHomePage() {
        _navigateToHome.value = true
    }

    fun navigateToHomePageDone() {
        _navigateToHome.value = null
    }


}