package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.category.Category
import com.example.workoutplan.data.category.CategoryRepository
import kotlinx.coroutines.Job

class SetupWorkoutCategoryViewModel(
    repository: CategoryRepository
) : SelectionViewModel, ViewModel() {


    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob= Job()

    /**
     * Repo call to get all category
     */
    val categories: LiveData<List<Category>> = repository.getAllCategory()

    /**
     * Keep the selected categoryId
     */
    private var _selectedCategoryId = MutableLiveData<Long?>()
    val selectedCategoryId: LiveData<Long?>
    get() = _selectedCategoryId

    /**
     * Needed for navigation to next fragment
     */
    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    /**
     * Make possible the navigation to the next step
     */
    private val _nextButtonStatus = MutableLiveData<Boolean>()
    override val nextButtonStatus: LiveData<Boolean>
        get() = _nextButtonStatus

    var firstSelect = false

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }

    /**
     * Data Binding value
     */
    override fun checkButton(): Boolean {
        return _nextButtonStatus.value ?: false
    }

    /**
     * Keeps the live data selection.
     */
    fun onSelectedCategory(category: Category) {
        _selectedCategoryId.value = category.categoryId
        Log.d("ViewModel", "${_selectedCategoryId.value} category ${category.categoryId}")
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

}