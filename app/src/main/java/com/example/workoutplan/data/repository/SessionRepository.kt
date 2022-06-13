package com.example.workoutplan.data.repository

import androidx.lifecycle.LiveData
import com.example.workoutplan.data.dao.SessionDao
import com.example.workoutplan.data.entity.Session

class SessionRepository(private val dao: SessionDao ) {

    fun getSessions(): LiveData<List<Session>> {
        return dao.getSessions()
    }

    suspend fun getAll(): List<Session> {
        return dao.getAll()
    }

    suspend fun insert(session: Session) {
        dao.insert(session)
    }

    suspend fun delete(session: Session) {
        dao.delete(session)
    }

    suspend fun update(session: Session) {
        dao.update(session)
    }

    fun getSessionById(sessionId: Long): LiveData<Session> {
        return dao.getSessionById(sessionId)
    }

    fun getLast(): LiveData<Session> {
        return dao.getLast()
    }


}