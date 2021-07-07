package com.example.workoutplan.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.relations.ExerciseDetailed
import com.example.workoutplan.data.repository.ExerciseRepository
import com.example.workoutplan.data.repository.WorkoutExerciseCrossRefRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WorkoutExercisePageViewModel(
        private val repository: WorkoutExerciseCrossRefRepository,
        private val workoutId: Long,
        private val exerciseId: Long,
    ) : ViewModel() {

    private val viewModelJob = Job()

    /**
     * Repo call to get actual exercise
     */
    val exercise: LiveData<ExerciseDetailed> = repository.getExerciseDetailedBydId(workoutId,exerciseId)


    private val _navigateBack = MutableLiveData<Boolean?>()
    val navigateBack: LiveData<Boolean?>
        get() = _navigateBack

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        Log.d("aiuto", "$workoutId + $exerciseId")
    }

    fun onNavigateBack() {
        _navigateBack.value = true
    }

    fun saveNewWorkoutDetail(set: Int, rep: Int, duration:Int) {
        exercise.value?.let {
            when(it.category) {
                1L -> viewModelScope.launch { repository.updateRepAndSet(workoutId,exerciseId,set,rep) }
                2L -> viewModelScope.launch { repository.updateDuration(workoutId,exerciseId,duration) }
                3L ->  viewModelScope.launch { repository.updateDuration(workoutId,exerciseId,duration) }
                else -> null
            }
        }
    }

    /**
     * When navigation is done
     */
    fun doneNavigating() {
        _navigateBack.value = null
    }

    }