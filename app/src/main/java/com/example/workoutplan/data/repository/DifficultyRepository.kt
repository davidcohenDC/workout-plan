package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.DifficultyDao
import com.example.workoutplan.data.entity.Difficulty

class DifficultyRepository(private val dao: DifficultyDao) {

    fun getAllDifficulty(): LiveData<List<Difficulty>> {
        return dao.getAllDifficulty()
    }

    suspend fun insert(difficulty: Difficulty) {
        dao.insert(difficulty)
    }

    suspend fun delete(difficulty: Difficulty) {
        dao.delete(difficulty)
    }

    suspend fun update(difficulty: Difficulty) {
        dao.update(difficulty)
    }

    fun getDifficultyById(difficultyId: Long): LiveData<Difficulty> {
        return dao.getDifficultyById(difficultyId)
    }

    suspend fun insertAll(difficultyList: List<Difficulty>) {
        return dao.insertAll(difficultyList)
    }
}