package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData

/**
 * Interface for all selectionViewModel
 */
interface SelectionViewModel{
    val nextButtonStatus: LiveData<Boolean>
    fun checkButton(): Boolean

}