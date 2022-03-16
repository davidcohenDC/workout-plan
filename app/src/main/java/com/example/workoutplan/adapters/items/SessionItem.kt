package com.example.workoutplan.adapters.items

data class SessionItem(
    val workoutId: Long,
    val exerciseId: Long,
    var repetition: Int?,
    var duration: Int?,
    var weight: Int?,
    var status: STATUS ) {

    companion object {
        enum class STATUS {
            UNDONE,
            DONE,
            DOING
        }
    }
}