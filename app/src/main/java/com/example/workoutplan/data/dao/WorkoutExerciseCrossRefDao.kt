package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.workoutplan.data.relations.ExerciseDetailed
@Dao
abstract class WorkoutExerciseCrossRefDao {

    @Query("SELECT WE.workoutId, E.exerciseId,E.description,E.name,E.muscle,E.difficulty,E.category, WE.`set`,WE.repetition,WE.duration FROM exercise E INNER JOIN workoutExerciseCross WE ON E.exerciseId = WE.exerciseId WHERE WE.workoutId = :workoutId")
    abstract fun getExercisesDetailedByWorkoutId(workoutId: Long): LiveData<List<ExerciseDetailed>>

    @Query("SELECT WE.workoutId, E.exerciseId,E.description,E.name,E.muscle,E.difficulty,E.category, WE.`set`,WE.repetition,WE.duration FROM exercise E INNER JOIN workoutExerciseCross WE ON E.exerciseId = WE.exerciseId WHERE WE.workoutId = :workoutId AND WE.exerciseId = :exerciseId")
    abstract fun getExerciseDetailedBydId(workoutId: Long, exerciseId: Long): LiveData<ExerciseDetailed>

    @Query("SELECT WE.`set` FROM exercise E INNER JOIN workoutExerciseCross WE ON E.exerciseId = WE.exerciseId WHERE WE.workoutId = :workoutId AND WE.exerciseId = :exerciseId")
    abstract fun getExerciseDetailedSetBydId(workoutId: Long, exerciseId: Long): LiveData<Int>

    @Query("SELECT WE.duration FROM exercise E INNER JOIN workoutExerciseCross WE ON E.exerciseId = WE.exerciseId WHERE WE.workoutId = :workoutId AND WE.exerciseId = :exerciseId")
    abstract fun getExerciseDetailedDurationBydId(workoutId: Long, exerciseId: Long): LiveData<Int>

    @Query("SELECT WE.repetition FROM exercise E INNER JOIN workoutExerciseCross WE ON E.exerciseId = WE.exerciseId WHERE WE.workoutId = :workoutId AND WE.exerciseId = :exerciseId")
    abstract fun getExerciseDetailedRepetitionBydId(workoutId: Long, exerciseId: Long): LiveData<Int>

    @Query("UPDATE workoutExerciseCross SET `set` = :set, repetition = :rep WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    abstract suspend fun updateRepAndSet(workoutId: Long, exerciseId: Long, set: Int, rep: Int)

    @Query("UPDATE workoutExerciseCross SET duration = :duration WHERE workoutId = :workoutId AND exerciseId = :exerciseId")
    abstract suspend fun updateDuration(workoutId: Long, exerciseId: Long, duration: Int)
}