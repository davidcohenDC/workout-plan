package com.example.workoutplan.data.relations

data class SessionItem(
    val id:Int,
    val workoutId: Long,
    val exerciseId: Long,
    var repetition: Int?,
    var duration: Long?,
    var weight: Int?,
    var status: STATUS
    ) {

    companion object {
        enum class STATUS {
            UNDONE,
            DONE,
            DOING,
            PROTO
        }
    }
}