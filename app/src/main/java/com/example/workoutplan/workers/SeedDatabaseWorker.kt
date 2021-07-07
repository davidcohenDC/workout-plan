package com.example.workoutplan.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.workoutplan.data.WorkoutPlanDatabase
import com.example.workoutplan.data.entity.Category
import com.example.workoutplan.data.entity.Difficulty
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.data.entity.Muscle
import com.example.workoutplan.utilities.CATEGORY_DATA_FILENAME
import com.example.workoutplan.utilities.DIFFICULTY_DATA_FILENAME
import com.example.workoutplan.utilities.EXERCISE_DATA_FILENAME
import com.example.workoutplan.utilities.MUSCLE_DATA_FILENAME
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.coroutineScope

class SeedDatabaseWorker(
        context: Context,
        workerParam: WorkerParameters,
) : CoroutineWorker(context, workerParam) {

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(EXERCISE_DATA_FILENAME).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader())
                        .use { jsonReader ->
                            val exerciseType = object : TypeToken<List<Exercise>>() {}.type
                            val exerciseList: List<Exercise> = Gson().fromJson(jsonReader, exerciseType)

                            val database = WorkoutPlanDatabase.getInstance(applicationContext)
                            database.ExerciseDao().insertAll(exerciseList)
                            Result.success()
                        }
            }
            applicationContext.assets.open(CATEGORY_DATA_FILENAME).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader())
                        .use { jsonReader ->
                            val categoryType = object : TypeToken<List<Category>>() {}.type
                            val categoryList: List<Category> = Gson().fromJson(jsonReader, categoryType)

                            val database = WorkoutPlanDatabase.getInstance(applicationContext)
                            database.categoryDao().insertAll(categoryList)
                            Result.success()
                        }
            }

            applicationContext.assets.open(DIFFICULTY_DATA_FILENAME).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader())
                        .use { jsonReader ->
                            val difficultyType = object : TypeToken<List<Difficulty>>() {}.type
                            val difficultyList: List<Difficulty> = Gson().fromJson(jsonReader, difficultyType)

                            val database = WorkoutPlanDatabase.getInstance(applicationContext)
                            database.difficultyDao().insertAll(difficultyList)
                            Result.success()
                        }
            }
            applicationContext.assets.open(MUSCLE_DATA_FILENAME).use { inputStream ->
                com.google.gson.stream.JsonReader(inputStream.reader())
                        .use { jsonReader ->
                            val muscleType = object : TypeToken<List<Muscle>>() {}.type
                            val muscleList: List<Muscle> = Gson().fromJson(jsonReader, muscleType)

                            val database = WorkoutPlanDatabase.getInstance(applicationContext)
                            database.muscleDao().insertAll(muscleList)
                            Result.success()
                        }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
    }
}
