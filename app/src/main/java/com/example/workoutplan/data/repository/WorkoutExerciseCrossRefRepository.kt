package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.WorkoutExerciseCrossRefDao
import com.example.workoutplan.data.relations.ExerciseDetailed

class WorkoutExerciseCrossRefRepository(private val dao: WorkoutExerciseCrossRefDao) {

    fun getExercisesDetailedByWorkoutId(workoutId: Long): LiveData<List<ExerciseDetailed>> {
        return dao.getExercisesDetailedByWorkoutId(workoutId)
    }

    fun getExerciseDetailedBydId(workoutId: Long, exerciseId: Long): LiveData<ExerciseDetailed> {
        return dao.getExerciseDetailedBydId(workoutId,exerciseId)
    }

    fun getExerciseDetailedRepetitionBydId(workoutId: Long, exerciseId: Long): LiveData<Int> {
        return dao.getExerciseDetailedRepetitionBydId(workoutId,exerciseId)
    }

/*    fun getExerciseDetailedSetBydId(workoutId: Long, exerciseId: Long): LiveData<Int> {
        return dao.gete(workoutId,exerciseId)
    }

    fun getExerciseDetailedDurationBydId(workoutId: Long, exerciseId: Long): LiveData<Int> {
        return dao.getExerciseDetailedBydId(workoutId,exerciseId)
    }*/

    suspend fun updateDuration(workoutId: Long, exerciseId: Long, duration: Int) {
        dao.updateDuration(workoutId,exerciseId,duration)
    }

    suspend fun updateRepAndSet(workoutId: Long, exerciseId: Long, set: Int, rep: Int) {
        dao.updateRepAndSet(workoutId,exerciseId,set,rep)
    }


}