package com.example.workoutplan.viewmodels

import androidx.lifecycle.LiveData
import com.example.workoutplan.adapters.items.SelectionItem

/**
 * Interface for all selectionViewModel
 */
interface SelectionViewModel {

    /**
     * live data that make possible the navigation to the next step
     */
    val nextButtonStatus: LiveData<Boolean>

    /**
     * keep the selected live data Id
     */
    val selectedId: LiveData<Long?>

    /**
     * live data needed for navigation to next fragment
     */
    val navigateNext: LiveData<Boolean?>

    /**
     * Data Binding value
     */
    fun checkButton(): Boolean

    /**
     * Keeps the live data selection.
     */
    fun onSelection(selection: SelectionItem)

    /**
     * This function is call when page navigated to the next fragment
     */
    fun onNavigateNext()

}