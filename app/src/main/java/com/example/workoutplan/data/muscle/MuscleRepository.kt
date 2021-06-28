package com.example.workoutplan.data.muscle

import androidx.lifecycle.LiveData

class MuscleRepository(private val dao: MuscleDao){

    fun getAllMuscle(): LiveData<List<Muscle>> {
        return dao.getAllCategory()
    }

    suspend fun insert(muscle: Muscle) {
        dao.insert(muscle)
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

    suspend fun insertAll(muscleList: List<Muscle>) {
        return dao.insertAll(muscleList)
    }
}