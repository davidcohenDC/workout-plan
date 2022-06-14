package com.example.workoutplan.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.workoutplan.data.repository.SessionRepository
import com.example.workoutplan.viewmodels.SessionSummaryViewModel
class SummaryViewModelFactory(
    private val repository: SessionRepository,
    private val sessionId: Long
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SessionSummaryViewModel::class.java)) {
            return SessionSummaryViewModel(repository, sessionId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}