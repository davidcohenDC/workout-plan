package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.category.Category
import com.example.workoutplan.data.category.CategoryRepository
import java.util.logging.Logger

class SetupWorkoutCategoryViewModel(
    repository: CategoryRepository
) : ViewModel() {

    val category: LiveData<List<Category>> = repository.getAllCategory()

    private var _selectedCategory = MutableLiveData<Long?>()
    val selectedCategory: LiveData<Long?>
    get() = _selectedCategory

    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

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

    fun onSelectedCategory(category: Category) {


        if(_selectedCategory.value == category.categoryId) {
            _selectedCategory.value = null
        } else {
            _selectedCategory.value = category.categoryId
            Log.d("ViewModel", "${_selectedCategory.value} category ${category.categoryId}")
        }
    }


    fun isSelected(category: Category?): Boolean {
        category?.let {
            return category.categoryId == _selectedCategory.value
        }
        return false
    }


    fun onNavigateNext() {
        _navigateToNext.value = true
    }

}