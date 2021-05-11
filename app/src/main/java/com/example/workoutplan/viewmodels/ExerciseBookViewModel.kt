package com.example.workoutplan.viewmodels

import androidx.lifecycle.*
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.data.exercise.ExerciseRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExerciseBookViewModel(
    private val repository: ExerciseRepository
) : ViewModel(){

    private val viewModelJob = Job()

    /**
     * Used to restore the exercises
     */
    private var exercisesBackup: List<Exercise>? = repository.getExercises().value

    /**
     * Needed for navigation into ExercisePageFragment
     */
    private val _navigateToExercisePage = MutableLiveData<Long?>()
    val navigateToExercisePage: LiveData<Long?>
        get() = _navigateToExercisePage

    /**
     * Make possible the navigation to the next step
     */
    private var _nextButtonEnable = MutableLiveData<Boolean>()
    val nextButtonEnable: LiveData<Boolean>
        get() = _nextButtonEnable

    private val _exercisesSelected = MutableLiveData<MutableList<Long>>()
    val exercisesSelected: LiveData<MutableList<Long>>
        get() = _exercisesSelected

    /**
     * Stored the visual exercises
     */
    val exercisesBook: LiveData<List<Exercise>> = repository.getExercises()


    init {
        _exercisesSelected.value = mutableListOf()
    }

     fun onAddItem(exercise: Exercise) {

         //If the list is empty make a backup
         _exercisesSelected.value?.let {
             if(it.isEmpty()) {
                 exercisesBackup = exercisesBook.value
             }
         }

         exercisesBook.value?.let {
             _exercisesSelected.value?.let {
                 it.add(exercise.exerciseId)
                 viewModelScope.launch {
                     repository.delete(exercise)
                 }
             }
         }
    }


    /**
     * Used in xml to check if enable or not
     */
    fun navigable(): Boolean {
        return _nextButtonEnable.value ?: false
    }

    /**
     * Restore exercises and clear the selected exercises
     */
    fun onReset() {
        viewModelScope.launch {
            exercisesBackup?.let { repository.insertAll(it) }
        }

        _exercisesSelected.value?.clear()
    }

    /**
     * get ExerciseSelectedSize
     */
    fun getExerciseSelectedSize() = _exercisesSelected.value?.size ?: 0

    /**
     * Check if possible to navigate to the next fragment
     */
    fun isNavigable() {
        _nextButtonEnable.value = _exercisesSelected.value?.size ?: 0 > 2
    }

    /**
     * When page clicked
     * @param id to pass for the ExercisePageFragment
     */
    fun onExerciseItemClicked(id:Long) {
        _navigateToExercisePage.value = id
    }

    /**
     * When page navigated make navigation null
     */
    fun onExerciseItemNavigated() {
        _navigateToExercisePage.value = null
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }


}

