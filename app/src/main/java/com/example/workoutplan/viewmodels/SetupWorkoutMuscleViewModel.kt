package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.workoutplan.adapters.SelectionItem
import com.example.workoutplan.data.entity.Muscle
import com.example.workoutplan.data.repository.MuscleRepository
import kotlinx.coroutines.Job

class SetupWorkoutMuscleViewModel(
        repository: MuscleRepository,
) : SelectionViewModel, ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Repo call to get all muscle
     */
    val muscles: LiveData<List<Muscle>> = repository.getAllMuscle()

    private var _nextButtonStatus = MutableLiveData<Boolean>()
    override val nextButtonStatus: LiveData<Boolean>
        get() = _nextButtonStatus

    private val _selectedMuscleId = MutableLiveData<Long?>()
    override val selectedId: LiveData<Long?>
        get() = _selectedMuscleId

    private val _navigateToNext = MutableLiveData<Boolean?>()
    override val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    override fun checkButton(): Boolean {
        return _nextButtonStatus.value ?: false
    }

    override fun onSelection(selection: SelectionItem) {
        _selectedMuscleId.value = selection.id
        Log.d("ViewModel", "${_selectedMuscleId.value} muscle ${selection.id}")
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