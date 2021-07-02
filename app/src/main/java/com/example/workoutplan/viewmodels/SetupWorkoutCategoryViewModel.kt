package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.adapters.SelectionItem
import com.example.workoutplan.data.entity.Category
import com.example.workoutplan.data.repository.CategoryRepository
import kotlinx.coroutines.Job

class SetupWorkoutCategoryViewModel(
        repository: CategoryRepository,
) : SelectionViewModel, ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Repo call to get all category (un mutable)
     */
    val categories: LiveData<List<Category>> = repository.getAllCategory()


    private var _selectedCategoryId = MutableLiveData<Long?>()
    override val selectedId: LiveData<Long?>
        get() = _selectedCategoryId

    private val _navigateToNext = MutableLiveData<Boolean?>()
    override val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    private val _nextButtonStatus = MutableLiveData<Boolean>()
    override val nextButtonStatus: LiveData<Boolean>
        get() = _nextButtonStatus

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }

    override fun checkButton(): Boolean {
        return _nextButtonStatus.value ?: false
    }

    override fun onSelection(selection: SelectionItem) {
        _selectedCategoryId.value = selection.id
        Log.d("ViewModel", "${_selectedCategoryId.value} category ${selection.id}")
    }

    override fun onNavigateNext() {
        _navigateToNext.value = true
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
}