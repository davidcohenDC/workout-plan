package com.example.workoutplan.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a session
 */
@Entity(tableName = "session")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Long = 0L,

    @ColumnInfo(name = "workout")
    val workoutId: Long,

    @ColumnInfo(name = "durata")
    val durata: Long,

    @ColumnInfo(name = "valutazione")
    val valutazione: Int

    )