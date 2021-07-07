package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.adapters.items.SelectionItem
import com.example.workoutplan.data.entity.Difficulty
import com.example.workoutplan.data.repository.DifficultyRepository
import kotlinx.coroutines.Job

class SetupWorkoutDifficultyViewModel(
        repository: DifficultyRepository,
) : SelectionViewModel, ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Repo call to get all difficulty
     */
    val difficulties: LiveData<List<Difficulty>> = repository.getAllDifficulty()

    private var _selectedDifficultyId = MutableLiveData<Long?>()
    override val selectedId: LiveData<Long?>
        get() = _selectedDifficultyId

    private val _navigateToNext = MutableLiveData<Boolean?>()
    override val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    private val _nextButtonStatus = MutableLiveData<Boolean>()
    override val nextButtonStatus: LiveData<Boolean>
        get() = _nextButtonStatus


    override fun checkButton(): Boolean {
        return _nextButtonStatus.value ?: false
    }


    override fun onSelection(selection: SelectionItem) {
        _selectedDifficultyId.value = selection.id
        Log.d("ViewModel", "${_selectedDifficultyId.value} difficulty ${selection.id}")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
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