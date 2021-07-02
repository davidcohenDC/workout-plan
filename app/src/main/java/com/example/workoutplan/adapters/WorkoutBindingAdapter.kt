package com.example.workoutplan.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.data.entity.Workout

@BindingAdapter("workoutTitle")
fun TextView.setWorkoutTitle(workout: Workout?) {
    workout?.let {
        text = workout.title
    }
}

@BindingAdapter("workoutDescription")
fun TextView.setWorkoutDescription(workout: Workout?) {
    workout?.let {
        text = resources.getString(
            when (workout.categoryId) {
                1L -> R.string.strength_description
                2L -> R.string.cardio_description
                3L -> R.string.healthy_description
                else -> R.string.app_name
            }
        )
    }
}

@BindingAdapter("workoutCategory")
fun ImageView.setWorkoutCategory(workout: Workout?) {
    workout?.let {
        setImageResource(
            when (workout.categoryId) {
                1L -> R.drawable.ic_baseline_local_fire
                2L -> R.drawable.ic_drop
                3L -> R.drawable.ic_baseline_health_and_safety_24
                else -> R.drawable.ic_launcher_foreground
            }
        )
    }

}

@BindingAdapter("workoutDifficulty")
fun ImageView.setWorkoutDifficulty(workout: Workout?) {
    workout?.let {
        setImageResource(
            when (workout.difficultyId) {
                1L -> R.drawable.ic_beginner
                2L -> R.drawable.ic_average
                3L -> R.drawable.ic_advance
                else -> R.drawable.ic_expert
            }
        )
    }
}

@BindingAdapter("workoutMuscle")
fun ImageView.setWorkoutMuscle(workout: Workout?) {
    workout?.let {
        setImageResource(
            when (workout.muscleId) {
                1L -> R.drawable.ic_chest
                2L -> R.drawable.ic_arms
                3L -> R.drawable.ic_back
                4L -> R.drawable.ic_abs
                5L -> R.drawable.ic_leg
                else -> R.drawable.ic_launcher_foreground
            }
        )
    }
}
