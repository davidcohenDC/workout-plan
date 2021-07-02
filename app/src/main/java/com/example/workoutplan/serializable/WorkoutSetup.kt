package com.example.workoutplan.serializable

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WorkoutSetup : Serializable {

    @SerializedName("title")
    var title: String = ""

    @SerializedName("category")
    var category: Long = 0L

    @SerializedName("difficulty")
    var difficulty: Long = 0L

    @SerializedName("muscle")
    var muscle: Long = 0L

    @SerializedName("exercises")
    var exercises: List<Long> = listOf()
}