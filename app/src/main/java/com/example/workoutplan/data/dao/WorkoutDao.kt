package com.example.workoutplan.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Workout
import com.example.workoutplan.data.entity.WorkoutExerciseCrossRef
import com.example.workoutplan.serializable.WorkoutSetup
import com.example.workoutplan.utilities.functions.configureWorkout
import com.example.workoutplan.utilities.functions.workoutSetupToWorkout

@Dao
abstract class WorkoutDao : BaseDao<Workout> {

    @Query("SELECT * FROM workout ORDER BY workoutId")
    abstract fun getAll(): List<Workout>

    @Query("SELECT * FROM workout ORDER BY workoutId")
    abstract fun getWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workout WHERE workoutId = :key")
    abstract fun getWorkoutById(key: Long): LiveData<Workout>

    @Query("DELETE FROM workoutExerciseCross WHERE workoutId = :workoutId")
    abstract suspend fun deleteWorkoutCross(workoutId: Long)

    @Query("SELECT COUNT(*) FROM workout")
    abstract fun getWorkoutsSize(): LiveData<Int>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutBinded(workout: Workout, workoutList: List<WorkoutExerciseCrossRef>) {
        insertWithId(workout)
        insertAllWorkout(workoutList)
    }

    @Transaction
    @Delete
    suspend fun removeWorkout(workout: Workout) {
        delete(workout)
        deleteWorkoutCross(workout.workoutId)
    }

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun finalQuery(exerciseList: List<Exercise>, workoutSetup: WorkoutSetup) {
        val id = insertWithId(workoutSetupToWorkout(workoutSetup))
        val list = configureWorkout(id, workoutSetup, exerciseList)
       insertAllWorkout(list)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWorkout(workout: WorkoutExerciseCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllWorkout(workoutList: List<WorkoutExerciseCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(workout: List<Workout>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertWithId(workout: Workout): Long

    @Query("SELECT * FROM workoutExerciseCross WHERE workoutId = :workoutId")
    abstract fun getWorkoutWithExercisesCrossById(workoutId: Long): LiveData<List<WorkoutExerciseCrossRef>>

}