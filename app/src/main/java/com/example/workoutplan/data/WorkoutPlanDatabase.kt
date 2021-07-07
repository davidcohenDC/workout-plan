package com.example.workoutplan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workoutplan.data.dao.*
import com.example.workoutplan.data.entity.*
import com.example.workoutplan.utilities.DATABASE_NAME
import com.example.workoutplan.workers.SeedDatabaseWorker


/**
 * A database that stores information.
 * And a global method to get access to the database.
 *
 */
@Database(entities = [Exercise::class, Category::class, Difficulty::class, Muscle::class, Workout::class, WorkoutExerciseCrossRef::class], version = 1, exportSchema = false)
abstract class WorkoutPlanDatabase : RoomDatabase() {

    /**
     * Connects the database to the ExerciseDAO.
     */
    abstract fun ExerciseDao(): ExerciseDao

    abstract fun categoryDao(): CategoryDao

    abstract fun difficultyDao(): DifficultyDao

    abstract fun muscleDao(): MuscleDao

    abstract fun workoutDao(): WorkoutDao

    abstract fun workoutExerciseCrossRefDao(): WorkoutExerciseCrossRefDao


    /**
     * Define a companion object, this allows us to add functions on the WorkoutPlanDatabase class.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getInstance.
         *
         * This will help us avoid repeatedly initializing the database, which is expensive.
         *
         *  The value of a volatile variable will never be cached, and all writes and
         *  reads will be done to and from the main memory. It means that changes made by one
         *  thread to shared data are visible to other threads.
         */
        @Volatile
        private var INSTANCE: WorkoutPlanDatabase? = null

        /**
         * Helper function to get the database.
         *
         * If a database has already been retrieved, the previous database will be returned.
         * Otherwise, create a new database.
         *
         * This function is threadsafe, and callers should cache the result for multiple database
         * calls to avoid overhead.
         *
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): WorkoutPlanDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create and pre-populate the database.
        private fun buildDatabase(context: Context): WorkoutPlanDatabase {
            return Room.databaseBuilder(context, WorkoutPlanDatabase::class.java, DATABASE_NAME)
                    .addCallback(
                            object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
                                    WorkManager.getInstance(context).enqueue(request)
                                }
                            }
                    )
                    .build()
        }
    }
}
