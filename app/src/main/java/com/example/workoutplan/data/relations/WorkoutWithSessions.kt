package com.example.workoutplan.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.workoutplan.data.entity.Session
import com.example.workoutplan.data.entity.Workout

data class WorkoutWithSessions(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workoutId",
        entityColumn = "sessionId",
    )
    val sessions: List<Session>,
)