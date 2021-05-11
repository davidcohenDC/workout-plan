package com.example.workoutplan.data.difficulty

import androidx.lifecycle.LiveData

class DifficultyRepository(private val dao: DifficultyDao){

    fun get(): LiveData<List<Difficulty>> {
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

    fun getCategoryById(difficultyId: Long): LiveData<Difficulty> {
        return dao.getDifficultyById(difficultyId)
    }

    suspend fun insertAll(difficultyList: List<Difficulty>) {
        return dao.insertAll(difficultyList)
    }
}