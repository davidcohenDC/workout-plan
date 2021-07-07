package com.example.workoutplan.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.workoutplan.R
import com.example.workoutplan.data.relations.ExerciseDetailed

@BindingAdapter("exerciseDetailedImage")
fun ImageView.setExerciseDetailedImage(item: ExerciseDetailed?) {
    item?.let {
        setImageResource(
                when (item.exerciseId) {
                    1L -> R.drawable.ic_child_pose
                    2L -> R.drawable.ic_crunch
                    3L -> R.drawable.ic_cyclette
                    4L -> R.drawable.ic_deadlift
                    5L -> R.drawable.ic_hollow_body
                    6L -> R.drawable.ic_plank
                    7L -> R.drawable.ic_push_up
                    8L -> R.drawable.ic_running
                    9L -> R.drawable.ic_squat
                    10L -> R.drawable.ic_streching
                    11L -> R.drawable.ic_jumping_rope
                    12L -> R.drawable.ic_curl
                    13L -> R.drawable.ic_donkey_kick
                    14L -> R.drawable.ic_inclined_curl
                    15L -> R.drawable.ic_hoverhead_circles
                    16L -> R.drawable.ic_dumbbells_squat
                    17L -> R.drawable.ic_pull_up
                    18L -> R.drawable.ic_military_press
                    19L -> R.drawable.ic_handstand_ball
                    20L -> R.drawable.ic_flipping_tire
                    21L -> R.drawable.ic_chest_press
                    else -> R.drawable.ic_launcher_background
                }
        )
    }
}

@BindingAdapter("exerciseDetailedTitle")
fun TextView.setExerciseDetailedTitle(item: ExerciseDetailed?) {
    item?.let {
        text = resources.getString(when (item.exerciseId) {
            1L -> R.string.child_pose
            2L -> R.string.crunch
            3L -> R.string.exercise_bike
            4L -> R.string.deadlift
            5L -> R.string.hollow_body
            6L -> R.string.plank
            7L -> R.string.push_up
            8L -> R.string.jogging
            9L -> R.string.squat
            10L -> R.string.stretching
            11L -> R.string.jumping_rope
            12L -> R.string.curl
            13L -> R.string.donkey_kick
            14L -> R.string.inclined_curl
            15L -> R.string.hoverhead_circles
            16L -> R.string.dumbbells_curl
            17L -> R.string.pull_up
            18L -> R.string.miitary_press
            19L -> R.string.ball_handstand
            20L -> R.string.flipping_tire
            21L -> R.string.bench_press
            else -> R.string.app_name
        }
        )
    }
}

@BindingAdapter("setVisible")
    fun View.isSetVisible(exercise: ExerciseDetailed?) {
    exercise?.let {
        visibility = when (exercise.category) {
            1L -> View.VISIBLE
            2L -> View.GONE
            3L -> View.GONE
            else -> View.GONE
        }
    }
}

    @BindingAdapter("repetitionVisible")
    fun View.isRepetitionVisible(exercise: ExerciseDetailed?) {
        exercise?.let {
            when(exercise.category) {
                1L -> visibility = View.VISIBLE
                2L -> visibility = View.GONE
                3L -> visibility = View.GONE
            }
        }

    }

    @BindingAdapter("durationVisible")
    fun View.isDurationVisible(exercise: ExerciseDetailed?) {
        exercise?.let {
            when(exercise.category) {
                1L -> visibility = View.GONE
                2L -> visibility = View.VISIBLE
                3L -> visibility = View.VISIBLE
            }
        }

    }
