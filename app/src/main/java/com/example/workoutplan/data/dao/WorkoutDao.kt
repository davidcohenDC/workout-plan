package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef
import com.example.workoutplan.data.relations.WorkoutWithExercises

@Dao
abstract class WorkoutDao : BaseDao<Workout> {

    @Query("SELECT * FROM workout ORDER BY workoutId")
    abstract fun getAll(): List<Workout>

    @Query("SELECT * FROM workout ORDER BY workoutId")
    abstract fun getWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workout WHERE workoutId = :key")
    abstract fun getWorkoutById(key: Long): LiveData<Workout>

    @Query("SELECT COUNT(*) FROM workout")
    abstract fun getWorkoutsSize(): LiveData<Int>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutBinded(workout: Workout, workoutList: List<WorkoutExerciseCrossRef>) {
        insertAllWorkout(workoutList)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: WorkoutExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllWorkout(workoutList: List<WorkoutExerciseCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(workout: List<Workout>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWithId(workout: Workout): Long

    @Transaction
    @Query("SELECT * FROM workout")
    abstract fun getWorkoutWithExercises(): List<WorkoutWithExercises>

    @Query("SELECT * FROM workoutExerciseCross WHERE workoutId = :workoutId")
    abstract suspend fun getWorkoutWithExercisesCrossById(workoutId: Long): List<WorkoutExerciseCrossRef>

}