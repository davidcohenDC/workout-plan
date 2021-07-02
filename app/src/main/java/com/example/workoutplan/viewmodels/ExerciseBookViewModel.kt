package com.example.workoutplan.viewmodels

import androidx.lifecycle.*
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.data.repository.WorkoutRepository
import com.example.workoutplan.serializable.QueryFilter
import com.example.workoutplan.serializable.WorkoutSetup
import com.example.workoutplan.utilities.EXERCISE_LIMIT
import com.example.workoutplan.utilities.configureWorkout
import com.example.workoutplan.utilities.workoutSetupToWorkout
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExerciseBookViewModel(
        private val exerciseRepository: ExerciseRepository,
        private val workoutRepository: WorkoutRepository,
        val workoutSetup: WorkoutSetup,
) : ViewModel() {

    /**
     * Used to clean the job in on cleared state
     */
    private val viewModelJob = Job()

    /**
     * Used to restore the exercises
     */
    private var exercisesBackup: List<Exercise>? = exerciseRepository.getAllFiltered(workoutSetup).value


    /**
     * Used to change filter query that map exercisesBook
     */
    private val filter = MutableLiveData(QueryFilter.ALL)

    /**
     * Keep al data of exercises
     */
    val exercisesBook = Transformations.switchMap(filter) { fil ->
        when (fil) {
            QueryFilter.ALL -> exerciseRepository.getAllFiltered(workoutSetup)
            QueryFilter.MUSCLE -> exerciseRepository.getFilteredWithMuscle()
            QueryFilter.ALPHABETIC -> exerciseRepository.getFilteredWithAlphabetic()
            QueryFilter.DIFFICULTY -> exerciseRepository.getFilteredWithDifficulty()
            null -> exerciseRepository.getAllFiltered(workoutSetup)
        }
    }

    /**
     * Needed for navigation into ExercisePageFragment
     */
    private val _navigateToExercisePage = MutableLiveData<Long?>()
    val navigateToExercisePage: LiveData<Long?>
        get() = _navigateToExercisePage

    private val _navigateToNext = MutableLiveData<Boolean?>()
    val navigateNext: LiveData<Boolean?>
        get() = _navigateToNext

    /**
     * Make possible the navigation to the next step
     */
    private var _nextButtonEnable = MutableLiveData<Boolean>()
    val nextButtonEnable: LiveData<Boolean>
        get() = _nextButtonEnable

    /**
     * Keep the exercisesId in a list
     */
    private val _exercisesSelected = MutableLiveData<List<Exercise>>()
    val exercisesSelected: LiveData<List<Exercise>>
        get() = _exercisesSelected

    init {
        _exercisesSelected.value = mutableListOf()
    }

    /**
     * Add item to selectionItem list and remove it from persistent data
     */
    fun onAddItem(exercise: Exercise) {

        //If the list is empty make a backup
        _exercisesSelected.value?.let {
            if (it.isEmpty()) {
                exercisesBackup = exercisesBook.value
            }
        }

        exercisesBook.value?.let { _ ->
            viewModelScope.launch {
                exerciseRepository.delete(exercise)
            }

            _exercisesSelected.value?.plus(exercise).let {
                _exercisesSelected.value = it
            }
        }
    }

    /**
     * Restore exercises and clear the selected exercises
     */
    fun onReset() {
        viewModelScope.launch {
            exercisesBackup?.let { exerciseRepository.insertAll(it) }
        }

        _exercisesSelected.value = listOf()
    }

    fun removeFromSelection(exercise: Exercise) {
        _exercisesSelected.value?.let {

            _exercisesSelected.value?.minus(exercise).let {
                _exercisesSelected.value = it
            }
        }

        viewModelScope.launch {
            exerciseRepository.insert(exercise)
        }
    }

    /**
     * Used in xml to check if enable or not
     */
    fun navigable(): Boolean {
        return _nextButtonEnable.value ?: false
    }

    /**
     * get ExerciseSelectedSize
     */
    fun getExerciseSelectedSize() = _exercisesSelected.value?.size ?: 0

    /**
     * Check if possible to navigate to the next fragment
     */
    fun isNavigable() {
        _nextButtonEnable.value = _exercisesSelected.value?.size ?: 0 >= EXERCISE_LIMIT
    }

    /**
     * When page clicked
     * @param id to pass for the ExercisePageFragment
     */
    fun onExerciseItemClicked(id: Long) {
        _navigateToExercisePage.value = id
    }

    /**
     * When page navigated make navigation null
     */
    fun onExerciseItemNavigated() {
        _navigateToExercisePage.value = null
    }

    /**
     * Get listItemSelectedId
     */
    fun getListItemSelectedId(): List<Long> {
        _exercisesSelected.value?.let { list ->
            return list.map { it.exerciseId }.toList()
        }
        return listOf()
    }

    /**
     * If change the filter the viewModel call the repo to make a new filter query
     */
    fun changeFilter(fil: QueryFilter) {
        filter.value = fil
    }

    fun onNavigateNext() {
        _navigateToNext.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }

    fun createNewWorkout() {

        viewModelScope.launch {
            workoutRepository.insert(workoutSetupToWorkout(workoutSetup)).apply {
                _exercisesSelected.value?.let { it ->
                    workoutRepository.insertAllWorkout(configureWorkout(this, workoutSetup, it))
                }
            }
        }
    }
}