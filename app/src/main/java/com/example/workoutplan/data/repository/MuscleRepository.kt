package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.MuscleDao
import com.example.workoutplan.data.entity.Muscle

class MuscleRepository(private val dao: MuscleDao) {

    fun getAllMuscle(): LiveData<List<Muscle>> {
        return dao.getAllCategory()
    }

    fun getAll(): List<Muscle> {
        return dao.getAll()
    }

    suspend fun insert(muscle: Muscle) {
        dao.insert(muscle)
    }

    suspend fun insertAll(muscleList: List<Muscle>) {
        return dao.insertAll(muscleList)
    }

    suspend fun delete(muscle: Muscle) {
        dao.delete(muscle)
    }

    suspend fun update(muscle: Muscle) {
        dao.update(muscle)
    }

    fun getMuscleById(muscleId: Long): LiveData<Muscle> {
        return dao.getMuscleById(muscleId)
    }

}