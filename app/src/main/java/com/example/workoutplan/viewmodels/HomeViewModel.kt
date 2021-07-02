package com.example.workoutplan.viewmodels

import androidx.lifecycle.ViewModel
import com.example.workoutplan.data.repository.WorkoutRepository

class HomeViewModel(
        repository: WorkoutRepository,
) : ViewModel() {

    var workoutSize = repository.getWorkoutsSize()


}