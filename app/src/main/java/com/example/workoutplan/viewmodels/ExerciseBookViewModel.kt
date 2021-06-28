package com.example.workoutplan.viewmodels

import androidx.lifecycle.*
import com.example.workoutplan.data.exercise.Exercise
import com.example.workoutplan.data.exercise.ExerciseRepository
import com.example.workoutplan.serializable.WorkoutSetup
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExerciseBookViewModel(
    private val repository: ExerciseRepository,
    workoutSetup: WorkoutSetup
) : ViewModel(){

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Used to restore the exercises
     */
    private var exercisesBackup: List<Exercise>? = repository.getAllFiltered(workoutSetup).value

    /**
     * Keep al data of exercises
     */
    val exercisesBook: LiveData<MutableList<Exercise>> = repository.getAllFiltered(workoutSetup)

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

    /**
     * Keep the exercisesId in a list
     */
    private val _exercisesSelected = MutableLiveData<MutableList<Exercise>>()
    val exercisesSelected: LiveData<MutableList<Exercise>>
        get() = _exercisesSelected

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
                 it.add(exercise)

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

    fun removeFromSelection(exercise: Exercise) {
        _exercisesSelected.value?.let {
            it.remove(exercise)
        }
        viewModelScope.launch {
            repository.insert(exercise)
        }
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

    fun getItemSelectedId(): List<Long> {
        _exercisesSelected.value?.let { list ->
            return list.map { it.exerciseId }.toList()
        }
        return listOf()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }
}
