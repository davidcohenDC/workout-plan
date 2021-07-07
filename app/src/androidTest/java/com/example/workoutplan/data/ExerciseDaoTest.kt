package com.example.workoutplan.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.example.workoutplan.data.dao.ExerciseDao
import com.example.workoutplan.data.entity.Exercise
import com.example.workoutplan.utilities.testExercises
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ExerciseDaoTest {

    private lateinit var database: WorkoutPlanDatabase
    private lateinit var exerciseDao: ExerciseDao
    private var testExerciseId: Long = 0


    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, WorkoutPlanDatabase::class.java).build()
        exerciseDao = database.ExerciseDao()

        database.ExerciseDao().insertAll(testExercises)
    }

    @After fun closeDb() {
        database.close()
    }

    @Test fun testExercises() = runBlocking {
        val exercise = Exercise(
                1L,
                "1st description",
                "first",
                1L,
                1L,
                1L
        )
        exerciseDao.insert(exercise)
        assertThat(exerciseDao.getAll().first().name, equalTo("first"))
    }
}